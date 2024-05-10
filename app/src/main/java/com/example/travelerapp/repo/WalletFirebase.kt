package com.example.travelerapp.repo

import android.content.Context
import android.util.Base64
import android.util.Log
import android.widget.Toast
import com.example.travelerapp.DBHandler
import com.example.travelerapp.createTransaction
import com.example.travelerapp.data.AgencyUser
import com.example.travelerapp.data.Trip
import com.example.travelerapp.data.Wallet
import com.example.travelerapp.decrypt
import com.example.travelerapp.encrypt
import com.google.firebase.firestore.FirebaseFirestore
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.SecretKey

private const val ANDROID_KEY_STORE = "AndroidKeyStore"
private const val AES_MODE = "AES/GCM/NoPadding"
private const val KEY_ALIAS = "WalletPin"
class WalletFirebase {
    fun createWallet(db: FirebaseFirestore, context: Context, user_id: String) {
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
            }
            .addOnFailureListener {
                Toast.makeText(context, "Error adding wallet to Firestore", Toast.LENGTH_SHORT)
                    .show()
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


    fun updateWalletPIN(db: FirebaseFirestore, context: Context, pin: String, user_id: String?) {
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
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(
                                context,
                                "Error updating walletPin for document with ID: $documentId: $e",
                                Toast.LENGTH_SHORT
                            ).show()
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
            }
    }

    fun reloadBalance(
        db: FirebaseFirestore,
        context: Context,
        amount: String,
        user_id: String?,
    ) {
        val collectionRef = db.collection("wallets")

        collectionRef.whereEqualTo("user_id", user_id)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    // Get the document ID
                    val documentId = document.id
//                    var available = document.get("available")
//                    if (available is Double) {
//                        var available = available
//                        available += amount.toDouble()
//                        // Perform further operations with available
//                    } else if (available is Long) {
//                        // If availableValue is of type Long, convert it to Double and perform calculations
//                        var available = available.toDouble() // Convert Long to Double
//                        available += amount.toDouble()
//                        // Perform further operations with available
//                    } else {
//                        // Handle the case where availableValue is not of type Double or Long
//                        // For example, you might need to handle other types like Int or Float
//                    }
//                    available += amount.toDouble()

                    collectionRef.document(documentId)
                        .update("available", amount)
                        .addOnSuccessListener { documentReference ->
//                            createTransaction(db, context, "Reload", available, documentId)
                            Toast.makeText(
                                context,
                                "Amount updated for document with ID: $documentId",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(
                                context,
                                "Error updating Amount for document with ID: $documentId: $e",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error querying documents: $e", Toast.LENGTH_SHORT).show()
            }
    }
}