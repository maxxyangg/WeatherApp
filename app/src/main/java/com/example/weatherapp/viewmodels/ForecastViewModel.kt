package com.example.weatherapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.Api
import com.example.weatherapp.Coordinates
import com.example.weatherapp.Forecast
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class ForecastViewModel @Inject constructor(private val service: Api): ViewModel() {

    private val _forecastConditions = MutableLiveData<Forecast>()
    val forecastConditions: LiveData<Forecast>
        get() = _forecastConditions

    fun loadData(latitude: Double, longitude: Double) = runBlocking{

        launch{
            _forecastConditions.value = service.getForcast(latitude, longitude)
        }

    }


}