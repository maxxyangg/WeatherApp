package com.example.weatherapp

data class CurrentConditions(
    val weather: List<WeatherCondition>,
    val main: CurrentTemps,
    val name: String,
)

