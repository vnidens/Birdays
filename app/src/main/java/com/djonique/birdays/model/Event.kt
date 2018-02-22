package com.djonique.birdays.model

import android.arch.persistence.room.*
import android.os.Parcel
import android.os.Parcelable
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
) : Parcelable {
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

    constructor(parcel: Parcel) : this(
            Type.valueOf(parcel.readString()),
            parcel.readString(),
            Date().apply { time = parcel.readLong() },
            parcel.readByte() != 0.toByte()) {
        id = parcel.readLong()
        personId = parcel.readLong()
        dataVersion = parcel.readLong()
        providerId = parcel.readLong()
        deleted = parcel.readByte() != 0.toByte()
        person = parcel.readParcelable(Person::class.java.classLoader)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(type.name)
        parcel.writeString(label)
        parcel.writeLong(date.time)
        parcel.writeByte(if (isYearKnown) 1 else 0)
        parcel.writeLong(id)
        parcel.writeLong(personId)
        parcel.writeLong(dataVersion)
        parcel.writeLong(providerId)
        parcel.writeByte(if (deleted) 1 else 0)
        parcel.writeParcelable(person, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Event> {
        override fun createFromParcel(parcel: Parcel): Event {
            return Event(parcel)
        }

        override fun newArray(size: Int): Array<Event?> {
            return arrayOfNulls(size)
        }
    }
}