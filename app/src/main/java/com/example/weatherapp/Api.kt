package com.example.weatherapp

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET("weather")
    fun getCurrentConditions(
        @Query("zip") zip: String,
        @Query("units") units: String = "imperial",
        @Query("appid") appId: String = "8c7e3d3413e2bbaae86a6b40d113721b",
    ) : Call<CurrentConditions>


    @GET("forecast/daily")
    fun getForcast(
        @Query("zip") zip: String,
        @Query("units") units: String = "imperial",
        @Query("appid") appId: String = "8c7e3d3413e2bbaae86a6b40d113721b",
        @Query("cnt") cnt: Int = 16,
    ) : Call<Forecast>
}