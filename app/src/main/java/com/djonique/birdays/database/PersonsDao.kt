package com.djonique.birdays.database

import android.arch.persistence.room.*
import com.djonique.birdays.model.Person
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
interface PersonsDao {
    @Insert
    fun insert(person: Person): Long

    @Insert
    fun insert(persons: Collection<Person>): List<Long>

    @Update
    fun update(person: Person): Int

    @Update
    fun update(persons: Collection<Person>): Int

    @Delete
    fun delete(person: Person): Int

    @Delete
    fun delete(persons: Collection<Person>): Int

    @Query("SELECT * FROM Persons")
    fun getPersons(): Flowable<List<Person>>

    @Query("SELECT * from Persons WHERE id=:arg0")
    fun getPersonById(arg0: Long): Person?

    @Query("SELECT * from Persons WHERE providerId=:arg0")
    fun getPersonByProviderId(arg0: Long): Person?

    @Query("SELECT * from Persons WHERE name=:arg0")
    fun getPersonByName(arg0: String): Person?
}