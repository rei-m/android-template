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
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import me.reim.androidtemplate.feature.composesampler.extension.rootView

class CounterFragment : Fragment() {

    private val liveDataViewModel: LiveDataViewModel by activityViewModels()
    private val flowViewModel: FlowViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return rootView {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                RememberStateView()
                RememberSavableStateView()
                LiveDataStateView(viewModel = liveDataViewModel)
                FlowStateView(viewModel = flowViewModel)
            }
        }
    }
}

class LiveDataViewModel : ViewModel() {
    private val _count = MutableLiveData(0)
    val count: LiveData<Int> = _count

    fun onChangeCount(newCount: Int) {
        _count.value = newCount
    }
}

class FlowViewModel : ViewModel() {
    private val _count = MutableStateFlow<Int>(0)
    val count: StateFlow<Int> = _count

    fun onChangeCount(newCount: Int) {
        _count.value = newCount
    }
}

@Composable
private fun RememberStateView() {
    var count by remember { mutableStateOf(0) }
    CounterFragmentViewPresenter(
        title = "remember",
        count = count,
        onClickPlus = {
            count += 1
        }, onClickMinus = {
            count -= 1
        }
    )
}

@Composable
private fun RememberSavableStateView() {
    var count by rememberSaveable { mutableStateOf(0) }
    CounterFragmentViewPresenter(
        title = "rememberSaveable",
        count = count,
        onClickPlus = {
            count += 1
        }, onClickMinus = {
            count -= 1
        }
    )
}

@Composable
private fun LiveDataStateView(viewModel: LiveDataViewModel) {
    val count by viewModel.count.observeAsState(0)
    CounterFragmentViewPresenter(
        title = "ViewModel + LiveData",
        count = count,
        onClickPlus = {
            viewModel.onChangeCount(count + 1)
        }, onClickMinus = {
            viewModel.onChangeCount(count - 1)
        }
    )
}

@Composable
private fun FlowStateView(viewModel: FlowViewModel) {
    val count by viewModel.count.collectAsState(0)
    CounterFragmentViewPresenter(
        title = "ViewModel + Flow",
        count = count,
        onClickPlus = {
            viewModel.onChangeCount(count + 1)
        }, onClickMinus = {
            viewModel.onChangeCount(count - 1)
        }
    )
}

@Composable
private fun CounterFragmentViewPresenter(title: String, count: Int, onClickPlus: () -> Unit, onClickMinus: () -> Unit) {
    Surface {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.h3,
                fontSize = 24.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
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
