package com.example.travelerapp.data

import com.google.firebase.firestore.PropertyName

data class User (
    var userId: String = "",
    val userName: String = "",
    val userEmail: String = "",
    val userPw: String = "",
    var userUri: String = "",
    val userSecureQst: String = "",

//    var userId: String = "",
//    @get:PropertyName("userName") val userName: String = "",
//    @get:PropertyName("userEmail") val userEmail: String = "",
//    @get:PropertyName("userPw") val userPw: String = "",
//    @get:PropertyName("userUri") var userUri: String = "",
//    @get:PropertyName("userSecureQst") val userSecureQst: String = ""
)