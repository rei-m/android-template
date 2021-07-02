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

package me.reim.androidtemplate.domain.qiita

import org.junit.jupiter.api.Assertions.assertEquals
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class Calculator() {
    fun add(a: Int, b: Int): Int {
        return a + b
    }
}

object ExampleSpekUnitTest : Spek({
    describe("A Calculator") {
        val calculator by memoized { Calculator() }

        it("should return 4") {
            assertEquals(4, calculator.add(2, 2))
        }

        it("should return 10") {
            assertEquals(10, calculator.add(5, 5))
        }

        it("should return 12") {
            assertEquals(12, calculator.add(6, 6))
        }
    }
})
