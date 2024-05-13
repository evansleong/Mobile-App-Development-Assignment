package com.example.travelerapp.data

import com.google.firebase.firestore.PropertyName

data class Trip(
    val tripId: String = "",
    val tripName: String = "",
    val tripLength: String = "",
    val tripFees: Double = 0.0,
    val tripDeposit: Double = 0.0,
    @get:PropertyName("isAvailable")
    @set:PropertyName("isAvailable")
    var isAvailable: Int = 0,
    @get:PropertyName("noOfUserBooked")
    @set:PropertyName("noOfUserBooked")
    var noOfUserBooked: Int = 0,
    val tripDesc: String = "",
    val depDate: String = "",
    val retDate: String = "",
    val tripUri: String = "",
    val options: List<String> = emptyList(),
    val agencyUsername: String = "", //FK
)
