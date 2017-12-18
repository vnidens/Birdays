package com.djonique.birdays.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import com.djonique.birdays.R
import com.djonique.birdays.adapters.AllEventsAdapter
import com.djonique.birdays.database.AppDatabase
import com.djonique.birdays.model.Event
import com.djonique.birdays.model.Person
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * Birdays
 *
 * Created by Victor Nidens
 * Date: 17.12.2017
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

class AllEventsFragment
    : Fragment(),
        AllEventsAdapter.OnItemClickListener,
        AllEventsAdapter.OnItemLongClickListener{

    @BindView(R.id.recyclerView)
    lateinit var recyclerView: RecyclerView

    private var unbinder: Unbinder? = null

    private lateinit var adapter: AllEventsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_recycler_view, container, false)

        unbinder = ButterKnife.bind(this, v)

        adapter = AllEventsAdapter(inflater.context)

        recyclerView.layoutManager = LinearLayoutManager(inflater.context)
        recyclerView.adapter = adapter

        return v
    }

    override fun onStart() {
        super.onStart()

        context?.let {
            AppDatabase.getInstance(it)
                    .getPersons()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            { persons ->
                                adapter.add(persons)
                            },
                            { t -> t.printStackTrace() }
                    )
        }
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroyView() {
        unbinder?.unbind()
        super.onDestroyView()
    }

    override fun onItemClick(event: Event, person: Person) {
        //TODO: start activity with event information
    }

    override fun onItemLongClick(event: Event, person: Person) {
        //TODO: ask to delete event or person
    }
}