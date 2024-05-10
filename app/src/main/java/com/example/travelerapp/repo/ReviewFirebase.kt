package com.example.travelerapp.repo

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.example.travelerapp.DBHandler
import com.example.travelerapp.createTransaction
import com.example.travelerapp.data.Review
import com.example.travelerapp.data.Trip
import com.example.travelerapp.data.Wallet
import com.google.firebase.firestore.FirebaseFirestore
import java.time.Instant

class ReviewFirebase {
    fun saveReview(
        db: FirebaseFirestore,
        context: Context,
        trip_name: String,
        title: String,
        rating: Int,
        comment: String,
        imageUris: List<Uri>,
        is_public: Int,
        trip_id: String? = null,
        user_id: String? = null,
    ) {
        val time = Instant.now().toEpochMilli()
        val imageUrls = mutableListOf<String>()
        for (uri in imageUris) {
            imageUrls.add(uri.toString())
        }

        val newReview = hashMapOf(
            "trip_id" to trip_id,
            "user_id" to user_id,
            "trip_name" to trip_name,
            "title" to title,
            "rating" to rating,
            "comment" to comment,
            "is_public" to is_public,
            "imageUrls" to imageUrls,
            "created_at" to time,
        )
        db.collection("reviews")
            .whereEqualTo("trip_id", trip_id)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val reviewDoc =
                        querySnapshot.documents[0]
                    val reviewId = reviewDoc.id

                    db.collection("reviews").document(reviewId)
                        .update(newReview)
                        .addOnSuccessListener {
                            Toast.makeText(context, "Review updated successfully", Toast.LENGTH_SHORT)
                                .show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(context, "Error updating review: $it", Toast.LENGTH_SHORT)
                                .show()
                        }
                } else {
                    db.collection("reviews")
                        .add(newReview)
                        .addOnSuccessListener { documentReference ->
                            Toast.makeText(
                                context,
                                "Review added to Firestore with ID: ${documentReference.id}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(
                                context,
                                "Error adding review to Firestore",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
            }
            .addOnFailureListener {
                Toast.makeText(context, "Error querying reviews: $it", Toast.LENGTH_SHORT).show()
            }
    }

    fun readDataFromFirestore(db: FirebaseFirestore, callback: (List<Review>) -> Unit) {
        db.collection("reviews")
            .get()
            .addOnSuccessListener { documents ->
                val reviews = mutableListOf<Review>()
                for (document in documents) {
                    try {
                        val review: Review = document.toObject(Review::class.java)
                        reviews.add(review)
                    } catch (e: Exception) {
                        Log.e("Firestore", "Error converting document to Review: ${e.message}")
                    }
                }
                callback(reviews)
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error getting documents: ${e.message}", e)
            }
    }

    fun readSingleReviewFromFirestore(
        db: FirebaseFirestore,
        id: String,
        callback: (Review?) -> Unit
    ) {
        db.collection("reviews")
            .document(id)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    try {
                        val review: Review? = documentSnapshot.toObject(Review::class.java)
                        callback(review)
                    } catch (e: Exception) {
                        Log.e("Firestore", "Error converting document to Trip: ${e.message}")
                        callback(null)
                    }
                } else {
                    Log.e("Firestore", "Document does not exist for tripId: $id")
                    callback(null)
                }
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error getting document: ${e.message}", e)
                callback(null)
            }
    }
}