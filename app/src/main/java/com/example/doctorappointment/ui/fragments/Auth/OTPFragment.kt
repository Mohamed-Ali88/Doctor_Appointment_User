package com.example.doctorappointment.ui.fragments.Auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.doctorappointment.R
import com.example.doctorappointment.databinding.FragmentOTPBinding
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.doctorappointment.domen.User
import com.example.doctorappointment.others.Utils
import com.example.doctorappointment.ui.activities.MainActivity
import com.example.doctorappointment.ui.viewModel.AuthViewModel
import kotlinx.coroutines.launch
class OTPFragment : Fragment() {
    private lateinit var binding: FragmentOTPBinding

    private lateinit var userNumber: String

    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOTPBinding.inflate(layoutInflater)
        getUserNumber()
        customizingEnteringOtp()
        sendOTP()
        onBackButtonClicked()
        onLoginButtonClicked()
        return binding.root
    }

    private fun onLoginButtonClicked() {
        binding.btnLogin.setOnClickListener {
            Utils.showDialog(requireContext(), "Singing now....")
            val editTexts = arrayOf(
                binding.et0tp1,
                binding.et0tp2,
                binding.et0tp3,
                binding.et0tp4,
                binding.et0tp5,
                binding.et0tp6
            )
            val otp = editTexts.joinToString("") { it.text.toString() }
            if (otp.length < editTexts.size) {
                Utils.showToast(requireContext(), "Please enter right otp")
            } else {
                editTexts.forEach { it.text?.clear(); it.clearFocus() }
                verifyOTP(otp)
            }
        }
    }

    private fun verifyOTP(otp: String) {
        val user =
            User(uid = null, userPhoneNumber = userNumber, userAddress = " ")
        viewModel.apply {
            signInWithPhoneAuthCredential(otp, userNumber, user)
            lifecycleScope.launch {
                isSignedInSuccessfully.collect {
                    if (it) {
                        Utils.apply {
                            showToast(requireContext(), "Logged In....")
                            hideDialog()
                            startActivity(Intent(requireContext(), MainActivity::class.java))
                            requireActivity().finish()
                        }
                    }
                }
            }
        }
    }

    private fun sendOTP() {
        Utils.showDialog(requireContext(), "Sending OTP....")
        viewModel.apply {
            sendOTP(userNumber, requireActivity())
            lifecycleScope.launch {
                otpSent.collect { otpSent ->
                    if (otpSent) {
                        Utils.hideDialog()
                        Utils.showToast(requireContext(), "Otp sent....")
                    }
                }
            }
        }
    }

    private fun onBackButtonClicked() {
        binding.tb0tpFragment.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_OTPFragment_to_signInFragment)
        }
    }

    private fun customizingEnteringOtp() {
        val editTexts = arrayOf(
            binding.et0tp1,
            binding.et0tp2,
            binding.et0tp3,
            binding.et0tp4,
            binding.et0tp5,
            binding.et0tp6
        )
        for (i in editTexts.indices) {
            editTexts[i].addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    if (s?.length == 1) {
                        if (i < editTexts.size - 1) {
                            editTexts[i + 1].requestFocus()
                        }
                    } else if (s?.length == 0) {
                        if (i > 0) {
                            editTexts[i - 1].requestFocus()
                        }
                    }
                }
            })
        }
    }

    private fun getUserNumber() {
        val bundle = arguments
        userNumber = bundle?.getString("number").toString()
        binding.tvUserNumber.text = userNumber


    }

}