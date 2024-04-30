package com.example.travelerapp

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.travelerapp.data.Trip
import com.google.firebase.firestore.FirebaseFirestore

fun addDataToFirestore(
    context: Context,
    db: FirebaseFirestore,
    tripPackageName: String,
    tripLength: String,
    tripPackageFees: Double,
    tripPackageDeposit: Double,
    tripPackageDesc: String,
    tripPackageDeptDate: String,
    tripPackageRetDate: String,
    uploadedImageUri: String?,
    selectedOption: List<String>,
//    isChecked: Boolean
) {
    val tripData = hashMapOf(
        "packageName" to tripPackageName,
        "tripLength" to tripLength,
        "fees" to tripPackageFees,
        "deposit" to tripPackageDeposit,
        "description" to tripPackageDesc,
        "deptDate" to tripPackageDeptDate,
        "retDate" to tripPackageRetDate,
        "imageUri" to uploadedImageUri,
        "options" to selectedOption,
//        "isActive" to isChecked
    )

    db.collection("trips")
        .add(tripData)
        .addOnSuccessListener { documentReference ->
            Log.d("Firestore", "Document added with ID: ${documentReference.id}")
            Toast.makeText(context, "Trip added to Firestore with ID: ${documentReference.id}", Toast.LENGTH_SHORT).show()
        }
        .addOnFailureListener { e ->
            Log.e("Firestore", "Error adding document", e)
            Toast.makeText(context, "Error adding trip to Firestore", Toast.LENGTH_SHORT).show()
        }
}

// Function to read data from Firestore
fun readDataFromFirestore(db: FirebaseFirestore, callback: (List<Trip>) -> Unit) {
    db.collection("trips")
        .get()
        .addOnSuccessListener { documents ->
            val trips = mutableListOf<Trip>()
            for (document in documents) {
                val trip = document.toObject(Trip::class.java)
                trips.add(trip)
            }
            callback(trips)
        }
        .addOnFailureListener { e ->
            Log.e("Firestore", "Error getting documents", e)
        }
}