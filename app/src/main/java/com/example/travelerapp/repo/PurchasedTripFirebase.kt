package com.example.travelerapp.repo

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.travelerapp.data.PurchasedTrip
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.toObjects

class PurchasedTripFirebase {
    fun addPTDataToFirestore(
        context: Context,
        db: FirebaseFirestore,
        agencyUsername: String,
        noPax: Int,
        tripId: String,
        userId: String
    ){
        val ptData = hashMapOf(
            "agencyUsername" to agencyUsername,
            "noPax" to noPax,
            "tripId" to tripId,
            "userId" to userId
        )

        db.collection("purchasedTrips")
            .add(ptData)
            .addOnSuccessListener {
                Log.d("Firestore","Document added $agencyUsername , $tripId bought by $userId")
                Toast.makeText(context, "$agencyUsername successfully purchased $tripId",Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.e("Firestore","error adding document",e)
                Toast.makeText(context,
                    "Error purchasing trip",Toast.LENGTH_SHORT).show()
            }
    }

    fun readPTData(db:FirebaseFirestore, callback: (List<PurchasedTrip>) -> Unit){
        db.collection("purchasedTrips")
            .get()
            .addOnSuccessListener { documents ->
                val purchasedTrips = mutableListOf<PurchasedTrip>()
                for(document in documents) {
                    try{
                        val purchasedTrip: PurchasedTrip = document.toObject(PurchasedTrip::class.java)
                        purchasedTrips.add(purchasedTrip)
                    }catch(e:Exception){
                        Log.e("Firestore","Error converting document to PT: ${e.message}")
                    }
                }
                callback(purchasedTrips)
            }
            .addOnFailureListener { e ->
                Log.e("Firestore","Error getting docs: ${e.message}",e)
            }
    }


}