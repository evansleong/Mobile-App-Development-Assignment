package com.example.travelerapp.repo

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.example.travelerapp.DBHandler
import com.example.travelerapp.createTransaction
import com.example.travelerapp.data.Review
import com.example.travelerapp.data.Transaction
import com.example.travelerapp.data.Trip
import com.example.travelerapp.data.Wallet
import com.google.firebase.firestore.FirebaseFirestore
import java.time.Instant

class TransactionFirebase {
    fun createTransaction(
        db: FirebaseFirestore,
        context: Context,
        operation: String,
        amount: String,
        tripName: String? = "null",
        agencyUsername: String? = "null",
        user_id: String,
        trip_id: String? = "null",
    ) {
        val time = Instant.now().toEpochMilli()

        val remarks = if (trip_id != "null") tripName else operation
        val description = if (trip_id != "null") "$agencyUsername - Booking Fee" else ""
        val newTransaction = hashMapOf(
            "trip_id" to trip_id,
            "user_id" to user_id,
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
            }
            .addOnFailureListener {
                Toast.makeText(context, "Error adding transaction to Firestore", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    fun readDataFromFirestore(db: FirebaseFirestore, callback: (List<Transaction>) -> Unit) {
        db.collection("transactions")
            .get()
            .addOnSuccessListener { documents ->
                val transactions = mutableListOf<Transaction>()
                for (document in documents) {
                    try {
                        val transaction: Transaction = document.toObject(Transaction::class.java)
                        transaction.id = document.id
                        transactions.add(transaction)
                    } catch (e: Exception) {
                        Log.e("Firestore", "Error converting document to Review: ${e.message}")
                    }
                }
                callback(transactions)
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error getting documents: ${e.message}", e)
            }
    }

    fun readSingleTransactionFromFirestore(
        db: FirebaseFirestore,
        id: String,
        callback: (Transaction?) -> Unit
    ) {
        db.collection("transactions")
            .document(id)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    try {
                        val transaction: Transaction? = documentSnapshot.toObject(Transaction::class.java)
                        callback(transaction)
                    } catch (e: Exception) {
                        Log.e("Firestore", "Error converting document to Trip: ${e.message}")
                        callback(null)
                    }
                } else {
                    Log.e("Firestore", "Document does not exist for tripId: $id")
                    callback(null)
                }
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error getting document: ${e.message}", e)
                callback(null)
            }
    }
}