package com.djonique.birdays.fragments

import android.content.Intent
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
import com.djonique.birdays.activities.EventDetailsActivity
import com.djonique.birdays.adapters.AllEventsAdapter
import com.djonique.birdays.adapters.EventsAdapter
import com.djonique.birdays.database.AppDatabase
import com.djonique.birdays.model.Event
import com.djonique.birdays.model.Person
import com.djonique.birdays.utils.disposeIfNot
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

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
        EventsAdapter.OnEventSelectedListener,
        AllEventsAdapter.OnItemLongClickListener {

    @BindView(R.id.recyclerView)
    lateinit var recyclerView: RecyclerView

    private var unbinder: Unbinder? = null

    private lateinit var adapter: AllEventsAdapter

    private var loadPeopleSubscription: Disposable? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_recycler_view, container, false)

        unbinder = ButterKnife.bind(this, v)

        adapter = AllEventsAdapter(inflater.context)
        adapter.onEventSelectedListener = this
        adapter.onItemLongClickListener = this

        recyclerView.layoutManager = LinearLayoutManager(inflater.context)
        recyclerView.adapter = adapter

        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        loadPersons()
    }

    override fun onStart() {
        super.onStart()

        if(adapter.itemCount == 0) {
            loadPersons()
        }
    }

    override fun onStop() {
        super.onStop()

        loadPeopleSubscription.disposeIfNot()
    }

    override fun onDestroyView() {
        unbinder?.unbind()
        adapter.onItemLongClickListener = null
        adapter.onEventSelectedListener = null
        super.onDestroyView()
    }

    override fun onEventSelected(event: Event, person: Person) {
        val intent = Intent(context, EventDetailsActivity::class.java)
        intent.putExtra(EventDetailsActivity.EXTRA_EVENT, event)
        startActivity(intent)
    }

    override fun onItemLongClick(event: Event, person: Person) {
        //TODO: ask to delete event or person
    }

    private fun loadPersons() {
        loadPeopleSubscription.disposeIfNot()

        loadPeopleSubscription = context!!.let {
            AppDatabase.instance(it)
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
}