package com.example.doctorappointment.ui.fragments.Auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.doctorappointment.R
import com.example.doctorappointment.databinding.FragmentOTPBinding

class OTPFragment : Fragment() {
    private lateinit var binding: FragmentOTPBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOTPBinding.inflate(layoutInflater)
        return binding.root
    }
}