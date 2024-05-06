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
    agencyUsername: String
//    isChecked: Boolean
) {

    val tripData = hashMapOf(
        "tripName" to tripPackageName,
        "tripLength" to tripLength,
        "tripFees" to tripPackageFees,
        "tripDeposit" to tripPackageDeposit,
        "tripDesc" to tripPackageDesc,
        "depDate" to tripPackageDeptDate,
        "retDate" to tripPackageRetDate,
        "tripUri" to uploadedImageUri,
        "options" to selectedOption,
        "agencyUsername" to agencyUsername
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
                try {
                    val trip: Trip = document.toObject(Trip::class.java)
                    trips.add(trip)
                } catch (e: Exception) {
                    Log.e("Firestore", "Error converting document to Trip: ${e.message}")
                }
            }
            callback(trips)
        }
        .addOnFailureListener { e ->
            Log.e("Firestore", "Error getting documents: ${e.message}", e)
        }
}
