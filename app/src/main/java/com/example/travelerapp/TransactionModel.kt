package com.example.travelerapp

import android.content.Context
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import java.time.Instant

fun createTransaction(
    db: FirebaseFirestore,
    context: Context,
    operation: String,
    amount: Double,
    wallet_id: String,
    trip_id: String? = null,
) {
    val time = Instant.now().toEpochMilli()
    val dbHandler: DBHandler = DBHandler(context)
    var tripName: String? = null
    var user_id: String? = null
    var agencyName: String? = null

    if (trip_id != null) {
        db.collection("trips").document(trip_id)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    user_id = document.getString("user_id")
                    tripName = document.getString("tripName")
                }
            }
    }

    if (user_id != null) {
        db.collection("users").document(user_id!!)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    agencyName = document.getString("username")
                }
            }
    }

    val remarks = if (trip_id != null) tripName else "$operation"
    val description = if (trip_id != null) "$agencyName - Booking Fee" else ""
    val newTransaction = hashMapOf(
        "trip_id" to trip_id,
        "wallet_id" to wallet_id,
        "operation" to operation,
        "amount" to amount,
        "status" to "success",
        "remarks" to remarks,
        "description" to description,
        "created_at" to time,
    )
    db.collection("transactions")
        .add(newTransaction)
        .addOnSuccessListener { documentReference ->
            Toast.makeText(
                context,
                "Transaction added to Firestore with ID: ${documentReference.id}",
                Toast.LENGTH_SHORT
            ).show()
            dbHandler.createTransaction(
                documentReference.id,
                wallet_id,
                operation,
                amount,
                "success",
                remarks,
                description,
                time,
                trip_id
            )
        }
        .addOnFailureListener {
            Toast.makeText(context, "Error adding transaction to Firestore", Toast.LENGTH_SHORT)
                .show()
        }
}