package com.djonique.birdays.model

import android.arch.persistence.room.*
import java.util.*

/**
 * Birdays
 *
 * Created by Victor Nidens
 * Date: 07.12.2017
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

@Entity(
        tableName = "Events",
        foreignKeys = [
            ForeignKey(
                    entity = Person::class,
                    parentColumns = ["id"],
                    childColumns = ["personId"],
                    onDelete = ForeignKey.CASCADE
            )
        ],
        indices = [
            Index("id"),
            Index("personId"),
            Index("providerId")
        ]
)
data class Event(
        var type: Type = Type.Birthday,
        var label: String? = null,
        var date: Date = Calendar.getInstance().time,
        var isYearKnown: Boolean = true
) {
    enum class Type {
        Custom,
        Anniversary,
        Other,
        Birthday
    }

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    var personId: Long = 0

    var dataVersion: Long = -1
    var providerId: Long = -1

    var deleted: Boolean = false

    @Ignore
    var person: Person? = null
}