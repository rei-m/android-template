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

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import me.reim.androidtemplate.domain.qiita.QiitaArticleRepository
import me.reim.androidtemplate.domain.qiita.QiitaUserId
import me.reim.androidtemplate.feature.pagingsampler.presentationmodel.AdapterItem
import me.reim.androidtemplate.feature.pagingsampler.presentationmodel.PresentationQiitaArticle
import javax.inject.Inject

@HiltViewModel
class QiitaArticlesViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val qiitaArticleRepository: QiitaArticleRepository
) : ViewModel() {
    private val initialQiitaUserIdText: String = savedStateHandle[QIITA_USER_ID_KEY] ?: let { DEFAULT_QIITA_USER_ID }
    val qiitaUserIdText: MutableLiveData<String> = MutableLiveData(initialQiitaUserIdText)

    private val searchQiitaUserIdTextFlow: MutableStateFlow<String> = MutableStateFlow(initialQiitaUserIdText)

    @ExperimentalCoroutinesApi
    val qiitaArticlePageFlow: LiveData<PagingData<AdapterItem>> = searchQiitaUserIdTextFlow.flatMapLatest { inputted ->
        qiitaArticleRepository.getArticleFlow(QiitaUserId(inputted)).map { pagingData ->
            pagingData.map { AdapterItem.QiitaArticleItem(PresentationQiitaArticle(it)) }
        }.map { pagingData ->
            pagingData.insertSeparators { before, after ->
                if (after == null) {
                    // afterがnullの場合はリストの最後なのでseparatorなし
                    return@insertSeparators null
                }

                if (before == null) {
                    // beforeがnullの場合はリストの先頭なのでseparatorを返す
                    return@insertSeparators AdapterItem.SeparatorItem(after.qiitaArticle.createdAtYearMonth)
                }

                // before / after 両方要素が存在する場合はseparatorを返すか判定する
                if (before.qiitaArticle.isCreatedInSameYearMonth(after.qiitaArticle)) {
                    null
                } else {
                    AdapterItem.SeparatorItem(after.qiitaArticle.createdAtYearMonth)
                }
            }
        }.cachedIn(viewModelScope)
    }.asLiveData()

    private fun getArticles(inputted: String) {
        if (inputted.isBlank()) {
            return
        }
        searchQiitaUserIdTextFlow.value = inputted
        savedStateHandle[QIITA_USER_ID_KEY] = inputted
    }

    val getArticles: Function1<String, Unit> = this::getArticles

    companion object {
        private const val QIITA_USER_ID_KEY = "qiita_user_id_key"
        private const val DEFAULT_QIITA_USER_ID = "rei-m"
    }
}
