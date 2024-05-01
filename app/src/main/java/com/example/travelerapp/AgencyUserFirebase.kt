    package com.example.travelerapp


    import android.content.Context
    import android.util.Log
    import android.widget.Toast
    import com.example.travelerapp.data.AgencyUser
    import com.example.travelerapp.data.Trip
    import com.google.firebase.firestore.FirebaseFirestore

    fun addDataToFirestore(
        context: Context,
        db: FirebaseFirestore,
        agencyUsername: String,
        agencyEmail: String,
        agencyPassword: String
    ) {

        val agencyData = hashMapOf(
            "agencyUsername" to agencyUsername,
            "agencyEmail" to agencyEmail,
            "agencyPassword" to agencyPassword
        )

        db.collection("agencyUser")
            .add(agencyData)
            .addOnSuccessListener { documentReference ->
                Log.d("Firestore", "Agency added with ID: ${documentReference.id}")
                Toast.makeText(context, "Agency added to Firestore with ID: ${documentReference.id}", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error adding document", e)
                Toast.makeText(context, "Error adding agency to Firestore", Toast.LENGTH_SHORT).show()
            }
    }

    fun readAgencyDataFromFirestore(db: FirebaseFirestore, callback: (List<AgencyUser>) -> Unit) {
        db.collection("agencyUser")
            .get()
            .addOnSuccessListener { documents ->
                val agencies = mutableListOf<AgencyUser>()
                for (document in documents) {
                    try {
                        val agencyuser: AgencyUser = document.toObject(AgencyUser::class.java)
                        agencies.add(agencyuser)
                    } catch (e: Exception) {
                        Log.e("Firestore", "Error converting document to AgencyUser: ${e.message}")
                    }
                }
                callback(agencies)
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error getting documents: ${e.message}", e)
            }
    }



