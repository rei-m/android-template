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
import dagger.multibindings.IntoSet
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
open class NetworkModule {
    @Singleton
    @Provides
    fun provideOkHttpClient(loggingInterceptors: Set<@JvmSuppressWildcards Interceptor>): OkHttpClient =
        OkHttpClient.Builder().apply {
            loggingInterceptors.forEach {
                addNetworkInterceptor(it)
            }
        }.build()

    @Provides
    @IntoSet
    fun provideNetworkLogger(): Interceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BASIC
    }
}
