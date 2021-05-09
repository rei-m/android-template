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

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import me.reim.androidtemplate.domain.QiitaArticle
import me.reim.androidtemplate.domain.QiitaArticleRepository
import me.reim.androidtemplate.infrastructure.network.QiitaApiService
import javax.inject.Inject

class QiitaArticleRepositoryImpl @Inject constructor(
    private val defaultDispatcher: CoroutineDispatcher,
    private val qiitaApiService: QiitaApiService
) : QiitaArticleRepository {
    override val articles: Flow<List<QiitaArticle>> = flow<List<QiitaArticle>> {
        println("emit articles !!")
        emit(listOf())
    }.flowOn(defaultDispatcher).conflate()

    override suspend fun tryUpdateRecentArticlesCache() {

    }


}
