package com.djonique.birdays.utils

import android.app.AlarmManager
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import android.support.annotation.PluralsRes
import android.support.v4.content.ContextCompat

/**
 * Birdays
 *
 * Created by Victor Nidens
 * Date: 09.01.2018
 *
 * The MIT License (MIT)
 *
 * Copyright (c) 2018 Victor Nidens
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

@ColorInt
fun Context.getColorCompat(@ColorRes color: Int): Int =
        ContextCompat.getColor(this, color)

inline val Context.alarmManager: AlarmManager
    get() = getSystemService(Context.ALARM_SERVICE) as AlarmManager

inline val Context.notificationManager: NotificationManager
    get() = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

inline val Context.sharedPreferencesDefault: SharedPreferences
    get() = PreferenceManager.getDefaultSharedPreferences(this)

inline fun Context.getQuantityString(@PluralsRes res: Int, quantity: Int): String
        = resources.getQuantityString(res, quantity)

inline fun Context.getQuantityString(@PluralsRes res: Int, quantity: Int, vararg formatArgs: Any): String
        = resources.getQuantityString(res, quantity, formatArgs)
