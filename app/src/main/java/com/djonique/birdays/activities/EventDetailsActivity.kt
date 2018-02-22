package com.djonique.birdays.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
import com.djonique.birdays.R
import com.djonique.birdays.model.Event
import com.djonique.birdays.utils.*

/**
 * Birdays
 *
 * Created by Victor Nidens
 * Date: 24.01.2018
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
class EventDetailsActivity : AppCompatActivity() {

    @BindView(R.id.toolbar)
    lateinit var toolbar: Toolbar
    @BindView(R.id.tvName)
    lateinit var tvName: TextView
    @BindView(R.id.textview_detail_age)
    lateinit var tvAge: TextView
    @BindView(R.id.textview_detail_date)
    lateinit var tvDate: TextView
    @BindView(R.id.tvDaysTo)
    lateinit var tvDaysTo: TextView
    @BindView(R.id.tvDaysToDescription)
    lateinit var tvDaysToDescription: TextView

    @BindView(R.id.lt_days_since_birth)
    lateinit var ltDaysSinceBirth: View
    @BindView(R.id.tvDaysSinceBirth)
    lateinit var tvDaysSinceBirth: TextView
    @BindView(R.id.ltZodiacSign)
    lateinit var ltZodiacSign: View
    @BindView(R.id.imgZodiacSign)
    lateinit var ivZodiacSign: ImageView
    @BindView(R.id.tvZodiacSign)
    lateinit var tvZodiacSign: TextView

    @BindView(R.id.ltPhone)
    lateinit var ltPhone: View
    @BindView(R.id.tvPhone)
    lateinit var tvPhone: TextView

    @BindView(R.id.ltEmail)
    lateinit var ltEmail: View
    @BindView(R.id.tvEmail)
    lateinit var tvEmail: TextView

    private var unbinder: Unbinder? = null

    private lateinit var event: Event

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(intent.extras?.containsKey(EXTRA_EVENT) != true) {
            finish()
            return
        }

        setContentView(R.layout.activity_event_details)
        unbinder = ButterKnife.bind(this)

        setSupportActionBar(toolbar)

        event = intent.extras.getParcelable<Event>(EXTRA_EVENT)

        val eventLabel = when(event.type) {
            Event.Type.Birthday -> getString(R.string.birthday)
            Event.Type.Anniversary -> getString(R.string.anniversary)
            Event.Type.Other -> getString(R.string.other)
            else -> event.label!!
        }

        supportActionBar?.title = eventLabel

        tvName.text = event.person?.name

        if(event.isYearKnown) {
            tvAge.text = Utils.getCurrentAge(event.date.time).toString()
        } else {
            tvAge.text = ""
        }

        tvDate.text = if (event.isYearKnown) {
            Utils.getDate(event.date.time)
        } else {
            Utils.getDateWithoutYear(event.date.time)
        }

        val daysTo = event.date.nextEvent().daysTo()
        tvDaysTo.text = daysTo.toString()
        tvDaysToDescription.text = getString(
                when(event.type) {
                    Event.Type.Birthday -> R.string.days_to_next_birthday
                    else -> R.string.days_to_next_anniversary
                },
                getQuantityString(R.plurals.days, daysTo)
        )

        if(event.type == Event.Type.Birthday) {
            if(event.isYearKnown) {
                ltDaysSinceBirth.show()
                tvDaysSinceBirth.text = Utils.daysSinceBirthday(event.date.time)
            } else {
                ltDaysSinceBirth.hide()
            }

            ltZodiacSign.show()
            Utils.getZodiacId(event.date.time).let { zodiacRes ->
                tvZodiacSign.setText(zodiacRes)
                ivZodiacSign.setImageResource(Utils.getZodiacImage(zodiacRes))
            }

        } else {
            ltDaysSinceBirth.hide()
            ltZodiacSign.hide()
        }

        val phone = event.person?.phone
        if(phone != null) {
            ltPhone.show()
            tvPhone.text = phone
        } else {
            ltPhone.hide()
        }

        val email = event.person?.email
        if(email != null) {
            ltEmail.show()
            tvEmail.text = email
        } else {
            ltEmail.hide()
        }
    }

    override fun onDestroy() {
        unbinder?.unbind()
        super.onDestroy()
    }

    @OnClick(
            R.id.ltPhone,
            R.id.btnSendMessage,
            R.id.ltEmail
            )
    fun onClick(v: View) {
        when(v.id) {
            R.id.ltPhone -> {}
            R.id.btnSendMessage -> {}
            R.id.ltEmail -> {}
        }
    }

    companion object {
        const val EXTRA_EVENT = "event"
    }
}