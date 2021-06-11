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

package me.reim.androidtemplate.feature.core.bindingadapter

import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.databinding.BindingAdapter

@BindingAdapter("onFinishEditAction")
fun EditText.onFinishEditAction(invoke: Function1<String, Unit>?) {
    if (invoke == null) {
        setOnEditorActionListener(null)
        setOnKeyListener(null)
        return
    }

    setOnEditorActionListener { _, actionId, _ ->
        val imeAction = when (actionId) {
            EditorInfo.IME_ACTION_DONE,
            EditorInfo.IME_ACTION_SEND,
            EditorInfo.IME_ACTION_GO -> true
            else -> false
        }
        if (imeAction) {
            invoke(text.toString())
        }
        return@setOnEditorActionListener imeAction
    }
    setOnKeyListener { _, keyCode, event ->
        val eventAction = event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER
        if (eventAction) {
            invoke(text.toString())
        }
        return@setOnKeyListener eventAction
    }
}
