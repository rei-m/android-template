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

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import me.reim.androidtemplate.domain.QiitaArticle
import me.reim.androidtemplate.feature.pagingsampler.databinding.ArticleViewItemBinding

class QiitaArticleViewHolder(private val binding: ArticleViewItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(qiitaArticle: QiitaArticle?) {
        binding.qiitaArticle = qiitaArticle
    }

    companion object {
        fun create(parent: ViewGroup): QiitaArticleViewHolder {
            val binding = ArticleViewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return QiitaArticleViewHolder(binding)
        }
    }
}
