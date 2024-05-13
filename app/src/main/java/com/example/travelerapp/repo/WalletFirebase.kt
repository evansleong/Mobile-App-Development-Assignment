package com.example.travelerapp.repo

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.travelerapp.DBHandler
import com.example.travelerapp.data.Wallet
import com.google.firebase.firestore.FirebaseFirestore

private const val ANDROID_KEY_STORE = "AndroidKeyStore"
private const val AES_MODE = "AES/GCM/NoPadding"
private const val KEY_ALIAS = "WalletPin"
class WalletFirebase {
    fun createWallet(db: FirebaseFirestore, context: Context, user_id: String, callback: (String) -> Unit) {
        val dbHandler: DBHandler = DBHandler(context)
        val newWallet = hashMapOf(
            "user_id" to user_id,
            "available" to "0",
            "frozen" to "0",
            "walletPin" to "null",
        )
        db.collection("wallets")
            .add(newWallet)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(
                    context,
                    "Wallet added to Firestore with ID: ${documentReference.id}",
                    Toast.LENGTH_SHORT
                ).show()
                callback(documentReference.id)
            }
            .addOnFailureListener {
                Toast.makeText(context, "Error adding wallet to Firestore", Toast.LENGTH_SHORT)
                    .show()
                callback("null")
            }
    }

    fun readDataFromFirestore(db: FirebaseFirestore, callback: (List<Wallet>) -> Unit) {
        db.collection("wallets")
            .get()
            .addOnSuccessListener { documents ->
                val wallets = mutableListOf<Wallet>()
                for (document in documents) {
                    try {
                        val wallet: Wallet = document.toObject(Wallet::class.java)
                        wallets.add(wallet)
                    } catch (e: Exception) {
                        Log.e("Firestore", "Error converting document to Wallet: ${e.message}")
                    }
                }
                callback(wallets)
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error getting documents: ${e.message}", e)
            }
    }


    fun updateWalletPIN(db: FirebaseFirestore, context: Context, pin: String, user_id: String?, callback: (String) -> Unit) {
        val collectionRef = db.collection("wallets")

        collectionRef.whereEqualTo("user_id", user_id)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val documentId = document.id
                    collectionRef.document(documentId)
                        .update("walletPin", pin)
                        .addOnSuccessListener {
                            Toast.makeText(
                                context,
                                "WalletPin updated for document with ID: $documentId",
                                Toast.LENGTH_SHORT
                            ).show()
                            callback(user_id.toString())
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(
                                context,
                                "Error updating walletPin for document with ID: $documentId: $e",
                                Toast.LENGTH_SHORT
                            ).show()
                            callback("null")
                        }
                }
            }
            .addOnFailureListener { e ->
                // Handle failure
                Toast.makeText(
                    context,
                    "Error querying documents: $e",
                    Toast.LENGTH_SHORT
                ).show()
                callback("null")
            }
    }

    fun updateBalance(
        db: FirebaseFirestore,
        context: Context,
        amount: String,
        user_id: String?,
        callback: (Boolean) -> Unit
    ) {
        val collectionRef = db.collection("wallets")

        collectionRef.whereEqualTo("user_id", user_id)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    // Get the document ID
                    val documentId = document.id
                    collectionRef.document(documentId)
                        .update("available", amount)
                        .addOnSuccessListener { documentReference ->
                            Toast.makeText(context, "Amount updated for document with ID: $documentId", Toast.LENGTH_SHORT).show()
                            callback(true)
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(context, "Error updating Amount for document with ID: $documentId: $e", Toast.LENGTH_SHORT).show()
                            callback(false)
                        }
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error querying documents: $e", Toast.LENGTH_SHORT).show()
                callback(false)
            }
    }
}