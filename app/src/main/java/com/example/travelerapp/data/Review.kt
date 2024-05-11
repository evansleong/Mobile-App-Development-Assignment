package com.example.travelerapp.data

import java.net.URL

data class Review(
    var id: String = "",
    val trip_id: String = "",
    val user_id: String = "",
    val trip_name: String = "",
    val title: String = "",
    val rating: Int = 0,
    val comment: String = "",
    val is_public: Int = 1,
    val imageUrls: String = "",
    val created_at: Long = 0L,
)