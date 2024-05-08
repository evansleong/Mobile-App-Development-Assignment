package com.example.travelerapp.viewModel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.travelerapp.repo.TripFirebase
import com.example.travelerapp.data.Trip
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.storage
import java.util.UUID

class TripViewModel : ViewModel() {
    var selectedTripId: String? = null
    private val database = TripFirebase()
    fun addTrip(context: Context, db: FirebaseFirestore, tripId: String, tripPackageName: String, tripLength: String, tripPackageFees: Double, tripPackageDeposit: Double, tripPackageDesc: String, tripPackageDeptDate: String, tripPackageRetDate: String, uploadedImageUri: String?, selectedOption: List<String>, agencyUsername: String){
        database.addDataToFirestore(
            context = context,
            db = db,
            tripId = tripId,
            tripPackageName = tripPackageName,
            tripLength = tripLength,
            tripPackageFees = tripPackageFees,
            tripPackageDeposit = tripPackageDeposit,
            tripPackageDesc = tripPackageDesc,
            tripPackageDeptDate = tripPackageDeptDate,
            tripPackageRetDate = tripPackageRetDate,
            uploadedImageUri = uploadedImageUri,
            selectedOption = selectedOption,
            agencyUsername = agencyUsername
        )
    }

    fun readTrip(db: FirebaseFirestore, callback: (List<Trip>) -> Unit){
        database.readDataFromFirestore(
            db = db,
            callback = callback
        )
    }

    fun editTrip(context: Context, db: FirebaseFirestore, tripId: String, newTripName: String, newTripLength: String, newTripFees: Double, newTripDesc: String, newOptions: List<String>){
        database.editTripInFirestore(
            context = context,
            db = db,
            tripId = tripId,
            newTripName = newTripName,
            newTripLength = newTripLength,
            newTripFees = newTripFees,
            newTripDesc = newTripDesc,
            newOptions = newOptions
        )
    }

    fun deleteTrip(db: FirebaseFirestore, tripId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit){
        database.deleteTripFromFirestore(
            db = db,
            tripId = tripId,
            onSuccess = onSuccess,
            onFailure = onFailure
        )
    }

    fun uploadImage(context: Context, imageUri: Uri?, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
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

    fun readSingleTrip(db: FirebaseFirestore, tripId: String, callback: (Trip?) -> Unit){
        database.readSingleTripFromFirestore(
            db = db,
            tripId = tripId,
            callback = callback
        )
    }
}