package com.example.travelerapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.health.connect.datatypes.units.Length
import com.example.travelerapp.data.Trip

class DBHandler(context: Context) :
        SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
        override fun onCreate(db: SQLiteDatabase) {
            val createTripTable = """
            CREATE TABLE TRIP_TABLE (
                trip_id INTEGER PRIMARY KEY AUTOINCREMENT,
                trip_name TEXT NOT NULL,
                trip_length TEXT NOT NULL,
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
        tripLength: String,
        tripFees: Double,
        tripDeposit: Double,
        tripDesc: String,
    ) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("TRIP_NAME", tripName)
            put("TRIP_LENGTH", tripLength)
            put("TRIP_FEES", tripFees)
            put("TRIP_DEPOSIT", tripDeposit)
            put("TRIP_DESC", tripDesc)
        }
        db.insert("TRIP_TABLE", null, values)
        db.close()
    }
    fun getAllTrips(): List<Trip> {
        val tripList: ArrayList<Trip> = ArrayList()
        val db = this.readableDatabase
        val cursorTrips: Cursor = db.rawQuery("SELECT * FROM TRIP_TABLE", null)
        if (cursorTrips.moveToFirst()){
            do {
                tripList.add(
                    Trip(
                        tripName = cursorTrips.getString(1),
                        tripLength = cursorTrips.getString(2),
                        tripFees = cursorTrips.getDouble(3),
                        tripDeposit = cursorTrips.getDouble(4),
                        tripDesc = cursorTrips.getString(5)
                    )
                )
            }while (cursorTrips.moveToNext())

        }
        cursorTrips.close()
        db.close()
        return tripList
    }

        fun deleteAllTrip() {
            val db = this.writableDatabase
            db.delete("TRIP_TABLE", null, null)
            db.close()
        }
        companion object {
        private const val DB_NAME = "travelerDB"
        private const val DB_VERSION = 3
    }
}