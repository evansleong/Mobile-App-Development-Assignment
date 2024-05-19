package com.example.travelerapp.repo

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.example.travelerapp.data.Review
import com.google.firebase.firestore.FirebaseFirestore

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
        trip_id: String? = "null",
        user_id: String? = "null",
        id: String = "null",
        created_at: Long = 0L,
        action: String,
        callback: (String) -> Unit
    ) {
        val time = System.currentTimeMillis()
//        val time = Instant.now().toEpochMilli()
        val imageUrls = mutableListOf<String>()
        for (uri in imageUris) {
            imageUrls.add(uri.toString())
        }
        val imageUrlsString: String = imageUrls.joinToString(",")

        val newReview = hashMapOf(
            "trip_id" to trip_id,
            "user_id" to user_id,
            "trip_name" to trip_name,
            "title" to title,
            "rating" to rating,
            "comment" to comment,
            "is_public" to is_public,
            "imageUrls" to imageUrlsString,
            "created_at" to if (created_at != 0L) created_at else time,
        )
        if (action == "Edit") {
            db.collection("reviews")
                .document(id)
                .update(newReview as Map<String, Any>)
                .addOnSuccessListener {
                    Toast.makeText(context, "Review updated successfully", Toast.LENGTH_SHORT).show()
                    callback(id)
                }
                .addOnFailureListener {e ->
                    Log.e("Firestore", "Error getting documents: ${e.message}", e)
                    callback("null")
                }
        } else {
            db.collection("reviews")
                .add(newReview)
                .addOnSuccessListener { documentReference ->
                    Toast.makeText(context, "Review added to Firestore with ID: ${documentReference.id}", Toast.LENGTH_SHORT).show()
                    callback(documentReference.id)
                }
                .addOnFailureListener { e ->
                    Log.e("Firestore", "Error getting documents: ${e.message}", e)
                    callback("null")
                }
        }
    }

    fun readDataFromFirestore(db: FirebaseFirestore, callback: (List<Review>) -> Unit) {
        db.collection("reviews")
            .orderBy("created_at", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                val reviews = mutableListOf<Review>()
                for (document in documents) {
                    try {
                        val review: Review = document.toObject(Review::class.java)
                        review.id = document.id
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

    fun deleteReviewFromFirestore(
        db: FirebaseFirestore,
        reviewId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection("reviews")
            .document(reviewId)
            .delete()
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }
}