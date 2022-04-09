package com.example.weatherapp.fragments

import android.os.Bundle
import android.text.Editable
import android.text.Layout
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import com.example.weatherapp.MainActivity
import com.example.weatherapp.R
import com.example.weatherapp.viewmodels.SearchViewModel
import com.example.weatherapp.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment: Fragment(R.layout.fragment_search){
    private lateinit var binding: FragmentSearchBinding
    @Inject lateinit var searchViewModel: SearchViewModel

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
                findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToCurrentConditionsFragment(binding.zipcode.text.toString(),currentConditions))
            }catch (e:retrofit2.HttpException){
                println("Error Connecting")
                ErrorDialogFragment().show(childFragmentManager,ErrorDialogFragment.TAG)
            }

        }
    }


    }





