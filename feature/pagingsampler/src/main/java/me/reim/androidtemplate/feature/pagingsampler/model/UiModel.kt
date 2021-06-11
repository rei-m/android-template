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

package me.reim.androidtemplate.feature.pagingsampler.model

import me.reim.androidtemplate.domain.QiitaArticle
import java.text.SimpleDateFormat
import java.util.*

sealed class UiModel {
    data class QiitaArticleItem(val qiitaArticle: QiitaArticle) : UiModel()
    data class SeparatorItem(val description: String) : UiModel()
}

val UiModel.QiitaArticleItem.createdAtYearMonth: String
    get() {
        val dateFormatter = SimpleDateFormat("yyyy年MM月", Locale.getDefault())
        return dateFormatter.format(qiitaArticle.createdAt)
    }
