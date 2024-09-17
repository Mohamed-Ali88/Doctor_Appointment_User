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
import com.example.doctorappointment.adapter.CategoryAdapter
import com.example.doctorappointment.adapter.TopDoctorsAdapter
import com.example.doctorappointment.databinding.FragmentHomeBinding
import com.example.doctorappointment.domen.CategoryModel
import com.example.doctorappointment.domen.DoctorsModel
import com.example.doctorappointment.ui.viewModel.MainViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var doctorsAdapter: TopDoctorsAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        initCategory()
        initTopDoctors()
        onSeeAllClicked()
        controlAppearingBottomNavigation()

        return binding.root
    }

    private fun initCategory() {
        binding.pbCategory.visibility = View.VISIBLE
        lifecycleScope.launch {
            viewModel.fetchAllCategories().collect {
                if (it.isEmpty()) {
                    binding.rvCategory.visibility = View.GONE
                    binding.llCategory.visibility = View.GONE
                } else {
                    binding.rvCategory.visibility = View.VISIBLE
                    binding.llCategory.visibility = View.VISIBLE
                }
                categoryAdapter = CategoryAdapter(::onCategoryClicked)
                categoryAdapter.differ.submitList(it)
                binding.rvCategory.adapter = categoryAdapter
                binding.pbCategory.visibility = View.GONE
            }
        }
    }

    private fun initTopDoctors() {
        binding.pbTopDoctors.visibility = View.VISIBLE
        lifecycleScope.launch {
            viewModel.fetchAllDoctors(true).collect {
                if (it.isEmpty()) {
                    binding.rvTopDoctors.visibility = View.GONE
                    binding.llTopDoctors.visibility = View.GONE
                } else {
                    binding.rvTopDoctors.visibility = View.VISIBLE
                    binding.llTopDoctors.visibility = View.VISIBLE
                }
                doctorsAdapter = TopDoctorsAdapter(::onTopDoctorClicked)
                doctorsAdapter.differ.submitList(it)
                binding.rvTopDoctors.adapter = doctorsAdapter
                binding.pbTopDoctors.visibility = View.GONE
            }
        }
    }

    private fun controlAppearingBottomNavigation() {
        val navController = findNavController()
        val bottomNavigationView =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.categoryDoctors -> bottomNavigationView.visibility = View.GONE
                R.id.topDoctors -> bottomNavigationView.visibility = View.GONE
                R.id.doctorDetails -> bottomNavigationView.visibility = View.GONE
                R.id.categoryDoctors -> bottomNavigationView.visibility = View.GONE
                R.id.home -> bottomNavigationView.visibility = View.VISIBLE
            }
        }
    }

    private fun onCategoryClicked(category: CategoryModel) {
        val bundle = Bundle()
        bundle.putString("categoryName", category.Name.toString())
        findNavController().navigate(R.id.action_home_to_topDoctors, bundle)
    }

    private fun onSeeAllClicked() {
        binding.tvSeeAllCategoryDoctors.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_categoryDoctors)
        }
        binding.tvSeeAllTopDoctors.setOnClickListener {
            val bundle = Bundle()
            bundle.putBoolean("topDoctor", true)
            findNavController().navigate(R.id.action_home_to_topDoctors, bundle)
        }
    }

    private fun onTopDoctorClicked(doctors: DoctorsModel) {
        val bundle = Bundle()
        bundle.putParcelable("doctorDetails", doctors)
        findNavController().navigate(R.id.action_home_to_doctorDetails, bundle)
    }
}