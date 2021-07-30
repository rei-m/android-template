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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import me.reim.androidtemplate.feature.composesampler.extension.navigateToCounter
import me.reim.androidtemplate.feature.composesampler.extension.navigateToSideEffect
import me.reim.androidtemplate.feature.composesampler.extension.rootView
import me.reim.androidtemplate.feature.composesampler.ui.theme.AndroidTemplateTheme

class ComposeMainFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return rootView {
            ComposeMainFragmentView(onClickOpenCounter = {
                navigateToCounter()
            }, onClickOpenSideEffect = {
                navigateToSideEffect()
            })
        }
    }
}

@Composable
private fun ComposeMainFragmentView(onClickOpenCounter: () -> Unit = {}, onClickOpenSideEffect: () -> Unit = {}) {
    Surface {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = onClickOpenCounter
            ) {
                Text(
                    text = "基本のサンプル（カウンターアプリ）",
                    style = MaterialTheme.typography.button
                )
            }

            Button(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth(),
                onClick = onClickOpenSideEffect
            ) {
                Text(
                    text = "副作用の確認",
                    style = MaterialTheme.typography.button
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AndroidTemplateTheme {
        ComposeMainFragmentView()
    }
}
