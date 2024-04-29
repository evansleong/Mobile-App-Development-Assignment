package com.example.travelerapp.data

data class User (
    val userId: Long,
    val userName: String,
    val userEmail: String,
    val userPw: String,
    val userWalletPin: Int
)