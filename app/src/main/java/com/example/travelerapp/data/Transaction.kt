package com.example.travelerapp.data

data class Transaction(
    var id: String = "",
    val trip_id: String = "",
    val user_id: String = "",
    val operation: String = "",
    val amount: String = "0",
    val status: String = "",
    val remarks: String = "",
    val description: String = "",
    val created_at: Long = 0L,
)
