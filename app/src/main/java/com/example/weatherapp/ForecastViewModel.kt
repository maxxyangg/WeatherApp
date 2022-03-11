package com.example.weatherapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class ForecastViewModel @Inject constructor(private val service: Api): ViewModel() {

    private val _forecastConditions = MutableLiveData<Forecast>()
    val forecastConditions: LiveData<Forecast>
        get() = _forecastConditions

    fun loadData() = runBlocking{

        launch{
            _forecastConditions.value = service.getForcast("55038")
        }

    }


}