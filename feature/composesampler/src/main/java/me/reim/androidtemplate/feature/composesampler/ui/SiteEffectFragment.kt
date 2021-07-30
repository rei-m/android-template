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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import me.reim.androidtemplate.feature.composesampler.extension.rootView

class SiteEffectFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return rootView {
            SiteEffectFragmentView()
        }
    }
}

@Composable
private fun SiteEffectFragmentView() {
    val scaffoldState = rememberScaffoldState()
    var submitted by remember { mutableStateOf("") }
    var errorCount by remember { mutableStateOf(0) }
    if (0 < errorCount) {
        LaunchedEffect(key1 = errorCount) {
            scaffoldState.snackbarHostState.showSnackbar(
                message = "なんか入力してください"
            )
        }
    }

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
                TextForm(onSubmit = {
                    submitted = it
                    if (submitted.isEmpty()) {
                        errorCount += 1
                    } else {
                        errorCount = 0
                    }
                })
            }
        })
}

@Composable
private fun TextForm(initialValue: String = "", onSubmit: (value: String) -> Unit) {
    var text by remember { mutableStateOf(initialValue) }
    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = text,
            onValueChange = { text = it },
            label = { Text("ラベル") }
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
