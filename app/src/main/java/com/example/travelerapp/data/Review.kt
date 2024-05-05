package com.example.travelerapp.data

import java.net.URL

data class Review(
    val id: String,
    val trip_id: String,
    val user_id: String,
    val trip_name: String,
    val title: String,
    val rating: Double,
    val comment: String,
    val is_public: Int,
    val imageUrls: String,
    val created_at: Long,
)