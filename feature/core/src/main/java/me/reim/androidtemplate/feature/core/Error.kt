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

package me.reim.androidtemplate.feature.core

import android.content.Context
import android.view.View
import me.reim.androidtemplate.domain.common.exception.MaintenanceException

sealed class Error {
    abstract fun handle()

    class MaintenanceError(private val context: Context) : Error() {
        override fun handle() {
            // メンテナンス表示用の画面を作ってバックスタックをクリアして移動
            TODO("Not yet implemented")
        }

        override fun equals(other: Any?): Boolean {
            return other is MaintenanceError
        }

        override fun hashCode(): Int {
            return context.hashCode()
        }
    }

    class UnhandledError(private val view: View, private val throwable: Throwable) : Error() {
        override fun handle() {
            // Crashlyticsに送る
            TODO("Not yet implemented")

        }
    }

    companion object {
        fun convert(
            context: Context,
            view: View,
            throwable: Throwable
        ): Error = when (throwable) {
            is MaintenanceException -> MaintenanceError(context)
            else -> UnhandledError(view, throwable)
        }
    }
}
