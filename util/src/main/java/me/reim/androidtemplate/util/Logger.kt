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

package me.reim.androidtemplate.util

import android.util.Log
import timber.log.Timber

object Logger {
    fun init(isDebug: Boolean) {
        if (isDebug) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(object : Timber.Tree() {
                override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
                    if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                        return
                    }

                    if (t == null) {
                        return
                    }

                    // Crashlyticsなどに送る感じ
                }
            })
        }
    }

    fun d(message: String, vararg args: Any?) = Timber.d(message, *args)

    fun d(t: Throwable, message: String, vararg args: Any?) = Timber.d(message, message, *args)

    fun d(t: Throwable) = Timber.d(t)

    fun e(message: String, vararg args: Any?) = Timber.e(message, *args)

    fun e(t: Throwable, message: String, vararg args: Any?) = Timber.e(t, message, *args)

    fun e(t: Throwable) = Timber.e(t)
}
