package com.example.travelerapp.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.travelerapp.data.Transaction
import com.example.travelerapp.repo.TransactionFirebase
import com.google.firebase.firestore.FirebaseFirestore

class TransactionViewModel : ViewModel() {
    var transaction: Transaction? = null
    private val database = TransactionFirebase()
    fun createTx(
        db: FirebaseFirestore,
        context: Context,
        operation: String,
        amount: String,
        description: String,
        tripName: String? = "null",
        agencyUsername: String? = "null",
        user_id: String,
        trip_id: String? = "null",
    ) {
        database.createTransaction(db,context, operation, amount, description, tripName, agencyUsername, user_id, trip_id)
    }

    fun readTxs(db: FirebaseFirestore, callback: (List<Transaction>) -> Unit) {
        database.readDataFromFirestore(db, callback)
    }

    fun readTx(db: FirebaseFirestore, id: String, callback: (Transaction?) -> Unit) {
        database.readSingleTransactionFromFirestore(db, id, callback)
    }
}