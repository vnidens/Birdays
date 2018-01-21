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

package com.djonique.birdays.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.djonique.birdays.R;
import com.djonique.birdays.fragments.AllEventsFragment;
import com.djonique.birdays.fragments.UpcomingEventsFragment;
import com.djonique.birdays.models.Person;

import java.util.ArrayList;
import java.util.List;

public class PagerAdapter extends FragmentPagerAdapter {

    private AllEventsFragment allEventsFragment;
    private UpcomingEventsFragment upcomingEventsFragment;
    private List<String> tabsTitles = new ArrayList<>(2);
    private List<Fragment> fragments = new ArrayList<>(2);

    public PagerAdapter(FragmentManager fm, Context context) {
        super(fm);

        tabsTitles.add(context.getString(R.string.all));
        tabsTitles.add(context.getString(R.string.upcoming));

        fragments.add(new AllEventsFragment());
        fragments.add(new UpcomingEventsFragment());
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return tabsTitles.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabsTitles.get(position);
    }

    @NonNull
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment createdFragment = (Fragment) super.instantiateItem(container, position);

        if(createdFragment instanceof AllEventsFragment) {
            allEventsFragment = ((AllEventsFragment)createdFragment);
        } else if(createdFragment instanceof UpcomingEventsFragment) {
            upcomingEventsFragment = (UpcomingEventsFragment)createdFragment;
        }

        return createdFragment;
    }

    public void search(String text) {
    }

    public void addPersonsFromDb() {
    }

    public void addPerson(Person person) {
    }

    public void deletePerson(long timeStamp) {
    }
}