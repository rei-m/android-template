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

package me.reim.androidtemplate.feature.pagingsampler.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import me.reim.androidtemplate.domain.QiitaArticle
import me.reim.androidtemplate.domain.QiitaArticleRepository
import me.reim.androidtemplate.domain.QiitaUserId
import javax.inject.Inject

@HiltViewModel
class QiitaArticlesViewModel @Inject constructor(
    private val qiitaArticleRepository: QiitaArticleRepository
) : ViewModel() {
    private var currentQueryValue: String? = null

    private var currentSearchResult: Flow<PagingData<QiitaArticle>>? = null

    fun search(queryString: String): Flow<PagingData<QiitaArticle>> {
        val lastResult = currentSearchResult
        if (queryString == currentQueryValue && lastResult != null) {
            return lastResult
        }
        currentQueryValue = queryString
        val newResult: Flow<PagingData<QiitaArticle>> =
            qiitaArticleRepository.getArticleStream(QiitaUserId(queryString)).cachedIn(viewModelScope)
        currentSearchResult = newResult
        return newResult
    }
}
