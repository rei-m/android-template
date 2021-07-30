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

package me.reim.androidtemplate.feature.composesampler.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import kotlinx.coroutines.launch
import me.reim.androidtemplate.feature.composesampler.extension.rootView

class SideEffectFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return rootView {
            SiteEffectFragmentView(requireActivity().onBackPressedDispatcher)
        }
    }
}

@Composable
private fun SiteEffectFragmentView(backDispatcher: OnBackPressedDispatcher) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = scaffoldState,
        drawerContent = { Text("Drawer content") },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Inc") },
                onClick = { /* fab click handler */ }
            )
        },
        content = { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                Text("空文字でボタンを押すとsnackbarを表示するサンプル")
                Divider(modifier = Modifier.padding(8.dp))
                LaunchedEffectView(scaffoldState = scaffoldState)
                Divider(modifier = Modifier.padding(8.dp))
                RememberCoroutineScopeView(scaffoldState = scaffoldState)
            }
            BackHandler(backDispatcher) {
                scope.launch {
                    scaffoldState.snackbarHostState
                        .showSnackbar("DisposableEffectで登録された戻るボタンのイベント")
                }
            }
        })
}

@Composable
private fun LaunchedEffectView(scaffoldState: ScaffoldState) {
    var submitted by remember { mutableStateOf("") }
    var errorCount by remember { mutableStateOf(0) }
    if (0 < errorCount) {
        LaunchedEffect(key1 = errorCount) {
            scaffoldState.snackbarHostState.showSnackbar(
                message = "これはcompose時に呼ばれている"
            )
        }
    }

    TextForm(
        label = "LaunchedEffectの確認",
        onSubmit = {
            submitted = it
            if (submitted.isEmpty()) {
                errorCount += 1
            } else {
                errorCount = 0
            }
        })
}

@Composable
private fun RememberCoroutineScopeView(scaffoldState: ScaffoldState) {
    val scope = rememberCoroutineScope()
    TextForm(
        label = "LaunchedEffectの確認",
        onSubmit = {
            scope.launch {
                if (it.isEmpty()) {
                    scaffoldState.snackbarHostState
                        .showSnackbar("これはsubmitのイベントハンドラ内で呼ばれている")
                }
            }
        })
}

@Composable
private fun TextForm(initialValue: String = "", label: String = "", onSubmit: (value: String) -> Unit) {
    var text by remember { mutableStateOf(initialValue) }
    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = text,
            onValueChange = { text = it },
            label = { Text(label) }
        )
        Button(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            onClick = {
                onSubmit(text)
            }
        ) {
            Text(
                text = "さぶみっと",
                style = MaterialTheme.typography.button
            )
        }
    }
}


@Composable
fun BackHandler(backDispatcher: OnBackPressedDispatcher, onBack: () -> Unit) {
    // Safely update the current `onBack` lambda when a new one is provided
    val currentOnBack by rememberUpdatedState(onBack)

    // Remember in Composition a back callback that calls the `onBack` lambda
    val backCallback = remember {
        // Always intercept back events. See the SideEffect for
        // a more complete version
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                currentOnBack()
            }
        }
    }

    // If `backDispatcher` changes, dispose and reset the effect
    DisposableEffect(backDispatcher) {
        // Add callback to the backDispatcher
        backDispatcher.addCallback(backCallback)

        // When the effect leaves the Composition, remove the callback
        onDispose {
            backCallback.remove()
            println("onDispose is called")
        }
    }
}
