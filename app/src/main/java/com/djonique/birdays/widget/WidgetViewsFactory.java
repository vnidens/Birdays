/*
 * Copyright 2017 Evgeny Timofeev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.djonique.birdays.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.djonique.birdays.R;
import com.djonique.birdays.database.DbHelper;
import com.djonique.birdays.models.Person;
import com.djonique.birdays.utils.Constants;
import com.djonique.birdays.utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class WidgetViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context context;
    private DbHelper dbHelper;
    private List<Person> widgetList;

    WidgetViewsFactory(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        dbHelper = new DbHelper(context);
    }

    @Override
    public void onDataSetChanged() {
        List<Person> persons = dbHelper.query().getPersons();
        widgetList = new ArrayList<>();
        Collections.sort(persons);

        Calendar today = Calendar.getInstance();
        int month = today.get(Calendar.MONTH);
        int day = today.get(Calendar.DAY_OF_MONTH);

        int position = 0;

        // Finds the position of the Person with closest date
        for (int i = 0; i < persons.size(); i++) {
            Person person = persons.get(i);
            if (person.getMonth() == month && person.getDay() >= day || person.getMonth() > month) {
                position = i;
                break;
            }
        }

        // Shifts list
        for (int i = position; i < persons.size(); i++) {
            widgetList.add(persons.get(i));
        }
        for (int i = 0; i < position; i++) {
            widgetList.add(persons.get(i));
        }
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public int getCount() {
        return widgetList.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.description_widget_list_view);

        String name = widgetList.get(i).getName();
        long date = widgetList.get(i).getDate();
        boolean yearUnknown = widgetList.get(i).isYearUnknown();

        view.setTextViewText(R.id.textview_widget_name, name);
        view.setTextViewText(R.id.textview_widget_date,
                Utils.getDateWithoutYear(date));
        if (!yearUnknown) {
            view.setTextViewText(R.id.textview_widget_age, String.valueOf(Utils.getCurrentAge(date)));
        } else {
            view.setTextViewText(R.id.textview_widget_age, "");
        }

        Intent clickIntent = new Intent();
        clickIntent.putExtra(Constants.TIME_STAMP, widgetList.get(i).getTimeStamp());
        view.setOnClickFillInIntent(R.id.relativelayout_widget, clickIntent);

        return view;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}