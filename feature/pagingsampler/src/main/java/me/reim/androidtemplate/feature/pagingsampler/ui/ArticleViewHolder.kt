/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.reim.androidtemplate.feature.pagingsampler.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import me.reim.androidtemplate.domain.QiitaArticle
import me.reim.androidtemplate.feature.pagingsampler.R

class ArticleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val name: TextView = view.findViewById(R.id.article_name)

    private var repo: QiitaArticle? = null

    init {
//        view.setOnClickListener {
//            repo?.url?.let { url ->
//                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
//                view.context.startActivity(intent)
//            }
//        }
    }

    fun bind(article: QiitaArticle?) {
        if (article == null) {
            name.text = "loading..."
        } else {
            showRepoData(article)
        }
    }

    private fun showRepoData(article: QiitaArticle) {
        this.repo = article
        name.text = article.title

    }

    companion object {
        fun create(parent: ViewGroup): ArticleViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.article_view_item, parent, false)
            return ArticleViewHolder(view)
        }
    }
}
