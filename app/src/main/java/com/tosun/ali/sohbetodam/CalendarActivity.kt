package com.tosun.ali.sohbetodam

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_calendar.*

class CalendarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)


        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->

            var date = dayOfMonth.toString() + "/" + (month).toString() + "/" + year.toString()

            var intent = Intent(this,DersDetaylariOdevActivity::class.java)
            intent.putExtra("date",date)
            startActivity(intent)
            //deneme
        }

    }
}
