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

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.reim.androidtemplate.feature.pagingsampler.R
import me.reim.androidtemplate.preference.AppPreference
import javax.inject.Inject

@AndroidEntryPoint
class EditAccessTokenDialogFragment : DialogFragment() {

    @Inject
    lateinit var appPreference: AppPreference

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        val inflater = requireActivity().layoutInflater

        val dialogView = inflater.inflate(R.layout.edit_access_token_dialog, null)
        val editAccessToken = dialogView.findViewById<EditText>(R.id.edit_text_qiita_access_token)

        lifecycleScope.launch {
            appPreference.qiitaAccessTokenFlow.collectLatest {
                editAccessToken.setText(it)
            }
        }

        builder.setView(dialogView)
            .setTitle(R.string.access_token_setting)
            .setPositiveButton(R.string.set) { _, _ ->
                lifecycleScope.launch {
                    appPreference.setQiitaAccessToken(editAccessToken?.text.toString())
                }
            }
            .setNegativeButton(R.string.back) { dialog, _ ->
                dialog.cancel()
            }

        return builder.create()
    }
}
