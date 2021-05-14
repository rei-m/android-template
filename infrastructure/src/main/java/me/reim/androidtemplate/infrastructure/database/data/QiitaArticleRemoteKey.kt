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

package me.reim.androidtemplate.infrastructure.database.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "qiita_article_remote_keys")
data class QiitaArticleRemoteKey  (
    @PrimaryKey
    val qiitaArticleId: String,
    val prevKey: Int?,
    val nextKey: Int?
)
