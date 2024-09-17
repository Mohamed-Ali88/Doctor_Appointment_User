package com.example.doctorappointment.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.doctorappointment.R
import com.example.doctorappointment.adapter.DoctorsAdapter
import com.example.doctorappointment.databinding.FragmentTopDoctorsBinding
import com.example.doctorappointment.domen.DoctorsModel
import com.example.doctorappointment.ui.viewModel.MainViewModel
import kotlinx.coroutines.launch

class TopDoctorsFragment : Fragment() {
    private lateinit var binding: FragmentTopDoctorsBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var doctorsAdapter: DoctorsAdapter
    private var topDoctor: Boolean = false
    private var categoryName: String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTopDoctorsBinding.inflate(layoutInflater)
        getBundle()
        onBackClicked()
        return binding.root
    }
    private fun onBackClicked() {
        binding.tbTopDoctor.setOnClickListener {
            findNavController().navigate(R.id.action_topDoctors_to_home)
        }
    }

    private fun getBundle() {
        val bundle = arguments
        topDoctor = bundle?.getBoolean("topDoctor", false)!!
        categoryName = bundle?.getString("categoryName","")!!
        if (topDoctor) {
            initDoctors(true)
        } else {
            initDoctors(categoryName)
        }

    }

    private fun initDoctors(topDoctor: Boolean) {
        binding.pbDoctors.visibility = View.VISIBLE
        lifecycleScope.launch {
            viewModel.fetchAllDoctors(topDoctor).collect {
                if (it.isNullOrEmpty()) {
                    binding.rvDoctors.visibility = View.GONE
                } else {
                    binding.rvDoctors.visibility = View.VISIBLE
                }
                doctorsAdapter = DoctorsAdapter(::onDoctorClicked)
                doctorsAdapter.differ.submitList(it)
                binding.rvDoctors.adapter = doctorsAdapter
                binding.pbDoctors.visibility = View.GONE
            }
        }
    }

    private fun initDoctors(categoryName: String) {
        binding.pbDoctors.visibility = View.VISIBLE
        lifecycleScope.launch {
            viewModel.fetchCategoryDoctors(categoryName).collect {
                if (it.isEmpty()) {
                    binding.rvDoctors.visibility = View.GONE
                    binding.tvNoDoctors.visibility = View.VISIBLE
                } else {
                    binding.rvDoctors.visibility = View.VISIBLE
                    binding.tvNoDoctors.visibility = View.GONE
                }
                doctorsAdapter = DoctorsAdapter(::onDoctorClicked)
                doctorsAdapter.differ.submitList(it)
                binding.rvDoctors.adapter = doctorsAdapter
                binding.pbDoctors.visibility = View.GONE
            }
        }
    }

    private fun onDoctorClicked(doctors: DoctorsModel) {
        val bundle = Bundle()
        bundle.putParcelable("doctorDetails", doctors)
        findNavController().navigate(R.id.action_topDoctors_to_doctorDetails, bundle)
    }

}