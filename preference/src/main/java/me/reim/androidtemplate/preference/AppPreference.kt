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

package me.reim.androidtemplate.preference

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "preference")
private val QIITA_ACCESS_TOKEN = stringPreferencesKey("qiita_access_token")

@Singleton
class AppPreference @Inject constructor(@ApplicationContext private val context: Context) {
    val qiitaAccessTokenFlow: Flow<String> = context.dataStore.data
        .map { it[QIITA_ACCESS_TOKEN] ?: "" }

    suspend fun setQiitaAccessToken(newQiitaAccessToken: String) {
        context.dataStore.edit { preference ->
            preference[QIITA_ACCESS_TOKEN] = newQiitaAccessToken
        }
    }
}
