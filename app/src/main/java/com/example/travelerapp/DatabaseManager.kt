package com.example.travelerapp

import android.content.Context
import androidx.room.Room

object DatabaseManager {
    private var instance: TravelerDatabase? = null

    fun getDatabase(context: Context): TravelerDatabase {
        return instance ?: synchronized(this) {
            val newInstance = Room.databaseBuilder(
                context.applicationContext,
                TravelerDatabase::class.java,
                "traveler_database"
            ).build()
            instance = newInstance
            newInstance
        }
    }
}