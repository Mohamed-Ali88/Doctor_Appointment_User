package com.example.doctorappointment.ui.fragments.User

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.FragmentNavigatorExtras
import com.example.doctorappointment.R
import com.example.doctorappointment.databinding.FragmentMakeAppointmentBinding

class MakeAppointmentFragment : Fragment() {
    private lateinit var binding: FragmentMakeAppointmentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMakeAppointmentBinding.inflate(layoutInflater)
        return binding.root
    }

}