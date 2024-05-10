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
        trip_id: String? = null,
    ) {
        database.saveReview(db,context,trip_name, title, rating, comment, imageUris, is_public, trip_id)
    }

    fun readReviews(db: FirebaseFirestore, callback: (List<Review>) -> Unit) {
        database.readDataFromFirestore(db, callback)
    }

    fun readReview(db: FirebaseFirestore, id: String, callback: (Review?) -> Unit) {
        database.readSingleReviewFromFirestore(db, id, callback)
    }
}