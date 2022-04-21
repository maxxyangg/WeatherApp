package com.example.weatherapp.fragments


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs

import com.bumptech.glide.Glide
import com.example.weatherapp.CurrentConditions
import com.example.weatherapp.viewmodels.CurrentConditionsViewModel
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentCurrentconditionsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@AndroidEntryPoint
class CurrentConditionsFragment : Fragment(R.layout.fragment_currentconditions) {

    private val apiKey = "8c7e3d3413e2bbaae86a6b40d113721b"
    private lateinit var binding: FragmentCurrentconditionsBinding
    @Inject lateinit var viewModel: CurrentConditionsViewModel
    private val args: CurrentConditionsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCurrentconditionsBinding.bind(view)
        bindData()

        binding.forecastButton.setOnClickListener{
            val action = CurrentConditionsFragmentDirections.actionCurrentConditionsFragmentToForecastFragment(args.coordinates)
            findNavController().navigate(action)
        }

    }

    private fun bindData(){

        binding.cityName.text = args.currentConditions.name
        binding.temperature.text = getString(R.string.temperature, args.currentConditions.main.temp.toInt())
        binding.feelsLike.text = getString(R.string.feels_like, args.currentConditions.main.feelsLike.toInt())
        binding.low.text = getString(R.string.low,args.currentConditions.main.tempMin.toInt())
        binding.high.text = getString(R.string.high, args.currentConditions.main.tempMax.toInt())
        binding.humidity.text = getString(R.string.humidity, args.currentConditions.main.humidity.toInt())
        binding.pressure.text = getString(R.string.pressure, args.currentConditions.main.pressure.toInt())

        val iconName = "${args.currentConditions.weather.firstOrNull()?.icon}"
        val iconUrl = "https://openweathermap.org/img/wn/${iconName}@2x.png"
        Glide.with(this)
            .load(iconUrl)
            .into(binding.conditionIcon)

    }

}