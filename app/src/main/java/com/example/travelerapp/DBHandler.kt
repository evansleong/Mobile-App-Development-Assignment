package com.example.travelerapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

    class DBHandler(context: Context) :
        SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
        override fun onCreate(db: SQLiteDatabase) {
            val createTripTable = """
            CREATE TABLE TRIP_TABLE (
                trip_id INTEGER PRIMARY KEY AUTOINCREMENT,
                trip_name TEXT NOT NULL,
                trip_fees REAL NOT NULL,
                trip_deposit REAL NOT NULL,
                trip_desc TEXT NOT NULL
            )
        """
            db.execSQL(createTripTable)
        }
        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            db.execSQL("DROP TABLE IF EXISTS TRIP_TABLE")
            onCreate(db)
        }
        // this method is use to add new course to our sqlite database.
    fun addNewTrip(
        tripName: String,
        tripFees: Double,
        tripDeposit: Double,
        tripDesc: String,
    ) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("TRIP_NAME", tripName)
            put("TRIP_FEES", tripFees)
            put("TRIP_DEPOSIT", tripDeposit)
            put("TRIP_DESC", tripDesc)
        }
        db.insert("TRIP_TABLE", null, values)
        db.close()
    }
        fun deleteAllTrip() {
            val db = this.writableDatabase
            db.delete("TRIP_TABLE", null, null)
            db.close()
        }
        companion object {
        private const val DB_NAME = "travelerDB"
        private const val DB_VERSION = 2
    }
}