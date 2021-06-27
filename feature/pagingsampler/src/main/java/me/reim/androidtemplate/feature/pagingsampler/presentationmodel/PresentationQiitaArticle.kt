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

package me.reim.androidtemplate.feature.pagingsampler.presentationmodel

import me.reim.androidtemplate.domain.qiita.QiitaArticle
import java.text.SimpleDateFormat
import java.util.*

data class PresentationQiitaArticle(private val model: QiitaArticle) {
    val title: String = model.title
    val createdAtDate: String
        get() = formatCreatedAt("yyyy年MM月dd日")
    val createdAtYearMonth: String
        get() = formatCreatedAt("yyyy年MM月")

    fun isCreatedInSameYearMonth(other: PresentationQiitaArticle): Boolean {
        return createdAtYearMonth == other.createdAtYearMonth
    }

    override fun equals(other: Any?): Boolean {
        if (other !is PresentationQiitaArticle) return false
        return model == other.model
    }

    override fun hashCode(): Int {
        return model.hashCode()
    }

    private fun formatCreatedAt(pattern: String): String {
        val dateFormatter = SimpleDateFormat(pattern, Locale.getDefault())
        return dateFormatter.format(model.createdAt)
    }
}
