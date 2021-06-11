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

package me.reim.androidtemplate.infrastructure.database

import androidx.paging.PagingSource
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import me.reim.androidtemplate.infrastructure.database.data.QiitaArticleAndUserData
import me.reim.androidtemplate.infrastructure.database.data.QiitaArticleData
import me.reim.androidtemplate.infrastructure.database.data.QiitaUserData

@Dao
interface QiitaArticleAndUserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllUser(qiitaUserData: List<QiitaUserData>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllArticle(qiitaArticleData: List<QiitaArticleData>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllArticleAndUser(qiitaUserData: List<QiitaUserData>, qiitaArticleData: List<QiitaArticleData>)

    @Query("DELETE FROM qiita_users")
    suspend fun clearQiitaUsers()

    @Query("DELETE FROM qiita_articles")
    suspend fun clearQiitaArticles()

    @Transaction
    @Query("SELECT * FROM qiita_articles")
    fun getAll(): Flow<List<QiitaArticleAndUserData>>

    @Transaction
    @Query(
        "SELECT * FROM qiita_articles " +
                "WHERE qiita_user_id = :qiitaUserId " +
                "ORDER BY created_at DESC"
    )
    fun articleByQiitaUserId(qiitaUserId: String): PagingSource<Int, QiitaArticleAndUserData>
}
