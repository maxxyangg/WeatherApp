package com.example.weatherapp

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class MyAdapter(private val data: List<DayForecast>) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        @SuppressLint("NewApi")
        val formatter = DateTimeFormatter.ofPattern("MMM dd")
        @SuppressLint("NewApi")
        val formatterTwo = DateTimeFormatter.ofPattern("h:mma")
        val dateView: TextView = view.findViewById(R.id.date)
        val dayTemp : TextView = view.findViewById(R.id.day_temp)
        val highTemp : TextView = view.findViewById(R.id.high_temp)
        val lowTemp : TextView = view.findViewById(R.id.low_temp)
        val humidity : TextView = view.findViewById(R.id.humidity_forecast)
        val pressure : TextView = view.findViewById(R.id.pressure_forecast)
        val sunrise : TextView = view.findViewById(R.id.sunrise)
        val sunset : TextView = view.findViewById(R.id.sunset)

        @SuppressLint("NewApi")
        fun bind(data: DayForecast){
            val instant = Instant.ofEpochSecond(data.date)
            val sunriseInstant = Instant.ofEpochSecond(data.sunrise)
            val sunsetInstant = Instant.ofEpochSecond(data.sunset)
            val dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
            val sunriseTime = LocalDateTime.ofInstant(sunriseInstant, ZoneId.systemDefault())
            val sunsetTime = LocalDateTime.ofInstant(sunsetInstant, ZoneId.systemDefault())

            dayTemp.append(" " + data.temp.day
                .toString()+"°")
            highTemp.append(" " + data.temp.max.toString() + "°")
            lowTemp.append(" " + data.temp.min.toString() + "°")
            humidity.append(" "+data.humidity.toString() + "%")
            pressure.append(" " + data.pressure.toString() + " hPa")
            sunrise.append(" " + formatterTwo.format(sunriseTime))
            sunset.append(" " + formatterTwo.format(sunsetTime))

            dateView.text = formatter.format(dateTime)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_date, parent, false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int{
        return data.size
    }


}