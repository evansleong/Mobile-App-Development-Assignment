package com.example.travelerapp.viewModel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.travelerapp.data.PieChartInput
import com.example.travelerapp.repo.TripFirebase
import com.example.travelerapp.data.Trip
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.storage
import java.util.UUID

class TripViewModel : ViewModel() {
    var selectedTripId: String? = null
    var numPax: Int = 0
    private val database = TripFirebase()
    fun addTrip(context: Context, db: FirebaseFirestore, tripId: String, tripPackageName: String, tripLength: String, tripPackageFees: Double, tripPackageDeposit: Double, tripPackageDesc: String, tripPackageDeptDate: String, tripPackageRetDate: String, uploadedImageUri: String?, selectedOption: List<String>, isAvailable: Int, noOfUserBooked: Int, agencyUsername: String, onSuccess: () -> Unit){
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
            isAvailable = isAvailable,
            noOfUserBooked = noOfUserBooked,
            agencyUsername = agencyUsername,
            onSuccess = onSuccess
        )
    }

    fun readTrip(db: FirebaseFirestore, callback: (List<Trip>) -> Unit){
        database.readDataFromFirestore(
            db = db,
            callback = callback
        )
    }

    fun editTrip(context: Context, db: FirebaseFirestore, tripId: String, newTripUri: String,  newTripName: String, newTripLength: String, newTripFees: Double, newTripDesc: String, newDeptDate: String, newRetDate: String, newOptions: List<String>){
        database.editTripInFirestore(
            context = context,
            db = db,
            tripId = tripId,
            newImageUri = newTripUri,
            newTripName = newTripName,
            newTripLength = newTripLength,
            newTripFees = newTripFees,
            newTripDesc = newTripDesc,
            newDeptDate = newDeptDate,
            newRetDate = newRetDate,
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

    fun updateAvailable(db: FirebaseFirestore, available: Int, numPax: Int, tripId: String, callback: (Boolean) -> Unit) {
        database.updateAvailableAmount(db, available, numPax, tripId, callback)
    }

    fun readSingleTrip(db: FirebaseFirestore, tripId: String, callback: (Trip?) -> Unit){
        database.readSingleTripFromFirestore(
            db = db,
            tripId = tripId,
            callback = callback
        )
    }

    fun readMultipleTrips(db: FirebaseFirestore, tripId: List<String>, callback: (List<Trip?>) -> Unit) {
        database.readMultipleTripFromFirestore(db, tripId, callback)
    }

    fun addPurchasedTrip(
        db: FirebaseFirestore,
        context: Context,
        tripId: String,
        agencyUsername: String,
        noPax: Int,
    ){
        database.addPurchasedTrip(db, context, tripId, agencyUsername, noPax)
    }

    fun readPurchasedTrips(db: FirebaseFirestore, agencyUsername: String, callback: (Int) -> Unit) {
        database.readPurchasedTrips(db, agencyUsername, callback)
    }

//    fun readPurchasedTripsForPieChart(db: FirebaseFirestore, agencyUsername: String, callback: (List<PieChartInput>) -> Unit) {
//        database.readPurchasedTripsForPieChart(db, agencyUsername,callback)
//    }
}