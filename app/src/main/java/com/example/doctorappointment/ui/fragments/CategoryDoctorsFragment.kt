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
import com.example.doctorappointment.adapter.DoctorsAdapter
import com.example.doctorappointment.adapter.TopDoctorsAdapter
import com.example.doctorappointment.databinding.FragmentDoctorCategoryBinding
import com.example.doctorappointment.domen.CategoryModel
import com.example.doctorappointment.domen.DoctorsModel
import com.example.doctorappointment.ui.viewModel.MainViewModel
import kotlinx.coroutines.launch

class CategoryDoctorsFragment : Fragment() {
    private lateinit var binding: FragmentDoctorCategoryBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var doctorsAdapter: TopDoctorsAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDoctorCategoryBinding.inflate(layoutInflater)
        initCategory()
        initAllDoctors()
        onBackClicked()
        onLlAllClicked()
        return binding.root
    }

    private fun onLlAllClicked() {
        binding.llAll.setOnClickListener {
            initAllDoctors()
        }
    }

    private fun onBackClicked() {
        binding.tbTopDoctor2.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_categoryDoctors_to_home)
        }
    }

    private fun initCategory() {
        binding.pbCategory.visibility = View.VISIBLE
        lifecycleScope.launch {
            viewModel.fetchAllCategories().collect {
                if (it.isEmpty()) {
                    binding.rvCategory.visibility = View.GONE
                } else {
                    binding.rvCategory.visibility = View.VISIBLE
                }
                categoryAdapter = CategoryAdapter(::onCategoryClicked)
                categoryAdapter.differ.submitList(it)
                binding.rvCategory.adapter = categoryAdapter
                binding.pbCategory.visibility = View.GONE
            }
        }
    }

    private fun initAllDoctors() {
        binding.pbDoctors.visibility = View.VISIBLE
        lifecycleScope.launch {
            viewModel.fetchAllDoctors(false).collect {
                if (it.isNullOrEmpty()) {
                    binding.rvDoctors.visibility = View.GONE
                    binding.tvNoDoctors.visibility = View.VISIBLE
                } else {
                    binding.rvDoctors.visibility = View.VISIBLE
                    binding.tvNoDoctors.visibility = View.GONE
                }
                doctorsAdapter = TopDoctorsAdapter(::onTopDoctorClicked)
                doctorsAdapter.differ.submitList(it)
                binding.rvDoctors.adapter = doctorsAdapter
                binding.pbDoctors.visibility = View.GONE
            }
        }
    }

    private fun onCategoryClicked(category: CategoryModel) {
        initDoctors(category.Name.toString())
    }

    private fun initDoctors(categoryName: String) {
        binding.pbDoctors.visibility = View.VISIBLE
        lifecycleScope.launch {
            viewModel.fetchCategoryDoctors(categoryName).collect {
                if (it.isNullOrEmpty()) {
                    binding.rvDoctors.visibility = View.GONE
                    binding.tvNoDoctors.visibility = View.VISIBLE
                } else {
                    binding.rvDoctors.visibility = View.VISIBLE
                    binding.tvNoDoctors.visibility = View.GONE
                }
                doctorsAdapter = TopDoctorsAdapter(::onTopDoctorClicked)
                doctorsAdapter.differ.submitList(it)
                binding.rvDoctors.adapter = doctorsAdapter
                binding.pbDoctors.visibility = View.GONE
            }
        }
    }

    private fun onTopDoctorClicked(doctors: DoctorsModel) {
        val bundle = Bundle()
        bundle.putParcelable("doctorDetails", doctors)
        findNavController().navigate(R.id.action_categoryDoctors_to_doctorDetails, bundle)
    }
}