package com.example.travelerapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val username: String,
    val email: String,
    val password: String
)