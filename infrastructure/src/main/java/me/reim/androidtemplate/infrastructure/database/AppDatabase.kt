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

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import me.reim.androidtemplate.infrastructure.database.data.Converters
import me.reim.androidtemplate.infrastructure.database.data.QiitaArticleData
import me.reim.androidtemplate.infrastructure.database.data.QiitaArticleRemoteKey
import me.reim.androidtemplate.infrastructure.database.data.QiitaUserData

@Database(
    entities = [QiitaUserData::class, QiitaArticleData::class, QiitaArticleRemoteKey::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun qiitaArticleAndUserDao(): QiitaArticleAndUserDao
    abstract fun qiitaArticleRemoteKeyDao(): QiitaArticleRemoteKeyDao

    companion object {
        const val DATABASE_NAME = "android-template-db"
    }
}
