/*
 * Copyright (c) 2021. Rei Matsushita
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */

package me.reim.androidtemplate.infrastructure.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import me.reim.androidtemplate.domain.common.exception.MaintenanceException
import me.reim.androidtemplate.domain.common.exception.TimeoutException
import me.reim.androidtemplate.domain.qiita.exception.InvalidQiitaAccessTokenException
import me.reim.androidtemplate.domain.qiita.exception.QiitaAccessTokenMissingException
import me.reim.androidtemplate.domain.qiita.exception.QiitaUserNotFoundException
import me.reim.androidtemplate.infrastructure.database.AppDatabase
import me.reim.androidtemplate.infrastructure.database.data.QiitaArticleAndUserData
import me.reim.androidtemplate.infrastructure.database.data.QiitaArticleData
import me.reim.androidtemplate.infrastructure.database.data.QiitaArticleRemoteKey
import me.reim.androidtemplate.infrastructure.database.data.QiitaUserData
import me.reim.androidtemplate.infrastructure.network.QiitaApiService
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalPagingApi::class)
class QiitaArticleMediator(
    private val qiitaAccessToken: String,
    private val qiitaUserId: String,
    private val service: QiitaApiService,
    private val database: AppDatabase
) : RemoteMediator<Int, QiitaArticleAndUserData>() {
    override suspend fun load(loadType: LoadType, state: PagingState<Int, QiitaArticleAndUserData>): MediatorResult {
        // stateは以前にロードされたページ、リスト内で最後にアクセスされたインデックス、およびページングストリームの初期化時に定義したPagingConfigに関する情報が得られます
        // loadTypeは以前にロードしたデータの最後（LoadType.APPEND）または最初（LoadType.PREPEND）でデータをロードする必要があるのか、それとも初めてデータをロードするのか（LoadType.REFRESH）がわかります

        if (qiitaAccessToken.isBlank()) {
            return MediatorResult.Error(QiitaAccessTokenMissingException())
        }

        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKey = getRemoteKeyClosestToCurrentPosition(state)
                remoteKey?.nextKey?.minus(1) ?: 1
            }
            LoadType.PREPEND -> {
                val remoteKey = getRemoteKeyForFirstItem(state)
                // If remoteKeys is null, that means the refresh result is not in the database yet.
                // We can return Success with `endOfPaginationReached = false` because Paging
                // will call this method again if RemoteKeys becomes non-null.
                // If remoteKeys is NOT NULL but its prevKey is null, that means we've reached
                // the end of pagination for prepend.
                remoteKey?.prevKey ?: let {
                    return MediatorResult.Success(endOfPaginationReached = remoteKey != null)
                }
            }
            LoadType.APPEND -> {
                val remoteKey = getRemoteKeyForLastItem(state)
                // If remoteKeys is null, that means the refresh result is not in the database yet.
                // We can return Success with endOfPaginationReached = false because Paging
                // will call this method again if RemoteKeys becomes non-null.
                // If remoteKeys is NOT NULL but its prevKey is null, that means we've reached
                // the end of pagination for append.
                remoteKey?.nextKey ?: let {
                    return MediatorResult.Success(endOfPaginationReached = remoteKey != null)
                }
            }
        }

        try {
            val apiResponseItems = service.getItems("Bearer $qiitaAccessToken", qiitaUserId, page)

            val qiitaArticleDataList = mutableListOf<QiitaArticleData>()
            val qiitaUserDataSet = mutableSetOf<QiitaUserData>()
            val endOfPaginationReached = apiResponseItems.isEmpty()

            apiResponseItems.forEach {
                val qiitaUserData = QiitaUserData(
                    id = it.user.id,
                    name = it.user.name,
                    profileImageUrl = it.user.profileImageUrl
                )
                qiitaUserDataSet.add(qiitaUserData)
                qiitaArticleDataList.add(
                    QiitaArticleData(
                        id = it.id,
                        title = it.title,
                        body = it.body,
                        qiitaUserOwnerId = qiitaUserData.id,
                        createdAt = dateFormatter.parse(it.createdAt)!!,
                        updatedAt = dateFormatter.parse(it.updatedAt)!!
                    )
                )
            }

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.qiitaArticleRemoteKeyDao().clearRemoteKeys()
                    database.qiitaArticleAndUserDao().clearQiitaArticles()
                    database.qiitaArticleAndUserDao().clearQiitaUsers()
                }
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = qiitaArticleDataList.map {
                    QiitaArticleRemoteKey(qiitaArticleId = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                database.qiitaArticleRemoteKeyDao().insertAll(keys)
                database.qiitaArticleAndUserDao()
                    .insertAllArticleAndUser(qiitaUserDataSet.toMutableList(), qiitaArticleDataList)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: UnknownHostException) {
            return MediatorResult.Error(TimeoutException(exception))
        } catch (exception: SocketTimeoutException) {
            return MediatorResult.Error(TimeoutException(exception))
        } catch (exception: HttpException) {
            return when (exception.code()) {
                401 -> {
                    MediatorResult.Error(InvalidQiitaAccessTokenException())
                }
                404 -> {
                    MediatorResult.Error(QiitaUserNotFoundException())
                }
                503 -> {
                    MediatorResult.Error(MaintenanceException(exception))
                }
                else -> {
                    MediatorResult.Error(exception)
                }
            }
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, QiitaArticleAndUserData>
    ): QiitaArticleRemoteKey? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.qiitaArticleData?.id?.let { qiitaArticleId ->
                database.qiitaArticleRemoteKeyDao().remoteKeyByQiitaArticleId(qiitaArticleId)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, QiitaArticleAndUserData>): QiitaArticleRemoteKey? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { qiitaArticleAndUser ->
                // Get the remote keys of the first items retrieved
                database.qiitaArticleRemoteKeyDao().remoteKeyByQiitaArticleId(qiitaArticleAndUser.qiitaArticleData.id)
            }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, QiitaArticleAndUserData>): QiitaArticleRemoteKey? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { qiitaArticleAndUser ->
                // Get the remote keys of the last item retrieved
                database.qiitaArticleRemoteKeyDao().remoteKeyByQiitaArticleId(qiitaArticleAndUser.qiitaArticleData.id)
            }
    }

    companion object {
        private val dateFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault())
    }
}
