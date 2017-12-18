package com.djonique.birdays.database

import android.arch.persistence.room.*
import com.djonique.birdays.model.Event
import io.reactivex.Flowable

/**
 * Birdays
 *
 * Created by Victor Nidens
 * Date: 08.12.2017
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

@Dao
interface EventsDao {
    @Insert
    fun insert(event: Event): Long

    @Insert
    fun insert(events: Collection<Event>): List<Long>

    @Update
    fun update(event: Event): Int

    @Update
    fun update(events: Collection<Event>): Int

    @Delete
    fun delete(event: Event): Int

    @Delete
    fun delete(events: Collection<Event>): Int

    @Query("SELECT * FROM Events WHERE personId=:arg0")
    fun getEventsForPersonId(arg0: Long): List<Event>

    @Query("SELECT * FROM Events WHERE personId=:arg0")
    fun getEventsForPersonIdObservable(arg0: Long): Flowable<List<Event>>

//    @Query("SELECT * FROM Events")
//    fun getEvents(): Flowable<List<Event>>
//
//    @Query("SELECT * from Persons WHERE providerId=:id")
//    fun getPersonByProviderId(id: Long): Maybe<Event>
}