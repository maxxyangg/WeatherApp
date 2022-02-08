package com.example.weatherapp

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ForecastActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView


    val adapterData = listOf<DayForecast>(
        DayForecast(1643749080, 1643724051, 1643770851, ForcastTemp(30.0f,5.0f,50.0f), 1023.0f, 100),
        DayForecast(1643835480, 1643724051, 1643770851, ForcastTemp(45.0f,3.0f,66.0f), 1023.0f, 79),
        DayForecast(1643921880, 1643724051, 1643770851, ForcastTemp(50.0f,10.0f,60.0f), 1023.0f, 71),
        DayForecast(1644008280, 1643724051, 1643770851, ForcastTemp(43.0f,6.0f,52.0f), 1023.0f, 61),
        DayForecast(1644094680, 1643724051, 1643770851, ForcastTemp(25.0f,-1.0f,35.0f), 1023.0f, 38),
        DayForecast(1644181080, 1643724051, 1643770851, ForcastTemp(31.0f,2.0f,40.0f), 1023.0f, 71),
        DayForecast(1644267480, 1643724051, 1643770851, ForcastTemp(24.0f,1.0f,34.0f), 1023.0f, 92),
        DayForecast(1644353880, 1643724051, 1643770851, ForcastTemp(21.0f,1.0f,24.0f), 1023.0f, 72),
        DayForecast(1644440280, 1643724051, 1643770851, ForcastTemp(45.0f,10.0f,52.0f), 1023.0f, 48),
        DayForecast(1644526680, 1643724051, 1643770851, ForcastTemp(41.0f,14.0f,52.0f), 1023.0f, 79),
        DayForecast(1644613080, 1643724051, 1643770851, ForcastTemp(24.0f,1.0f,34.0f), 1023.0f, 66),
        DayForecast(1644699480, 1643724051, 1643770851, ForcastTemp(25.0f,-1.0f,35.0f), 1023.0f, 31),
        DayForecast(1644785880, 1643724051, 1643770851, ForcastTemp(35.0f,4f,37.0f), 1023.0f, 92),
        DayForecast(1644872280, 1643724051, 1643770851, ForcastTemp(1.0f,1.0f,24.0f), 1023.0f, 70),
        DayForecast(1644958680, 1643724051, 1643770851, ForcastTemp(24.0f,14.0f,30.0f), 1023.0f, 88),
        DayForecast(1645045080, 1643724051, 1643770851, ForcastTemp(40.0f,40.0f,40.0f), 1023.0f, 95),


    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forecast)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.adapter = MyAdapter(adapterData)

        recyclerView.addItemDecoration(DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL)) // https://stackoverflow.com/questions/31242812/how-can-a-divider-line-be-added-in-an-android-recyclerview
        recyclerView.layoutManager = LinearLayoutManager(this)
    }
}