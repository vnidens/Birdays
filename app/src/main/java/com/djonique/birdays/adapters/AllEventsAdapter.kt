package com.djonique.birdays.adapters

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.preference.PreferenceManager
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.djonique.birdays.R
import com.djonique.birdays.model.Event
import com.djonique.birdays.model.Person
import com.djonique.birdays.utils.Constants
import com.djonique.birdays.utils.Utils
import com.djonique.birdays.utils.day
import com.djonique.birdays.utils.month
import java.util.*

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

class AllEventsAdapter(context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = mutableListOf<Item>()
    private val itemsComparator = ItemsComparator()

    private var displayedAge = "0"

    init {
        context.resources.getStringArray(R.array.months).forEachIndexed { index, s ->
            items.add(MonthItem(index, s))
        }

        displayedAge = PreferenceManager.getDefaultSharedPreferences(context)
                .getString(Constants.DISPLAYED_AGE_KEY, "0")
    }

    var onItemClickListener: OnItemClickListener? = null
    var onItemLongClickListener: OnItemLongClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            0 -> EventViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.event_list_item, parent, false))
            else -> MonthViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.model_separator, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        items[position].let {
            when(it) {
                is EventItem -> (holder as? EventViewHolder)?.bind(it)
                is MonthItem -> (holder as? MonthViewHolder)?.bind(it)
            }
        }
    }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int {
        return when(items[position]) {
            is EventItem -> 0
            is MonthItem -> 1
        }
    }

    override fun getItemId(position: Int): Long {
        return when(items[position]) {
            is EventItem -> 0
            is MonthItem -> 1
        }
    }

    fun add(person: Person) {
        person.events.map { EventItem(it, person) }
                .forEach { eventItem ->
                    val index = items.indexOfLast { itemsComparator.compare(it, eventItem) == -1 }

                    if(index != -1) {
                        items.add(index + 1, eventItem)
                        notifyItemInserted(index + 1)
                    } else {
                        items.add(eventItem)
                        notifyItemInserted(items.size - 1)
                    }
                }
    }

    fun add(persons: Collection<Person>) {
        persons.forEach(this::add)
    }

    inner class EventViewHolder(itemView: View)
        : RecyclerView.ViewHolder(itemView),
            View.OnClickListener,
            View.OnLongClickListener {

        @BindView(R.id.tvAge)
        lateinit var tvAge: TextView
        @BindView(R.id.tvName)
        lateinit var tvName: TextView
        @BindView(R.id.tvEventName)
        lateinit var tvEventName: TextView
        @BindView(R.id.tvEventDate)
        lateinit var tvDate: TextView

        private lateinit var item: EventItem

        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
            ButterKnife.bind(this, itemView)
        }

        fun bind(item: EventItem) {
            val context = itemView.context
            this.item = item

            tvName.text = item.person.name

            if(item.event.isYearKnown) {
                val age = if (displayedAge == "0") Utils.getCurrentAge(item.event.date.time) else Utils.getFutureAge(item.event.date.time)
                val ageCircle = tvAge.background as GradientDrawable
                ageCircle.setColor(ContextCompat.getColor(context, getAgeCircleColor(age)))

                tvAge.visibility = View.VISIBLE
                tvAge.text = age.toString()
            } else {
                tvAge.visibility = View.INVISIBLE
            }

            tvEventName.text = context.getString(R.string.celebrates_description).format(
                    when (item.event.type) {
                        Event.Type.Birthday -> context.getString(R.string.birthday)
                        Event.Type.Anniversary -> context.getString(R.string.anniversary)
                        Event.Type.Other -> context.getString(R.string.event)
                        Event.Type.Custom -> item.event.label
                    }
            )

            tvDate.text = if (item.event.isYearKnown) {
                Utils.getDate(item.event.date.time)
            } else {
                Utils.getDateWithoutYear(item.event.date.time)
            }
        }

        override fun onClick(v: View?) {
            onItemClickListener?.onItemClick(item.event, item.person)
        }

        override fun onLongClick(v: View?): Boolean {
            return when(onItemLongClickListener) {
                null -> false
                else -> {
                    onItemLongClickListener?.onItemLongClick(item.event, item.person)
                    true
                }
            }
        }

        private fun getAgeCircleColor(age: Int): Int {
            return when (age) {
                in 0..10 -> R.color.age1
                in 10..20 -> R.color.age2
                in 20..30 -> R.color.age3
                in 30..40 -> R.color.age4
                in 40..50 -> R.color.age5
                in 50..60 -> R.color.age6
                in 60..70 -> R.color.age7
                else -> R.color.age8
            }
        }
    }

    class MonthViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @BindView(R.id.textview_separator)
        lateinit var tvMonthName: TextView

        private lateinit var item: MonthItem

        init {
            itemView.isEnabled = false
            ButterKnife.bind(this, itemView)
        }

        fun bind(item: MonthItem) {
            this.item = item
            tvMonthName.text = item.name
        }

    }

    interface OnItemClickListener {
        fun onItemClick(event: Event, person: Person)
    }

    interface OnItemLongClickListener {
        fun onItemLongClick(event: Event, person: Person)
    }

}

sealed class Item {
}

data class EventItem(val event: Event, val person: Person) : Item() {
}

data class MonthItem(val id: Int, val name: String) : Item() {
}

internal class ItemsComparator : Comparator<Item> {
    override fun compare(left: Item?, right: Item?): Int {
        if(left == null)
            return -1

        if(right == null)
            return 1

        if(left is MonthItem && right is MonthItem) {
            return if(left.id < right.id) -1 else 1
        }

        val leftDay = when(left) {
            is EventItem -> left.event.date.day()
            is MonthItem -> 0
        }

        val leftMonth = when(left) {
            is EventItem -> left.event.date.month()
            is MonthItem -> left.id
        }

        val rightDay = when(right) {
            is EventItem -> right.event.date.day()
            is MonthItem -> 0
        }

        val rightMonth = when(right) {
            is EventItem -> right.event.date.month()
            is MonthItem -> right.id
        }

        return when {
            leftMonth < rightMonth -> -1
            leftMonth > rightMonth -> 1
            else -> {
                when {
                    leftDay < rightDay -> -1
                    leftDay > rightDay -> 1
                    else -> 0
                }
            }
        }

    }

}
