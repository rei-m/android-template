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
import android.os.Handler
import android.os.Looper
import android.view.View
import java.util.*

class ErrorHandler {
    private val queue = LinkedList<ErrorRunnable>()
    private val handler = Handler(Looper.getMainLooper())

    fun handle(
        context: Context, view: View, throwable: Throwable
    ) {
        val runnable = ErrorRunnable(Error.convert(context, view, throwable))
        handle(runnable)
    }

    fun handle(runnable: ErrorRunnable) {
        if (queue.none { it.error == runnable.error }) {
            queue.add(runnable)
            handler.post(runnable)
        }
    }

    inner class ErrorRunnable(val error: Error) : Runnable {
        override fun run() {
            error.handle()
            val running = this
            handler.postDelayed({
                queue.remove(running)
            }, 200)
        }
    }
}
