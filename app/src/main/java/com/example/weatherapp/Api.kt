package com.example.weatherapp

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    //@Query("zip") zip: String,
    @GET("weather")
    suspend fun getCurrentConditions(
        @Query("zip") zip: String,
        @Query("units") units: String = "imperial",
        @Query("appid") appId: String = "8c7e3d3413e2bbaae86a6b40d113721b",
    ) : CurrentConditions

    //@Query("zip") zip: String,
    @GET("forecast/daily")
    suspend fun getForcast(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("units") units: String = "imperial",
        @Query("appid") appId: String = "8c7e3d3413e2bbaae86a6b40d113721b",
        @Query("cnt") cnt: Int = 16,
    ) : Forecast

    @GET("weather")
    suspend fun getCurrentConditionsLocation(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("units") units: String = "imperial",
        @Query("appid") appId: String = "8c7e3d3413e2bbaae86a6b40d113721b",
    ) : CurrentConditions


}