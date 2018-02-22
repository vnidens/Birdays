package com.djonique.birdays.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context
import com.djonique.birdays.model.Event
import com.djonique.birdays.model.Person
import com.djonique.birdays.utils.daysTo
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
@Database(
        entities = [
            Person::class,
            Event::class
        ],
        version = 1
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    companion object {

        private var INSTANCE: AppDatabase? = null

        fun instance(context: Context): AppDatabase {
            if(INSTANCE == null) {
                synchronized(this) {
                    if(INSTANCE == null) {
                            INSTANCE = Room.databaseBuilder(context, AppDatabase::class.java, "app_db")
                                    .build()
                    }
                }
            }
            return INSTANCE!!
        }

    }

    abstract val personsDao: PersonsDao
    abstract val eventsDao: EventsDao

    fun getPersons(): Flowable<List<Person>> {
        return personsDao.getPersons()
                .doOnNext {
                    it.forEach { person ->
                        eventsDao.getEventsForPersonId(person.id)
                                .onEach { it.person = person }
                                .forEach { person.events.add(it) }
                    }
                }
    }

    fun getUpcomingEvents(): Flowable<List<Event>> {
        return eventsDao.getEvents()
                .flatMap {
                    Flowable.just(
                            it.filter { it.date.daysTo() <= 31 }
                                    .toList()
                    )
                }
                .doOnNext { events ->
                    events.forEach { event ->
                        event.person = personsDao.getPersonById(event.personId)
                    }
                }
    }

    fun insert(person: Person) {
        person.id = personsDao.insert(person)

        person.events.forEach {
            it.personId = person.id
            it.id = eventsDao.insert(it)
        }
    }

}