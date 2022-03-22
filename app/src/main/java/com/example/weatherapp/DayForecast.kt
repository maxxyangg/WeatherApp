package com.example.weatherapp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class DayForecast(
    val dt: Long,
    val sunrise: Long,
    val sunset: Long,
    val temp: @RawValue ForcastTemp,
    val pressure: Float,
    val humidity: Int,
    val weather: @RawValue List<WeatherCondition>
):Parcelable