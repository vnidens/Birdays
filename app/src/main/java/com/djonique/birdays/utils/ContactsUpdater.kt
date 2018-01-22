package com.djonique.birdays.utils

import com.djonique.birdays.database.AppDatabase
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Birdays
 *
 * Created by Victor Nidens
 * Date: 20.12.2017
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
class ContactsUpdater
@Inject
constructor(
        private val contactsProvider: ContactsProvider,
        private val appDb: AppDatabase
) {

    fun updateContacts(): Completable {
        return contactsProvider.systemContacts()
                .doOnNext { contacts ->
                    contacts.forEach { contact ->
                        val person = appDb.personsDao.let {
                            it.getPersonByProviderId(contact.providerId) ?: it.getPersonByName(contact.name)
                        }

                        if (person == null) {
                            appDb.insert(contact)
                        } else {
                            person.events.filter { it !in contact.events }.forEach { it.deleted = true }
                            contact.events.filter { it !in person.events }.forEach { person.events.add(it) }

                            appDb.eventsDao.update(person.events)
                        }
                    }
                }
                .flatMapCompletable { Completable.complete() }
                .subscribeOn(Schedulers.io())
    }

}