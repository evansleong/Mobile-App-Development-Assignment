package com.example.travelerapp.data

data class Trip(
    val tripName: String = "",
    val tripLength: String = "",
    val tripFees: Double = 0.0,
    val tripDeposit: Double = 0.0,
    val tripDesc: String = "",
    val depDate: String = "",
    val retDate: String = ""
)
