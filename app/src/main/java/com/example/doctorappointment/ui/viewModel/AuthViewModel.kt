package com.example.doctorappointment.ui.viewModel

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.example.doctorappointment.domen.User
import com.example.doctorappointment.others.Utils
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.concurrent.TimeUnit

class AuthViewModel : ViewModel() {

    private val _verificationId = MutableStateFlow<String?>(null)
    private val _otpSent = MutableStateFlow(false)
    val otpSent = _otpSent

    private val _idSignedInSuccessfully = MutableStateFlow(false)
    val isSignedInSuccessfully = _idSignedInSuccessfully

    private val _isACurrentUser = MutableStateFlow(false)
    val isACurrentUser = _isACurrentUser

    init {
        Utils.getAuthInstance().currentUser?.let {
            _isACurrentUser.value = true
        }
    }

    fun sendOTP(userNumber: String, activity: Activity) {

        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            }

            override fun onVerificationFailed(e: FirebaseException) {
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken,
            ) {
                _verificationId.value = verificationId
                _otpSent.value = true
            }
        }

        val options = PhoneAuthOptions.newBuilder(Utils.getAuthInstance())
            .setPhoneNumber("+2${userNumber}") // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(activity) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun signInWithPhoneAuthCredential(otp: String, userNumber: String, user: User) {
        val credential = PhoneAuthProvider.getCredential(_verificationId.value.toString(), otp)
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            val token = task.result
            user.userToken = token
            Utils.getAuthInstance().signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    user.uid = Utils.getCurrentUserId()
                    if (task.isSuccessful) {
                        FirebaseDatabase.getInstance().getReference("Users")
                            .child(user.uid!!).setValue(user)
                        _idSignedInSuccessfully.value = true
                    }
                }
        }
    }

}