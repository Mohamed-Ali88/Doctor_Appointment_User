package com.example.doctorappointment.domen

data class User(
    var uid: String? = null,
    var name: String? = null,
    val userPhoneNumber: String? = null,
    val userAddress: String? = null,
    var userToken: String? = null
)
