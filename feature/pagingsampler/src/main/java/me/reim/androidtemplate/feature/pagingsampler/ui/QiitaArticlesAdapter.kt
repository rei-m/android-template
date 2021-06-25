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
import me.reim.androidtemplate.feature.pagingsampler.presentationmodel.AdapterItem

class QiitaArticlesAdapter : PagingDataAdapter<AdapterItem, RecyclerView.ViewHolder>(COMPARATOR) {
    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is AdapterItem.QiitaArticleItem -> R.layout.qiita_article_view_item
            is AdapterItem.SeparatorItem -> R.layout.separator_view_item
            else -> throw UnsupportedOperationException("Unknown view")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItem(position).let {
            when (it) {
                is AdapterItem.QiitaArticleItem -> (holder as QiitaArticleViewHolder).bind(it.qiitaArticle)
                is AdapterItem.SeparatorItem -> (holder as SeparatorViewHolder).bind(it.description)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.qiita_article_view_item -> QiitaArticleViewHolder.create(parent)
            R.layout.separator_view_item -> SeparatorViewHolder.create(parent)
            else -> throw UnsupportedOperationException("Unknown viewType")
        }
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<AdapterItem>() {
            override fun areItemsTheSame(oldItem: AdapterItem, newItem: AdapterItem): Boolean {
                return (oldItem is AdapterItem.QiitaArticleItem && newItem is AdapterItem.QiitaArticleItem &&
                        oldItem.qiitaArticle == newItem.qiitaArticle) ||
                        (oldItem is AdapterItem.SeparatorItem && newItem is AdapterItem.SeparatorItem &&
                                oldItem.description == newItem.description)
            }

            override fun areContentsTheSame(oldItem: AdapterItem, newItem: AdapterItem): Boolean =
                oldItem == newItem
        }
    }
}
