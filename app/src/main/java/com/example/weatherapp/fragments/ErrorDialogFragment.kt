package com.example.weatherapp.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.weatherapp.R

class ErrorDialogFragment: DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setMessage("Error fetching data for that zip code")
            .setPositiveButton(R.string.ok, null)
            .create()

    companion object{
        const val TAG = "ErrorDialogFragment"
    }
}