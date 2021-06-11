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

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import me.reim.androidtemplate.feature.pagingsampler.R
import me.reim.androidtemplate.feature.pagingsampler.model.UiModel

class QiitaArticlesAdapter : PagingDataAdapter<UiModel, RecyclerView.ViewHolder>(COMPARATOR) {
    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is UiModel.QiitaArticleItem -> R.layout.article_view_item
            is UiModel.SeparatorItem -> R.layout.separator_view_item
            null -> throw UnsupportedOperationException("Unknown view")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItem(position).let {
            when (it) {
                is UiModel.QiitaArticleItem -> (holder as QiitaArticleViewHolder).bind(it.qiitaArticle)
                is UiModel.SeparatorItem -> (holder as SeparatorViewHolder).bind(it.description)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.article_view_item -> QiitaArticleViewHolder.create(parent)
            R.layout.separator_view_item -> SeparatorViewHolder.create(parent)
            else -> throw UnsupportedOperationException("Unknown viewType")
        }
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<UiModel>() {
            override fun areItemsTheSame(oldItem: UiModel, newItem: UiModel): Boolean {
                return (oldItem is UiModel.QiitaArticleItem && newItem is UiModel.QiitaArticleItem &&
                        oldItem.qiitaArticle == newItem.qiitaArticle) ||
                        (oldItem is UiModel.SeparatorItem && newItem is UiModel.SeparatorItem &&
                                oldItem.description == newItem.description)
            }

            override fun areContentsTheSame(oldItem: UiModel, newItem: UiModel): Boolean =
                oldItem == newItem
        }
    }
}
