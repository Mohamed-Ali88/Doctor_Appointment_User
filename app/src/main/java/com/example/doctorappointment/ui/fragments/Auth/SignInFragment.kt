package com.example.doctorappointment.ui.fragments.Auth

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.doctorappointment.databinding.FragmentSignInBinding
import android.os.Build
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.doctorappointment.R
import com.example.doctorappointment.others.Utils
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices

class SignInFragment : Fragment() {
    private lateinit var binding: FragmentSignInBinding

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private var currentLocation: android.location.Location? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignInBinding.inflate(layoutInflater)
        setStatusBarColor()
        getUserNumber()
        onContinueButtonClick()
        getCurrentLocation()
        return binding.root
    }

    private fun onContinueButtonClick() {
        binding.btnContinue.setOnClickListener {
            val number = binding.etUserNumber.text.toString()
            val name = binding.etUserName.text.toString()
            if (number.isEmpty() || number.length != 11) {
                Utils.showToast(requireContext(), "Please enter valid phone number")
            } else {
                if (name.isEmpty()) {
                    Utils.showToast(requireContext(), "Please enter your name")
                } else {
                    val bundle = Bundle()
                    bundle.putString("number", number)
                    bundle.putString("name", name)
                    bundle.putString("location",currentLocation.toString())
                    findNavController().navigate(
                        R.id.action_signInFragment_to_OTPFragment,
                        bundle
                    )
                }
            }
        }
    }

    private fun getUserNumber() {
        binding.etUserNumber.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }
                override fun onTextChanged(number: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    val len = number?.length
                    if (len == 11) {
                        binding.btnContinue.setBackgroundColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.darkPurple
                            )
                        )
                    } else {
                        binding.btnContinue.setBackgroundColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.purple
                            )
                        )
                    }
                }

                override fun afterTextChanged(p0: Editable?) {

                }
            })
    }

    private fun setStatusBarColor() {
        activity?.window?.apply {
            val statusBarColors = ContextCompat.getColor(requireContext(), R.color.purple)
            statusBarColor = statusBarColors
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
    }

    private fun getCurrentLocation(){
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        locationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            object : LocationCallback() {
                override fun onLocationResult(p0: LocationResult) {
                    super.onLocationResult(p0!!)
                    p0?.lastLocation?.let {
                        currentLocation = it
                        val latitude = currentLocation?.latitude
                        val longitude = currentLocation?.longitude
                        Utils.showToast(requireContext(),"Latitude: $latitude, Longitude: $longitude")
                    }
                }
            },
            Looper.myLooper()
        )
    }

}