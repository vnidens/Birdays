package com.djonique.birdays.utils

import android.content.ContentResolver
import android.provider.ContactsContract
import com.djonique.birdays.model.Event
import com.djonique.birdays.model.Person
import io.reactivex.Observable
import java.util.*
import javax.inject.Inject

/**
 * Birdays
 *
 * Created by Victor Nidens
 * Date: 09.12.2017
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

class ContactsProvider
@Inject
constructor(private val contentResolver: ContentResolver) {
    fun systemContacts(): Observable<List<Person>> {
        return Observable.fromCallable {
            val uri = ContactsContract.Data.CONTENT_URI
            val condition = "${ContactsContract.Data.MIMETYPE} = ?"

            val cursor = contentResolver.query(uri,
                    null,
                    condition,
                    arrayOf(ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE),
                    ContactsContract.CommonDataKinds.Event.CONTACT_ID + " ASC")

            val persons = mutableListOf<Person>()

            while(cursor.moveToNext()) {
                val contactId = cursor.getLong(cursor.getColumnIndex(ContactsContract.Data.CONTACT_ID))

                var person = persons.firstOrNull {
                    it.providerId == contactId
                }

                if(person != null) continue

                person = Person().apply {
                    providerId = cursor.getLong(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.CONTACT_ID))
                    name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                }

                person.email = getContactPrimaryEmail(contactId)
                person.phone = getContactPrimaryPhone(contactId)

                person.events.addAll(loadContactEvents(contactId))

                persons.add(person)
            }

            cursor.close()

            persons
        }
    }

    private fun loadContactEvents(contactId: Long): List<Event> {

        val eventTypeFromInt = { type: Int ->
            when(type) {
                ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY -> Event.Type.Birthday
                ContactsContract.CommonDataKinds.Event.TYPE_ANNIVERSARY -> Event.Type.Anniversary
                ContactsContract.CommonDataKinds.Event.TYPE_CUSTOM -> Event.Type.Custom
                else -> Event.Type.Other
            }
        }

        val uri = ContactsContract.Data.CONTENT_URI
        val condition = "${ContactsContract.Data.CONTACT_ID} = ? AND ${ContactsContract.Data.MIMETYPE} = ?"

        val cursor = contentResolver.query(uri,
                null,
                condition,
                arrayOf(contactId.toString(), ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE),
                null)

        val events = mutableListOf<Event>()

        while (cursor.moveToNext()) {
            val event = Event(
                    type = eventTypeFromInt(cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.TYPE)))
            ).apply {
                val eventDate = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.START_DATE))
                date = eventDate.toDate()

                isYearKnown = eventDate.isYearKnown

                if(type == Event.Type.Custom) {
                    label = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.LABEL))
                }

                personId = contactId

                dataVersion = cursor.getLong(cursor.getColumnIndex(ContactsContract.Data.DATA_VERSION))

            }

            events.add(event)
        }

        cursor.close()

        return events
    }

    private fun getContactPrimaryEmail(contactId: Long): String? {
        val uri = ContactsContract.Data.CONTENT_URI
        val condition = "${ContactsContract.Data.CONTACT_ID} = ? AND ${ContactsContract.Data.MIMETYPE} = ?"

        var email: String? = null
        val cursor = contentResolver.query(uri,
                null,
                condition,
                arrayOf(contactId.toString(), ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE),
                null)

        while(cursor.moveToNext()) {
            email = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS))
            val primary = cursor.getInt(cursor.getColumnIndex(ContactsContract.Data.IS_PRIMARY))

            if(primary > 0)
                break
        }

        cursor.close()

        return email
    }

    private fun getContactPrimaryPhone(contactId: Long): String? {
        val uri = ContactsContract.Data.CONTENT_URI
        val condition = "${ContactsContract.Data.CONTACT_ID} = ? AND ${ContactsContract.Data.MIMETYPE} = ?"

        var phone: String? = null
        val cursor = contentResolver.query(uri,
                null,
                condition,
                arrayOf(contactId.toString(), ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE),
                null)

        while(cursor.moveToNext()) {
            phone = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
            val primary = cursor.getInt(cursor.getColumnIndex(ContactsContract.Data.IS_PRIMARY))

            if(primary > 0)
                break
        }

        cursor.close()

        return phone
    }

}

private val String.isYearKnown: Boolean
    get() = split('-')[0].isNotBlank()

private fun String.toDate(): Date {
    val cal = Calendar.getInstance()
    val dateParts = split('-')
    var monthIndex = 1
    if(isYearKnown) {
        cal.set(Calendar.YEAR, dateParts[0].toInt())
    } else {
        monthIndex++
    }

    cal.set(Calendar.MONTH, dateParts[monthIndex].toInt() - 1)
    cal.set(Calendar.DAY_OF_MONTH, dateParts[monthIndex + 1].toInt())

    return cal.time
}