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
import me.reim.androidtemplate.infrastructure.network.QiitaApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object QiitaApiModule {
    @Provides
    fun provideQiitaApiService(okHttpClient: OkHttpClient): QiitaApiService {
        val retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://qiita.com/api/v2/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        return retrofit.create(QiitaApiService::class.java)
    }
}
