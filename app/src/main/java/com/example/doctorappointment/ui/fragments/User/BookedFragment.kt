package com.example.doctorappointment.ui.fragments.User

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.doctorappointment.databinding.FragmentBookedBinding

class BookedFragment : Fragment() {
    private lateinit var binding : FragmentBookedBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookedBinding.inflate(layoutInflater)
        return binding.root
    }

}