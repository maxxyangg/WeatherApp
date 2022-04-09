package com.example.weatherapp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ForcastTemp(
    val day: Float,
    val min: Float,
    val max: Float,
):Parcelable