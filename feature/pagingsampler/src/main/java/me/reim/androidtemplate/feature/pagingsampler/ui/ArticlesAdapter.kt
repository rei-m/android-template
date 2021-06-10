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
import me.reim.androidtemplate.domain.QiitaArticle

class ArticlesAdapter : PagingDataAdapter<QiitaArticle, RecyclerView.ViewHolder>(COMPARATOR) {
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ArticleViewHolder).bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ArticleViewHolder.create(parent)
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<QiitaArticle>() {
            override fun areItemsTheSame(oldItem: QiitaArticle, newItem: QiitaArticle): Boolean {
                return oldItem == newItem
//                return (oldItem is UiModel.RepoItem && newItem is UiModel.RepoItem &&
//                        oldItem.repo.fullName == newItem.repo.fullName) ||
//                        (oldItem is UiModel.SeparatorItem && newItem is UiModel.SeparatorItem &&
//                                oldItem.description == newItem.description)
            }

            override fun areContentsTheSame(oldItem: QiitaArticle, newItem: QiitaArticle): Boolean =
                oldItem == newItem
        }
    }
}
