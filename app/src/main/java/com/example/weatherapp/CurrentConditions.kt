package com.example.weatherapp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class CurrentConditions(
    val weather:  List<WeatherCondition>,
    val main: @RawValue CurrentTemps,
    val name: String,
): Parcelable

