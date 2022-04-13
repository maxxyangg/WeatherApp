package com.example.weatherapp.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.text.Editable
import android.text.Layout
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import com.example.weatherapp.MainActivity
import com.example.weatherapp.R
import com.example.weatherapp.viewmodels.SearchViewModel
import com.example.weatherapp.databinding.FragmentSearchBinding
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import java.util.*
import java.util.jar.Manifest
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment: Fragment(R.layout.fragment_search), ActivityCompat.OnRequestPermissionsResultCallback {
    private lateinit var binding: FragmentSearchBinding
    @Inject lateinit var searchViewModel: SearchViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val REQUEST_LOCATION_PERMISSION: Int = 123



    @RequiresApi(Build.VERSION_CODES.N)
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
        } else {
            requestLocationPermission()
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.requireActivity())
        fusedLocationClient.lastLocation
            .addOnSuccessListener {
                requestNewLocation()
                Log.d("MainActivity", it.toString())
                searchViewModel.updateCoordinates(it.latitude,it.longitude)
                searchViewModel.loadLocationData()
                val currentConditions = searchViewModel.currentConditions?.value!!
                findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToCurrentConditionsFragment(binding.zipcode.text.toString(),currentConditions,currentConditions.coord))
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
        locationRequest.fastestInterval = 0L
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        val locationProvider = LocationServices.getFusedLocationProviderClient(this.requireActivity())
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.locations.forEach {

                }
            }
        }

        locationProvider.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }




}





