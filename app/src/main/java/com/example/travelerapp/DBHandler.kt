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
                trip_length TEXT NOT NULL,               
                trip_fees REAL NOT NULL,
                trip_deposit REAL NOT NULL,
                trip_desc TEXT NOT NULL,
                trip_dep_date TEXT NOT NULL,
                trip_ret_date TEXT NOT NULL
            )
            """

            val createUserTable = """
            CREATE TABLE users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT NOT NULL,
                email TEXT NOT NULL,
                password TEXT NOT NULL,
                walletPIN INTEGER DEFAULT 000000 NOT NULL,
                is_user INTEGER DEFAULT 1 CHECK (is_user IN (0,1)) NOT NULL
            )
        """
            val createWalletTable = """
                CREATE TABLE wallets (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                available REAL NOT NULL,
                frozen REAL NOT NULL,
                user_id INTEGER,
                FOREIGN KEY (user_id) REFERENCES users(id)
            )
        """
            val createTransactionTable = """
            CREATE TABLE transactions (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                operation TEXT CHECK (operation IN ('top_up', 'subscribe')) NOT NULL,
                amount REAL NOT NULL,
                status TEXT CHECK (status IN ('success', 'pending', 'failed')) NOT NULL,
                remark TEXT,
                description TEXT,
                created_at TIMESTAMP,
                user_id INTEGER,
                FOREIGN KEY (user_id) REFERENCES users(id)
            )
        """
            val createReviewTable = """
            CREATE TABLE reviews (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                rating INTEGER NOT NULL,
                comment TEXT,
                is_public INTEGER CHECK (is_public IN (0,1)),
                created_at TIMESTAMP,
                trip_id INTEGER,
                user_id INTEGER,
                FOREIGN KEY (trip_id) REFERENCES trips(id),
                FOREIGN KEY (user_id) REFERENCES users(id)
            )
        """
            db.execSQL(createTripTable)
            db.execSQL(createUserTable)
            db.execSQL(createWalletTable)
            db.execSQL(createTransactionTable)
            db.execSQL(createReviewTable)
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            db.execSQL("DROP TABLE IF EXISTS TRIP_TABLE")
            db.execSQL("DROP TABLE IF EXISTS users")
            db.execSQL("DROP TABLE IF EXISTS wallets")
            db.execSQL("DROP TABLE IF EXISTS trips")
            db.execSQL("DROP TABLE IF EXISTS transactions")
            db.execSQL("DROP TABLE IF EXISTS reviews")
            onCreate(db)
        }

        fun addNewUser(
            username: String,
            email: String,
            password: String,
            is_user: Int,
        ) {
            val db = this.writableDatabase
            val values = ContentValues().apply {
                put("username", username)
                put("email", email)
                put("password", password)
                put("is_user", is_user)
            }
            db.insert("users", null, values)
            db.close()
        }

        fun deleteAllUser() {
            val db = this.writableDatabase
            db.delete("users", null, null)
            db.close()
        }

        // this method is use to add new course to our sqlite database.
        fun addNewTrip(
            tripName: String,
            tripLength: String,
            tripFees: Double,
            tripDeposit: Double,
            tripDesc: String,
            depDate: String,
            retDate: String,
        ) {
            val db = this.writableDatabase
            val values = ContentValues().apply {
                put("trip_name", tripName)
                put("trip_length",tripLength )
                put("trip_fees", tripFees)
                put("trip_deposit", tripDeposit)
                put("trip_desc", tripDesc)
                put("trip_dep_date", depDate)
                put("trip_ret_date", retDate)
            }
            db.insert("TRIP_TABLE", null, values)
            db.close()
        }

        fun deleteAllTrip() {
            val db = this.writableDatabase
            db.delete("trips", null, null)
            db.close()
        }

        companion object {
            private const val DB_NAME = "travelerDB"
            private const val DB_VERSION = 10
        }
    }
