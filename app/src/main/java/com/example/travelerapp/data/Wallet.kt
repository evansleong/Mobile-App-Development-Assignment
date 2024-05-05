package com.example.travelerapp.data

data class Wallet(
    val user_id: String,
    val available: Double = 0.0,
    val frozen: Double = 0.0,
    val walletPin: String = "",
)