package com.example.travelerapp.data

data class Transaction(
    val id: String,
    val trip_id: String,
    val wallet_id: String,
    val operation: String,
    val amount: Double = 0.0,
    val status: String,
    val remarks: String = "",
    val description: String = "",
    val created_at: Long,
)
