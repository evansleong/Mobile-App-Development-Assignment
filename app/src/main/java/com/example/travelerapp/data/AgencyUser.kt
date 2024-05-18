package com.example.travelerapp.data

data class AgencyUser(
    val agencyId: String = "",
    val agencyUsername: String = "",
    val agencyEmail: String = "",
    val agencyPassword: String = "",
    var agencyPicture: String? = null,
    val agencySecureQst: String = ""
)
