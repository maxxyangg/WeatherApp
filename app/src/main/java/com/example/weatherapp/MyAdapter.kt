package com.example.weatherapp

import android.annotation.SuppressLint
import android.app.Activity
import android.media.Image

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
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

        var dateView: TextView = view.findViewById(R.id.date)
        var dayTemp : TextView = view.findViewById(R.id.day_temp)
        var highTemp : TextView = view.findViewById(R.id.high_temp)
        var lowTemp : TextView = view.findViewById(R.id.low_temp)
        var humidity : TextView = view.findViewById(R.id.humidity_forecast)
        var pressure : TextView = view.findViewById(R.id.pressure_forecast)
        var sunrise : TextView = view.findViewById(R.id.sunrise)
        var sunset : TextView = view.findViewById(R.id.sunset)
        var conditionIcon : ImageView = view.findViewById(R.id.forecast_icon)
        var newview : View = view

        @SuppressLint("NewApi")
        fun bind(data: DayForecast){

            val instant = Instant.ofEpochSecond(data.dt)
            val sunriseInstant = Instant.ofEpochSecond(data.sunrise)
            val sunsetInstant = Instant.ofEpochSecond(data.sunset)
            val dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
            val sunriseTime = LocalDateTime.ofInstant(sunriseInstant, ZoneId.systemDefault())
            val sunsetTime = LocalDateTime.ofInstant(sunsetInstant, ZoneId.systemDefault())


            dayTemp.text = ("Temp: ${data.temp.day.toInt().toString()}°")
            highTemp.text = ("High: ${data.temp.max.toInt().toString()}°")
            lowTemp.text = ("Low: ${data.temp.min.toInt().toString()}")
            humidity.text = ("Humidity: ${data.humidity.toInt().toString()}%")
            pressure.text = ("Pressure: ${data.pressure.toInt().toString()}hPa")
            sunrise.text = ("Sunrise: ${formatterTwo.format(sunriseTime)}")
            sunset.text = ("Sunset: ${formatterTwo.format(sunsetTime)}")
            dateView.text = ("${formatter.format(dateTime)}")


            val iconName = data.weather.firstOrNull()?.icon
            val iconUrl = "https://openweathermap.org/img/wn/${iconName}@2x.png"
            Glide.with(newview).load(iconUrl).into(conditionIcon)

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