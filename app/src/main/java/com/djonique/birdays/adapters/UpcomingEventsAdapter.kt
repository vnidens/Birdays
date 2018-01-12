package com.djonique.birdays.adapters

import android.content.Intent
import android.net.Uri
import android.support.constraint.Group
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.djonique.birdays.R
import com.djonique.birdays.model.Event
import com.djonique.birdays.utils.daysTo
import com.djonique.birdays.utils.hide
import com.djonique.birdays.utils.show

/**
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
class UpcomingEventsAdapter
    : RecyclerView.Adapter<UpcomingEventsAdapter.ViewHolder>(),
        EventsAdapter {

    private val items = mutableListOf<Event>()

    private var _eventSelectedListener: EventsAdapter.OnEventSelectedListener? = null
    override var onEventSelectedListener: EventsAdapter.OnEventSelectedListener?
        get() = _eventSelectedListener
        set(value) { _eventSelectedListener = value }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.upcoming_event_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.bind(items[position])
    }

    fun add(event: Event) {
//        items.add(event)
        val insertIndex = insertSorted(event)
        notifyItemInserted(insertIndex)
    }

    fun add(events: Collection<Event>) {
        events.forEach(this::add)
    }

    private fun insertSorted(event: Event): Int {
        val daysTo = event.date.daysTo()
        val index = items.indexOfFirst { it.date.daysTo() > daysTo }.let {
            if(it != -1) it else items.size
        }

        items.add(index, event)

        return index
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @BindView(R.id.tvName)
        lateinit var tvName: TextView
        @BindView(R.id.tvEventName)
        lateinit var tvEventName: TextView
        @BindView(R.id.tvEventDate)
        lateinit var tvEventDate: TextView
        @BindView(R.id.groupPhone)
        lateinit var groupPhone: Group
        @BindView(R.id.groupEmail)
        lateinit var groupEmail: Group

        init {
            ButterKnife.bind(this, itemView)
        }

        private lateinit var event: Event

        internal fun bind(item: Event) {
            this.event = item
            val context = itemView.context

            tvName.text = event.person?.name

            tvEventName.text = event.type.let {
                val eventDescription = when (it) {
                    Event.Type.Birthday, Event.Type.Anniversary -> context.getString(R.string.celebrates_event_name)
                    else -> context.getString(R.string.marks_event_name)
                }
                eventDescription.format(
                        when (event.type) {
                            Event.Type.Birthday -> context.getString(R.string.birthday)
                            Event.Type.Anniversary -> context.getString(R.string.anniversary)
                            Event.Type.Other -> context.getString(R.string.event)
                            Event.Type.Custom -> event.label
                        }
                )
            }

            val daysTo = event.date.daysTo()

            tvEventDate.text = when (daysTo) {
                0 -> context.getString(R.string.today)
                else -> context.resources.getQuantityString(R.plurals.in_days, daysTo, daysTo)
            }

//            val backgroundColor = when (daysTo) {
//                0 -> context.getColorCompat(R.color.cardview_birthday)
//                else -> context.getColorCompat(R.color.cardview_background)
//            }

//            itemView.setBackgroundColor()

            if(event.person?.phone != null) {
                groupPhone.show()
            } else {
                groupPhone.hide()
            }

            if(event.person?.email != null) {
                groupEmail.show()
            } else {
                groupEmail.hide()
            }

        }

        @OnClick(R.id.btnCall, R.id.btnMessage, R.id.btnEmail)
        fun onActionClicked(v: View) {
            val intent = when(v.id) {
                R.id.btnCall -> {
                    Intent.createChooser(
                            Intent(Intent.ACTION_DIAL, Uri.parse("tel:${event.person?.phone}")),
                            null
                    )
                }

                R.id.btnMessage -> {
                    Intent.createChooser(
                            Intent(Intent.ACTION_SEND, Uri.parse("smsto:${event.person?.phone}")).apply {
                                type = "text/plain"
                                putExtra("address", event.person?.phone)
                            },

                            null
                    )
                }

                R.id.btnEmail -> {
                    Intent.createChooser(
                            Intent(Intent.ACTION_SENDTO).apply {
                                putExtra(Intent.EXTRA_EMAIL, arrayOf(event.person?.email))
                                //todo: put Intent.EXTRA_SUBJECT?
                                data = Uri.parse("mailto:")
                            },
                            null
                    )
                }

                else -> null
            }

            intent?.let { v.context.startActivity(it) }
        }
    }

}