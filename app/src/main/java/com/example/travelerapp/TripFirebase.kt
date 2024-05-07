package com.example.travelerapp

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.example.travelerapp.data.Trip
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.storage
import java.util.UUID

fun addDataToFirestore(
    context: Context,
    db: FirebaseFirestore,
    tripId: String,
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
        "tripId" to tripId,
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
        .document(tripId)
        .set(tripData)
        .addOnSuccessListener {
            Log.d("Firestore", "Document added with ID: $tripId")
            Toast.makeText(context, "Trip added to Firestore with ID: $tripId", Toast.LENGTH_SHORT).show()
        }
        .addOnFailureListener { e ->
            Log.e("Firestore", "Error adding document", e)
            Toast.makeText(context, "Error adding trip to Firestore", Toast.LENGTH_SHORT).show()
        }
}

fun deleteTripFromFirestore(db: FirebaseFirestore, tripId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
    db.collection("trips")
        .document(tripId)
        .delete()
        .addOnSuccessListener {
            onSuccess()
        }
        .addOnFailureListener { e ->
            onFailure(e)
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

fun readSingleTripFromFirestore(db: FirebaseFirestore, tripId: String, callback: (Trip?) -> Unit) {
    db.collection("trips")
        .document(tripId)
        .get()
        .addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                try {
                    val trip: Trip? = documentSnapshot.toObject(Trip::class.java)
                    callback(trip)
                } catch (e: Exception) {
                    Log.e("Firestore", "Error converting document to Trip: ${e.message}")
                    callback(null)
                }
            } else {
                Log.e("Firestore", "Document does not exist for tripId: $tripId")
                callback(null)
            }
        }
        .addOnFailureListener { e ->
            Log.e("Firestore", "Error getting document: ${e.message}", e)
            callback(null)
        }
}

fun uploadImageToFirebaseStorage(context: Context, imageUri: Uri?, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
    if (imageUri != null) {
        val storageRef = Firebase.storage.reference
        val imageRef = storageRef.child("images/${UUID.randomUUID()}")
        val uploadTask = imageRef.putFile(imageUri)

        uploadTask.addOnSuccessListener { taskSnapshot ->
            // Image uploaded successfully
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                // Get the download URL
                onSuccess(uri.toString())
            }.addOnFailureListener { exception ->
                onFailure(exception)
            }
        }.addOnFailureListener { exception ->
            onFailure(exception)
        }
    }
}

