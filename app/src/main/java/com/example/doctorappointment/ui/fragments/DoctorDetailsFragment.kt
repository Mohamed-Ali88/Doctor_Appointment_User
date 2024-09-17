package com.example.doctorappointment.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.doctorappointment.R
import com.example.doctorappointment.databinding.FragmentDetailsDoctorBinding
import com.example.doctorappointment.domen.DoctorsModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class DoctorDetailsFragment : Fragment() {
    private lateinit var binding: FragmentDetailsDoctorBinding
    private lateinit var item: DoctorsModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailsDoctorBinding.inflate(layoutInflater)
        getBundle()
        return binding.root
    }

    private fun getBundle() {
        val arguments = requireArguments()
        item = arguments.getParcelable("doctorDetails")!!
        binding.apply {
            tvDoctorName.text = item.Name
            tvRating.text = item.Rating.toString()
            tvAddress.text = item.Address
            tvPatient.text = item.Patiens
            tvSpecial.text = item.Special
            tvExperiens.text = item.Expriense.toString()
            tvBioGraphy.text = item.Biography
            ivBackToHomeFragment.setOnClickListener {
                findNavController().popBackStack()
            }
            llWebsite.setOnClickListener {
                val i = Intent(Intent.ACTION_VIEW)
                i.setData(Uri.parse(item.Site))
                startActivity(i)
            }
            llShare.setOnClickListener {
                val intent = Intent(Intent.ACTION_SEND)
                intent.setType("text/plain")
                intent.putExtra(Intent.EXTRA_SUBJECT, item.Name)
                intent.putExtra(
                    Intent.EXTRA_TEXT,
                    item.Name + " " + item.Address + " " + item.Mobile
                )
                startActivity(Intent.createChooser(intent, "Choose one"))

            }
            llMessage.setOnClickListener {
                val uri = Uri.parse("smsto:${item.Mobile}")
                val intent = Intent(Intent.ACTION_SENDTO, uri)
                intent.putExtra("sms_body", "the SMS text")
                startActivity(intent)
            }
            llCall.setOnClickListener {
                val uri = "tel:${item.Mobile?.trim()}"
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse(uri))
                intent.putExtra("sms_body", "the SMS text")
                startActivity(intent)
            }
            llDirection.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.Location))
                startActivity(intent)
            }

            Glide.with(requireContext())
                .load(item.Picture)
                .into(ivDoctor)
        }
    }
}