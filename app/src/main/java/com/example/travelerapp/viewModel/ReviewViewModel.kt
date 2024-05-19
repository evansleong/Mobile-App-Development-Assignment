package com.example.travelerapp.viewModel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.travelerapp.data.Review
import com.example.travelerapp.repo.TripFirebase
import com.example.travelerapp.data.Trip
import com.example.travelerapp.repo.ReviewFirebase
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.storage
import java.util.UUID

class ReviewViewModel : ViewModel() {
    var tripPurchasedId: List<String>? = null
    var review: Review? = null
    private val database = ReviewFirebase()
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
        database.saveReview(db,context,trip_name, title, rating, comment, imageUris, is_public, trip_id, user_id, id, created_at, action, callback)
    }

    fun readReviews(db: FirebaseFirestore, callback: (List<Review>) -> Unit) {
        database.readDataFromFirestore(db, callback)
    }

    fun readReview(db: FirebaseFirestore, id: String, callback: (Review?) -> Unit) {
        database.readSingleReviewFromFirestore(db, id, callback)
    }

    fun uploadImage(context: Context, imageUri: Uri?, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
//        for (imageUri in imageUris) {
            if (imageUri != null) {
                val storageRef = Firebase.storage.reference
                val imageRef = storageRef.child("reviewImage/${UUID.randomUUID()}")
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
//        }
    }

    fun deleteReview(db: FirebaseFirestore, reviewId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit){
        database.deleteReviewFromFirestore(
            db = db,
            reviewId = reviewId,
            onSuccess = onSuccess,
            onFailure = onFailure
        )
    }
}