package com.djonique.birdays.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.djonique.birdays.R
import com.djonique.birdays.adapters.UpcomingEventsAdapter
import com.djonique.birdays.database.AppDatabase
import com.djonique.birdays.utils.disposeIfNot
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by Victor Nidens
 *
 * The MIT License (MIT)
 *
 * Copyright (c) 2018 Victor Nidens
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
class UpcomingEventsFragment : Fragment() {

    private val adapter = UpcomingEventsAdapter()

    private var loadUpcomingEventsSubscription: Disposable? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_recycler_view, container, false)

        val recyclerView = v.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = adapter

        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        loadUpcomingEvents()
    }

    override fun onStart() {
        super.onStart()

        if(adapter.itemCount == 0) {
            loadUpcomingEvents()
        }
    }

    override fun onStop() {
        super.onStop()
        loadUpcomingEventsSubscription.disposeIfNot()
    }

    fun loadUpcomingEvents() {
        loadUpcomingEventsSubscription.disposeIfNot()
        loadUpcomingEventsSubscription = context!!.let {
            AppDatabase.instance(it)
                    .getUpcomingEvents()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            { events ->
                                adapter.add(events)
                            },
                            {
                                t -> t.printStackTrace()
                            }
                    )
        }
    }
}