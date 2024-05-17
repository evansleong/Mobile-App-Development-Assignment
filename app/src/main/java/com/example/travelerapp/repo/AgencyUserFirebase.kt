    package com.example.travelerapp.repo

    import android.content.Context
    import android.util.Log
    import android.widget.Toast
    import com.example.travelerapp.data.AgencyUser
    import com.example.travelerapp.data.Trip
    import com.google.firebase.firestore.FirebaseFirestore

    class AgencyUserFirebase {
        fun addDataToFirestore(
            context: Context,
            db: FirebaseFirestore,
            agencyId: String,
            agencyUsername: String,
            agencyEmail: String,
            agencyPassword: String,
            agencyPicture: String?,
        ) {

            val agencyData = hashMapOf(
                "agencyId" to agencyId,
                "agencyUsername" to agencyUsername,
                "agencyEmail" to agencyEmail,
                "agencyPassword" to agencyPassword,
                "agencyPicture" to agencyPicture
            )

            db.collection("agencyUser")
                .document(agencyId)
                .set(agencyData)
                .addOnSuccessListener {
                    Log.d("Firestore", "Agency added with ID: $agencyId")
                    Toast.makeText(
                        context,
                        "Agency added to Firestore with ID: $agencyId",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .addOnFailureListener { e ->
                    Log.e("Firestore", "Error adding document", e)
                    Toast.makeText(context, "Error adding agency to Firestore", Toast.LENGTH_SHORT)
                        .show()
                }
        }

        fun readAgencyDataFromFirestore(
            db: FirebaseFirestore,
            callback: (List<AgencyUser>) -> Unit
        ) {
            db.collection("agencyUser")
                .get()
                .addOnSuccessListener { documents ->
                    val agencies = mutableListOf<AgencyUser>()
                    for (document in documents) {
                        try {
                            val agencyuser: AgencyUser = document.toObject(AgencyUser::class.java)
                            agencies.add(agencyuser)
                        } catch (e: Exception) {
                            Log.e(
                                "Firestore",
                                "Error converting document to AgencyUser: ${e.message}"
                            )
                        }
                    }
                    callback(agencies)
                }
                .addOnFailureListener { e ->
                    Log.e("Firestore", "Error getting documents: ${e.message}", e)
                }
        }

        fun readSingleAgencyFromFirestore(
            db: FirebaseFirestore,
            agencyId: String,
            callback: (AgencyUser?) -> Unit
        ) {
            db.collection("agencyUser")
                .document(agencyId)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        try {
                            val agency: AgencyUser? = documentSnapshot.toObject(AgencyUser::class.java)
                            callback(agency)
                        } catch (e: Exception) {
                            Log.e("Firestore", "Error converting document to Trip: ${e.message}")
                            callback(null)
                        }
                    } else {
                        Log.e("Firestore", "Document does not exist for tripId: $agencyId")
                        callback(null)
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("Firestore", "Error getting document: ${e.message}", e)
                    callback(null)
                }
        }

        fun editAgencyProfilePicture(
            context: Context,
            db: FirebaseFirestore,
            agencyId: String,
            newAgencyPicture: String,
        ) {
            val agencyRef = db.collection("agencyUser").document(agencyId)

            val newData = hashMapOf<String, Any>(
                "agencyPicture" to newAgencyPicture,
            )

            agencyRef
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

        fun fetchAgencyPicture(agencyId: String, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
            val firestore = FirebaseFirestore.getInstance()
            firestore.collection("agencies")
                .document(agencyId)
                .get()
                .addOnSuccessListener { document ->
                    val pictureUrl = document.getString("agencyPicture")
                    if (!pictureUrl.isNullOrEmpty()) {
                        onSuccess(pictureUrl)
                    }
                }
                .addOnFailureListener { exception ->
                    onFailure(exception)
                }
        }


    }



