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

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import me.reim.androidtemplate.domain.common.exception.TimeoutException
import me.reim.androidtemplate.domain.qiita.exception.InvalidQiitaAccessTokenException
import me.reim.androidtemplate.domain.qiita.exception.QiitaAccessTokenMissingException
import me.reim.androidtemplate.domain.qiita.exception.QiitaUserNotFoundException
import me.reim.androidtemplate.feature.core.ErrorHandler
import me.reim.androidtemplate.feature.pagingsampler.R
import me.reim.androidtemplate.feature.pagingsampler.databinding.QiitaArticlesFragmentBinding

@AndroidEntryPoint
class QiitaArticlesFragment : Fragment() {
    private var _binding: QiitaArticlesFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: QiitaArticlesViewModel by activityViewModels()

    private val adapter = QiitaArticlesAdapter()

    private val errorHandler = ErrorHandler()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = QiitaArticlesFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpToolbar()

        setUpView()

        subscribeUI()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.qiita_articles_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.access_token_settings -> {
                val action = QiitaArticlesFragmentDirections.actionOpenEditAccessTokenDialog()
                findNavController().navigate(action)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setUpToolbar() {
        setHasOptionsMenu(true)
    }

    private fun setUpView() {
        val decoration = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        binding.listQiitaArticles.addItemDecoration(decoration)
        binding.listQiitaArticles.adapter = adapter.withLoadStateHeaderAndFooter(
            header = QiitaArticlesLoadStateAdapter { adapter.retry() },
            footer = QiitaArticlesLoadStateAdapter { adapter.retry() }
        )
        binding.buttonRetry.setOnClickListener { adapter.retry() }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun subscribeUI() {
        adapter.loadStateFlow.asLiveData().observe(viewLifecycleOwner, { loadState ->
            val refreshLoadState = loadState.refresh
            binding.refreshLoadState = refreshLoadState
            binding.message = null
            binding.isVisibleRetryButton = false

            if (refreshLoadState is LoadState.NotLoading && adapter.itemCount == 0) {
                binding.message = getString(R.string.message_empty)
            }

            if (refreshLoadState is LoadState.Error) {
                handleRefreshError(refreshLoadState.error)
            }
        })

        adapter.loadStateFlow
            .distinctUntilChangedBy { it.refresh }  // loadState???Refresh??????????????????????????????emit?????????
            .filter { it.refresh is LoadState.NotLoading }
            .asLiveData()   // ?????????LiveData??????????????????????????????
            .observe(viewLifecycleOwner, {
                // ???????????????????????????????????????????????????????????????TOP???????????????
                binding.listQiitaArticles.scrollToPosition(0)
            })

        viewModel.qiitaArticlePage.observe(viewLifecycleOwner, {
            adapter.submitData(lifecycle, it)
        })
    }

    private fun handleRefreshError(error: Throwable) {
        when (error) {
            is QiitaAccessTokenMissingException -> {
                binding.message = getString(R.string.message_not_found_access_token)
                binding.isVisibleRetryButton = false
            }
            is InvalidQiitaAccessTokenException -> {
                binding.message = getString(R.string.message_invalid_access_token)
                binding.isVisibleRetryButton = false
            }
            is QiitaUserNotFoundException -> {
                binding.message = getString(R.string.message_not_found_user_id)
                binding.isVisibleRetryButton = false
            }
            is TimeoutException -> {
                binding.message = getString(R.string.message_timeout)
                binding.isVisibleRetryButton = true
            }
            else -> {
                errorHandler.handle(requireContext(), binding.root, error)
            }
        }
    }
}
