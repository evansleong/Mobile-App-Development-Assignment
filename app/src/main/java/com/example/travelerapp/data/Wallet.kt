package com.example.travelerapp.data

data class Wallet(
    val user_id: String = "",
    var available: String = "0",
    val frozen: String = "0",
    var walletPin: String = "",
)