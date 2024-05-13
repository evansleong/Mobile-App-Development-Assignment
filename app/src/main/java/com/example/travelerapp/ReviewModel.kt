package com.example.travelerapp
//
//import android.content.Context
//import android.net.Uri
//import android.widget.Toast
//import com.google.firebase.Firebase
//import com.google.firebase.firestore.FirebaseFirestore
//import com.google.firebase.firestore.firestore
//import java.time.Instant
//
//fun saveReview(
//    db: FirebaseFirestore,
//    context: Context,
//    trip_name: String,
//    title: String,
//    rating: Int,
//    comment: String,
//    imageUris: List<Uri>,
//    is_public: Int,
//    trip_id: String? = null,
//) {
//    val time = Instant.now().toEpochMilli()
//    val dbHandler: DBHandler = DBHandler(context)
//    var trip_id: String? = null
//    var user_id: String? = null
//    var agencyName: String? = null
//    val imageUrls = mutableListOf<String>()
//    for (uri in imageUris) {
//        imageUrls.add(uri.toString())
//    }
//
//    if (trip_id != null) {
//        db.collection("trips").document(trip_id)
//            .get()
//            .addOnSuccessListener { document ->
//                if (document != null && document.exists()) {
//                    user_id = document.getString("user_id")
//                    trip_id = document.getString("tripName")
//                }
//            }
//    }
//
//    if (user_id != null) {
//        db.collection("users").document(user_id!!)
//            .get()
//            .addOnSuccessListener { document ->
//                if (document != null && document.exists()) {
//                    agencyName = document.getString("username")
//                }
//            }
//    }
//    val newReview = hashMapOf(
//        "trip_id" to trip_id,
//        "user_id" to user_id,
//        "trip_name" to trip_name,
//        "title" to title,
//        "rating" to rating,
//        "comment" to comment,
//        "is_public" to is_public,
//        "imageUrls" to imageUrls,
//        "created_at" to time,
//    )
//    db.collection("reviews")
//        .whereEqualTo("trip_id", trip_id)
//        .get()
//        .addOnSuccessListener { querySnapshot ->
//            if (!querySnapshot.isEmpty) {
//                val reviewDoc =
//                    querySnapshot.documents[0] // Assuming there's only one matching review
//                val reviewId = reviewDoc.id // Get the ID of the review
//                val reviewData = reviewDoc.data // Get the data of the review
//
//                db.collection("reviews").document(reviewId)
//                    .update(newReview)
//                    .addOnSuccessListener {
//                        Toast.makeText(context, "Review updated successfully", Toast.LENGTH_SHORT)
//                            .show()
//                        dbHandler.saveReview(
//                            reviewId,
//                            agencyName.toString(),
//                            title,
//                            rating,
//                            comment,
//                            imageUrls,
//                            is_public,
//                            time
//                        )
//                    }
//                    .addOnFailureListener {
//                        Toast.makeText(context, "Error updating review: $it", Toast.LENGTH_SHORT)
//                            .show()
//                    }
//            } else {
//                db.collection("reviews")
//                    .add(newReview)
//                    .addOnSuccessListener { documentReference ->
//                        Toast.makeText(
//                            context,
//                            "Review added to Firestore with ID: ${documentReference.id}",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                        dbHandler.saveReview(
//                            documentReference.id,
//                            agencyName.toString(),
//                            title,
//                            rating,
//                            comment,
//                            imageUrls,
//                            is_public,
//                            time
//                        )
//                    }
//                    .addOnFailureListener {
//                        Toast.makeText(
//                            context,
//                            "Error adding review to Firestore",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//            }
//        }
//        .addOnFailureListener {
//            Toast.makeText(context, "Error querying reviews: $it", Toast.LENGTH_SHORT).show()
//        }
//}