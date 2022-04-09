package com.example.weatherapp.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.Api
import com.example.weatherapp.CurrentConditions
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import kotlin.random.Random

class SearchViewModel @Inject constructor(private val service: Api) : ViewModel() {

    private var zipCode: String? = null

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

    private fun isValidZipCode(zipCode: String): Boolean {
        return zipCode.length == 5 && zipCode.all { it.isDigit() }
    }

    fun loadData() = runBlocking {
        launch {
            println("Successful Call to CurrentConditions")
            println(zipCode)
            _currentConditions.value = zipCode?.let { service.getCurrentConditions(it) }
            println(currentConditions.value?.name)
        }

    }



}