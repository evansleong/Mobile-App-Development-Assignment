package com.example.travelerapp.data

import java.net.URL

data class Review(
    val id: Int,
    val title: String,
    val rating: Double,
    val comment: String,
    val is_public: Int,
    val imageUrl: String,
    val created_at: Long,
)