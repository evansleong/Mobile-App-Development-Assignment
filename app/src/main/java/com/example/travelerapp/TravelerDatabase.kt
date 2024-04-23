package com.example.travelerapp

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [User::class], version = 1)
abstract class TravelerDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}