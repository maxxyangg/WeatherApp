package com.example.weatherapp.fragments

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager

import android.os.Bundle
import android.os.Looper

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View

import androidx.core.app.ActivityCompat

import androidx.core.content.ContextCompat.startForegroundService
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

import com.example.weatherapp.MyService
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentSearchBinding
import com.example.weatherapp.viewmodels.SearchViewModel
import com.google.android.gms.location.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class SearchFragment: Fragment(R.layout.fragment_search), ActivityCompat.OnRequestPermissionsResultCallback {
    private lateinit var binding: FragmentSearchBinding
    @Inject lateinit var searchViewModel: SearchViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val REQUEST_LOCATION_PERMISSION: Int = 123


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchBinding.bind(view)



        searchViewModel.enableButton.observe(viewLifecycleOwner){ enable ->
            binding.searchButton.isEnabled = enable
        }

        searchViewModel.showErrorDialog.observe(viewLifecycleOwner){showError ->
            if(showError){
                ErrorDialogFragment().show(childFragmentManager, ErrorDialogFragment.TAG)
            }
        }

        binding.zipcode.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                p0?.toString()?.let{searchViewModel.updateZipCode(it)}
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })




        binding.searchButton.setOnClickListener{
            try {
                println("Button clicked")
                searchViewModel.loadData()
                val currentConditions = searchViewModel.currentConditions?.value!!
                findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToCurrentConditionsFragment(binding.zipcode.text.toString(),currentConditions,currentConditions.coord))
            }catch (e:retrofit2.HttpException){
                println("Error Connecting")
                ErrorDialogFragment().show(childFragmentManager,ErrorDialogFragment.TAG)
            }

        }


        binding.locationButton.setOnClickListener{
            requestLocationUpdates()
        }

        binding.notificationButton.setOnClickListener {
            if(isMyServiceRunning(MyService::class.java)){
                requestLocationNotifications()
                binding.notificationButton.setText(R.string.notificationsOnText)

            }else{
                requestLocationNotifications()
                binding.notificationButton.setText(R.string.notificationsOffText)

            }
        }


    }


    private fun requestLocationPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION)){
            AlertDialog.Builder(requireContext())
                .setMessage(R.string.location_permission_rationale)
                .setNeutralButton(R.string.okay){_, _ ->
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION),
                        REQUEST_LOCATION_PERMISSION
                    )
                }
                .create()
                .show()

        }else {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION), REQUEST_LOCATION_PERMISSION)

        }
    }

    private fun requestLocationUpdates(){
        if (ActivityCompat.checkSelfPermission(
                this.requireContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            //  when permission is already granted
            println("You have permissions")
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
            fusedLocationClient.lastLocation
                .addOnSuccessListener {
                    requestNewLocation()
                    Log.d("MainActivity", it.toString())
                    searchViewModel.updateCoordinates(it.latitude,it.longitude)
                    searchViewModel.loadLocationData()
                    val currentConditions = searchViewModel.currentConditions?.value!!
                    findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToCurrentConditionsFragment(binding.zipcode.text.toString(),currentConditions,currentConditions.coord))
                }
        } else {
            requestLocationPermission()
        }


    }

    private fun requestNewLocation() {
        if (ActivityCompat.checkSelfPermission(
                this.requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this.requireContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        val locationRequest = LocationRequest.create()
        locationRequest.interval = 0L
        locationRequest.fastestInterval =  0L
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.setInterval(60000 * 15)
        val locationProvider = LocationServices.getFusedLocationProviderClient(this.requireActivity())
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.locations.forEach {
                    println(it.toString())
                }
            }
        }

        locationProvider.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }


    private fun requestLocationNotifications(){
        if (ActivityCompat.checkSelfPermission(
                this.requireContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            //  when permission is already granted
            println("You have permissions")
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.requireActivity())
            fusedLocationClient.lastLocation
                .addOnSuccessListener {
                    requestNewLocation()
                    Log.d("MainActivity", it.toString())
                    searchViewModel.updateCoordinates(it.latitude,it.longitude)
                    searchViewModel.loadLocationData()
                    val currentConditions = searchViewModel.currentConditions?.value!!
                    var serviceIntent = Intent(requireContext(), MyService::class.java)
                    serviceIntent.putExtra("CityName", currentConditions.name)
                    serviceIntent.putExtra("CurrentTemp", currentConditions.main.temp)
                    startForegroundService(requireContext(), serviceIntent)
                }



        } else {
            requestLocationPermission()
        }

    }

    private fun isMyServiceRunning(myClass: Class<MyService>): Boolean {
        val manager: ActivityManager = requireActivity().getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

        @Suppress("DEPRECATION")
        for(service: ActivityManager.RunningServiceInfo in manager.getRunningServices(Int.MAX_VALUE)){
            if (myClass.name.equals(service.service.className)){

                return true;
            }
        }
        return false;
    }



}



