package com.example.travelerapp.repo

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.travelerapp.data.Trip
import com.google.firebase.firestore.FirebaseFirestore

class TripFirebase {
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
        isAvailable: Int,
        noOfUserBooked: Int,
        agencyUsername: String,
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
            "isAvailable" to isAvailable,
            "noOfUserBooked" to noOfUserBooked,
            "agencyUsername" to agencyUsername,
//        "isActive" to isChecked
        )

        db.collection("trips")
            .document(tripId)
            .set(tripData)
            .addOnSuccessListener {
                Log.d("Firestore", "Document added with ID: $tripId")
                Toast.makeText(
                    context,
                    "Trip added to Firestore with ID: $tripId",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error adding document", e)
                Toast.makeText(context, "Error adding trip to Firestore", Toast.LENGTH_SHORT).show()
            }
    }

    fun editTripInFirestore(
        context: Context,
        db: FirebaseFirestore,
        tripId: String,
        newImageUri: String,
        newTripName: String,
        newTripLength: String,
        newTripFees: Double,
        newTripDesc: String,
        newDeptDate: String,
        newRetDate: String,
        newOptions: List<String>
    ) {
        val tripRef = db.collection("trips").document(tripId)

        val newData = hashMapOf(
            "tripUri" to newImageUri,
            "tripName" to newTripName,
            "tripLength" to newTripLength,
            "tripFees" to newTripFees,
            "tripDesc" to newTripDesc,
            "depDate" to newDeptDate,
            "retDate" to newRetDate,
            "options" to newOptions
        )

        tripRef
            .update(newData)
            .addOnSuccessListener {
                Log.d("Firestore", "Trip edited successfully")
                Toast.makeText(context, "Trip edited successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error editing trip: ${e.message}", e)
                Toast.makeText(context, "Error editing trip", Toast.LENGTH_SHORT).show()
            }
    }

    fun deleteTripFromFirestore(
        db: FirebaseFirestore,
        tripId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
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

    fun updateAvailableAmount(
        db: FirebaseFirestore,
        available: Int,
        numPax: Int,
        tripId: String,
        callback: (Boolean) -> Unit
    ){
        val balance: Int = available - numPax
        val numBooked: Int = numPax
        db.collection("trips")
            .document(tripId)
            .update(
                mapOf(
                    "isAvailable" to balance,
                    "noOfUserBooked" to numBooked
                )
            )
            .addOnSuccessListener { documentSnapshot ->
                callback(true) // Success, document updated
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error getting document: ${e.message}", e)
                callback(false)
            }
    }

    fun readSingleTripFromFirestore(
        db: FirebaseFirestore,
        tripId: String,
        callback: (Trip?) -> Unit
    ) {
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

    fun readMultipleTripFromFirestore(
        db: FirebaseFirestore,
        tripIds: List<String>,
        callback: (List<Trip?>) -> Unit
    ) {
        val trips = mutableListOf<Trip?>()
        val totalTrips = tripIds.size
        var tripsRetrieved = 0

        for (tripId in tripIds) {
            db.collection("trips")
                .document(tripId)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        try {
                            val trip: Trip? = documentSnapshot.toObject(Trip::class.java)
                            trips.add(trip)
                        } catch (e: Exception) {
                            Log.e("Firestore", "Error converting document to Trip: ${e.message}")
                            trips.add(null)
                        }
                    } else {
                        Log.e("Firestore", "Document does not exist for tripId: $tripId")
                        trips.add(null)
                    }
                    tripsRetrieved++
                    if (tripsRetrieved == totalTrips) {
                        callback(trips)
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("Firestore", "Error getting document: ${e.message}", e)
                    tripsRetrieved++
                    if (tripsRetrieved == totalTrips) {
                        callback(trips)
                    }
                }
        }
    }

    fun addPurchasedTrip(
        db: FirebaseFirestore,
        context: Context,
        tripId: String,
        agencyUsername: String,
        noPax: Int,
    ) {
        val tripData = hashMapOf(
            "tripId" to tripId,
            "agencyUsername" to agencyUsername,
            "noPax" to noPax
        )

        db.collection("purchasedTrips")
            .add(tripData)
            .addOnSuccessListener {
                Log.d("Firestore", "Document added to purchasedTrips with ID: $it.id")
                Toast.makeText(context, "PurchasedTrips added to Firestore with ID: $it.id", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error adding document", e)
                Toast.makeText(context, "Error adding purchasedTrips to Firestore", Toast.LENGTH_SHORT).show()
            }
    }

    // Function to read the total number of purchased trips
    fun readPurchasedTrips(
        db: FirebaseFirestore,
        agencyUsername: String,
        callback: (Int) -> Unit
    ) {
        db.collection("purchasedTrips")
            .whereEqualTo("agencyUsername", agencyUsername)
            .get()
            .addOnSuccessListener { documents ->
                var totalNoPax = 0 // Initialize the total number of passengers
                for (document in documents) {
                    val noPax = document.getLong("noPax")?.toInt() ?: 0
                    totalNoPax += noPax // Add the number of passengers to the total
                }
                callback(totalNoPax) // Pass the total number of passengers to the callback function
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error getting purchasedTrips: ${e.message}", e)
                callback(0) // If there's an error, pass 0 as the total number of passengers
            }
    }

}

