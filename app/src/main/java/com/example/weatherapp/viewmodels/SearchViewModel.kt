package com.example.weatherapp.viewmodels

import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.Api
import com.example.weatherapp.Coordinates
import com.example.weatherapp.CurrentConditions
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.jar.Manifest
import javax.inject.Inject
import kotlin.random.Random

class SearchViewModel @Inject constructor(private val service: Api) : ViewModel() {

    private var zipCode: String? = null
    private var lat: Double? = null
    private var lon: Double? = null

    private val _enableButton = MutableLiveData(false)
    private val _showErrorDialog = MutableLiveData(false)
    private val _currentConditions = MutableLiveData<CurrentConditions>()

    val currentConditions: LiveData<CurrentConditions>
        get() = _currentConditions

    val showErrorDialog: LiveData<Boolean>
        get() = _showErrorDialog

    val enableButton: LiveData<Boolean>
        get() = _enableButton




    fun updateZipCode(zipCode: String){
        if(zipCode != this.zipCode){
            _enableButton.value = isValidZipCode(zipCode)
            this.zipCode = zipCode
        }
    }

    fun updateCoordinates(latitude: Double, longitude: Double){
        this.lat = latitude
        this.lon = longitude
    }

    private fun isValidZipCode(zipCode: String): Boolean {
        return zipCode.length == 5 && zipCode.all { it.isDigit() }
    }

    fun loadData() = runBlocking {
        launch {
            _currentConditions.value = zipCode?.let { service.getCurrentConditions(it) }
        }
    }

    fun loadLocationData() = runBlocking {
        launch {
            _currentConditions.value = lat?.let { lon?.let { it1 ->
                service.getCurrentConditionsLocation(it,
                    it1
                )
            } }
        }
    }







}