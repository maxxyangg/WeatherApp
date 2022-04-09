package com.example.weatherapp.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.DayForecast
import com.example.weatherapp.MyAdapter
import com.example.weatherapp.R

import com.example.weatherapp.databinding.FragmentForecastBinding
import com.example.weatherapp.viewmodels.ForecastViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class ForecastFragment : Fragment(R.layout.fragment_forecast) {

    private lateinit var recyclerView: RecyclerView
    lateinit var adapterData : List<DayForecast>
    private lateinit var binding: FragmentForecastBinding
    @Inject lateinit var viewModel: ForecastViewModel
    private val args: ForecastFragmentArgs by navArgs()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentForecastBinding.bind(view)

        adapterData = mutableListOf()
        recyclerView = view.findViewById(R.id.recyclerView)
        binding.recyclerView.adapter = MyAdapter(adapterData)
        binding.recyclerView.layoutManager = LinearLayoutManager(view.context)

        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.getContext(),
                DividerItemDecoration.VERTICAL
            )
        ) // https://stackoverflow.com/questions/31242812/how-can-a-divider-line-be-added-in-an-android-recyclerview

        viewModel.loadData(args.zipCode)
        viewModel.forecastConditions.observe(viewLifecycleOwner){
                forecast -> binding.recyclerView.adapter = MyAdapter(forecast.list)
        }

    }

}