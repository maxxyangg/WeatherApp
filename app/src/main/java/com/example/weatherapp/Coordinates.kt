package com.example.weatherapp

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Coordinates (
    @Json(name = "lat") val latitude : Double,
    @Json(name = "lon") val longitude: Double,
): Parcelable