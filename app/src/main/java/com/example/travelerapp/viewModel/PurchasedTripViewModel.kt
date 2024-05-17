package com.example.travelerapp.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.travelerapp.data.PurchasedTrip
import com.example.travelerapp.repo.PurchasedTripFirebase
import com.google.firebase.firestore.FirebaseFirestore

class PurchasedTripViewModel:ViewModel() {
    var ptTripId: String? = null
    var ptUserId: String? = null
    var numPax: Int = 0
    private val database = PurchasedTripFirebase()

    fun addPTTrip(context: Context,
                  db:FirebaseFirestore,
                  agencyUsername:String,
                  noPax: Int,
                  tripId: String,
                  userId:String){
        database.addPTDataToFirestore(
            context = context,
            db=db,
            agencyUsername = agencyUsername,
            noPax = noPax,
            tripId = tripId,
            userId = userId
        )
    }

    fun readPTTrip(
        db: FirebaseFirestore,
        callback:(List<PurchasedTrip>) -> Unit
        ){
        database.readPTData(
            db = db,
            callback = callback
        )
    }

//    fun getPurchasedTripsByEmail(
//        db: FirebaseFirestore,
//        userEmail: String,
//        purchasedTrips: List<PurchasedTrip>
//    ): List<PurchasedTrip>? {
//        return purchasedTrips.filter{ it.userEmail == userEmail}
//    }
}