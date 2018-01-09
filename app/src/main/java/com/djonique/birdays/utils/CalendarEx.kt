package com.djonique.birdays.utils

import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Birdays
 *
 * Created by Victor Nidens
 * Date: 17.12.2017
 *
 * The MIT License (MIT)
 *
 * Copyright (c) 2017 Victor Nidens
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the Birdays), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

var Calendar.year: Int
    get() = get(Calendar.YEAR)
    set(value) {
        set(Calendar.YEAR, value)
    }

val Calendar.month: Int
    get() = get(Calendar.MONTH)

val Calendar.day: Int
    get() = get(Calendar.DAY_OF_MONTH)

fun Calendar.daysTo(other: Calendar): Int
        = TimeUnit.MILLISECONDS.toDays(other.timeInMillis - timeInMillis).toInt()

fun Calendar.daysTo(): Int
        = Calendar.getInstance().daysTo(this)