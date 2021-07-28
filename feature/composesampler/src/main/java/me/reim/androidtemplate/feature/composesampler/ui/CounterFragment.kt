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
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import me.reim.androidtemplate.feature.composesampler.extension.rootView

class CounterFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return rootView {
            CounterFragmentView()
        }
    }
}

@Composable
private fun CounterFragmentView() {
    var count by remember { mutableStateOf(0) }
    CounterFragmentViewPresenter(
        count = count,
        onClickPlus = {
            count += 1
        }, onClickMinus = {
            count -= 1
        }
    )
}

@Composable
private fun CounterFragmentViewPresenter(count: Int, onClickPlus: () -> Unit, onClickMinus: () -> Unit) {
    Surface {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = count.toString(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.body1,
                fontSize = 32.sp,
                modifier = Modifier
                    .fillMaxWidth()
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Button(
                    onClick = onClickMinus,
                ) {
                    Text(
                        text = "-",
                        style = MaterialTheme.typography.button,
                        fontSize = 32.sp,
                    )
                }
                Button(
                    onClick = onClickPlus,
                ) {
                    Text(
                        text = "+",
                        style = MaterialTheme.typography.button,
                        fontSize = 32.sp,
                    )
                }
            }
        }
    }
}
