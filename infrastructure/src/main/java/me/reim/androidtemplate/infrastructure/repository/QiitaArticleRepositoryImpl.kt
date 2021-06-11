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

package me.reim.androidtemplate.infrastructure.repository

import androidx.paging.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.reim.androidtemplate.domain.*
import me.reim.androidtemplate.infrastructure.data.QiitaArticleMediator
import me.reim.androidtemplate.infrastructure.database.AppDatabase
import me.reim.androidtemplate.infrastructure.network.QiitaApiService

class QiitaArticleRepositoryImpl(
    private val qiitaApiService: QiitaApiService,
    private val appDatabase: AppDatabase,
) : QiitaArticleRepository {
    override fun getArticleStream(qiitaUserId: QiitaUserId): Flow<PagingData<QiitaArticle>> {
        val pagingSourceFactory = {
            appDatabase.qiitaArticleAndUserDao().articleByQiitaUserId(qiitaUserId.value)
        }

        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            remoteMediator = QiitaArticleMediator(
                qiitaUserId.value,
                qiitaApiService,
                appDatabase
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow.map { source ->
            source.map {
                val (qiitaArticleData, qiitaUserData) = it
                QiitaArticle(
                    id = QiitaArticleId(qiitaArticleData.id),
                    title = qiitaArticleData.title,
                    body = qiitaArticleData.body,
                    user = QiitaUser(
                        id = QiitaUserId(qiitaUserData.id),
                        name = qiitaUserData.name,
                        profileImageUrl = qiitaUserData.profileImageUrl
                    ),
                    createdAt = qiitaArticleData.createdAt,
                    updatedAt = qiitaArticleData.updatedAt,
                )
            }
        }
    }
}
