package com.djonique.birdays.utils

import java.util.*

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

fun Date.year(): Int = Calendar.getInstance().let {
        it.time = this
        it.year
    }

fun Date.month(): Int = Calendar.getInstance().let {
        it.time = this
        it.month
    }

fun Date.day(): Int = Calendar.getInstance().let {
        it.time = this
        it.day
    }

fun Date.daysTo(other: Date): Int {
    val thisCal = Calendar.getInstance()
    val otherCal = Calendar.getInstance().apply {
        time = other
        set(Calendar.HOUR_OF_DAY, 10)
    }

    thisCal.time = this
    thisCal.set(Calendar.HOUR_OF_DAY, 9)

    if(thisCal.month < otherCal.month
            || (thisCal.month == otherCal.month && thisCal.day <= otherCal.day)) {
        otherCal.year = thisCal.year
    } else {
        otherCal.year = thisCal.year + 1
    }

    return thisCal.daysTo(otherCal)
}

fun Date.daysTo(): Int = Calendar.getInstance().time.daysTo(this)

fun Date.nextEvent(): Date {
    return Calendar.getInstance().let {
        it.time = this
        it.nextEvent()
        it.time
    }
}