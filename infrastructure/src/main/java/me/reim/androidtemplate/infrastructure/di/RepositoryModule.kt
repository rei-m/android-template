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

package me.reim.androidtemplate.infrastructure.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import me.reim.androidtemplate.domain.QiitaArticleRepository
import me.reim.androidtemplate.infrastructure.network.QiitaApiService
import me.reim.androidtemplate.infrastructure.repository.QiitaArticleRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    fun provideQiitaArticleRepository(qiitaApiService: QiitaApiService): QiitaArticleRepository = QiitaArticleRepositoryImpl(
        Dispatchers.Default,
        qiitaApiService
    )
}
