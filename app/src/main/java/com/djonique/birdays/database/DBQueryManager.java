package com.djonique.birdays.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.djonique.birdays.model.Person;
import com.djonique.birdays.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class DBQueryManager {

    private SQLiteDatabase database;
    private Person person;
    private List<Person> persons;
    private String name, phoneNumber, email, lowerCaseName;
    private long date, timeStamp;

    DBQueryManager(SQLiteDatabase database) {
        this.database = database;
    }

    public Person getPerson(long timeStamp) {

        Cursor cursor = database.query(DBHelper.DB_PERSONS, null, DBHelper.SELECTION_TIME_STAMP,
                new String[]{Long.toString(timeStamp)}, null, null, null);

        if (cursor.moveToFirst()) {
            name = getName(cursor);
            date = getDate(cursor);
            phoneNumber = getPhoneNumber(cursor);
            email = getEmail(cursor);
            lowerCaseName = getLowerCaseName(cursor);

            person = new Person(name, date, phoneNumber, email, timeStamp, lowerCaseName);
        }
        cursor.close();

        return person;
    }

    public List<Person> getPersons() {
        persons = new ArrayList<>();

        Cursor cursor = database.query(DBHelper.DB_PERSONS, null, null, null, null,
                null, null);

        if (cursor.moveToFirst()) {
            do {
                name = getName(cursor);
                date = getDate(cursor);
                phoneNumber = getPhoneNumber(cursor);
                email = getEmail(cursor);
                timeStamp = getTimeStamp(cursor);
                lowerCaseName = getLowerCaseName(cursor);

                person = new Person(name, date, phoneNumber, email, timeStamp, lowerCaseName);
                persons.add(person);

            } while (cursor.moveToNext());
        }
        cursor.close();

        return persons;
    }

    public List<Person> getSearchPerson(String selection, String[] selectionArgs, String orderBy) {
        persons = new ArrayList<>();

        Cursor cursor = database.query(DBHelper.DB_PERSONS, null, selection, selectionArgs, null,
                null, orderBy);

        if (cursor.moveToFirst()) {
            do {
                name = getName(cursor);
                date = getDate(cursor);
                phoneNumber = getPhoneNumber(cursor);
                email = getEmail(cursor);
                timeStamp = getTimeStamp(cursor);
                lowerCaseName = getLowerCaseName(cursor);

                person = new Person(name, date, phoneNumber, email, timeStamp, lowerCaseName);
                persons.add(person);

            } while (cursor.moveToNext());
        }
        cursor.close();

        return persons;
    }

    public List<Person> getThisMonthPersons() {
        persons = new ArrayList<>();

        Cursor cursor = database.query(DBHelper.DB_PERSONS, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                name = getName(cursor);
                date = getDate(cursor);
                phoneNumber = getPhoneNumber(cursor);
                email = getEmail(cursor);
                timeStamp = getTimeStamp(cursor);
                lowerCaseName = getLowerCaseName(cursor);

                if (Utils.isCurrentMonth(date)) {
                    person = new Person(name, date, phoneNumber, email, timeStamp, lowerCaseName);
                    persons.add(person);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();

        return persons;
    }

    public List<Person> getSearchMonthPerson(String selection, String[] selectionArgs,
                                             String orderBy) {
        persons = new ArrayList<>();

        Cursor cursor = database.query(DBHelper.DB_PERSONS, null, selection, selectionArgs, null,
                null, orderBy);

        if (cursor.moveToFirst()) {
            do {
                name = getName(cursor);
                date = getDate(cursor);
                phoneNumber = getPhoneNumber(cursor);
                email = getEmail(cursor);
                timeStamp = getTimeStamp(cursor);
                lowerCaseName = getLowerCaseName(cursor);

                if (Utils.isCurrentMonth(date)) {
                    person = new Person(name, date, phoneNumber, email, timeStamp, lowerCaseName);
                    persons.add(person);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();

        return persons;
    }

    public List<Person> getFamousPerson() {
        persons = new ArrayList<>();

        Cursor cursor = database.query(DBHelper.DB_FAMOUS, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                name = getName(cursor);
                date = getDate(cursor);

                if (Utils.isCurrentMonth(date)) {
                    person = new Person(name, date);
                    persons.add(person);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();

        return persons;
    }

    private String getName(Cursor cursor) {
        return cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_NAME));
    }

    private long getDate(Cursor cursor) {
        return cursor.getLong(cursor.getColumnIndex(DBHelper.COLUMN_DATE));
    }

    private String getPhoneNumber(Cursor cursor) {
        return cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_PHONE_NUMBER));
    }

    private String getEmail(Cursor cursor) {
        return cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_EMAIL));
    }

    private long getTimeStamp(Cursor cursor) {
        return cursor.getLong(cursor.getColumnIndex(DBHelper.COLUMN_TIME_STAMP));
    }

    private String getLowerCaseName(Cursor cursor) {
        return cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_LOWER_CASE_NAME));
    }
}
