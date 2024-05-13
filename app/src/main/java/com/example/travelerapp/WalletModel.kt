package com.example.travelerapp
//
//import android.content.Context
//import android.security.keystore.KeyGenParameterSpec
//import android.security.keystore.KeyProperties
//import android.widget.Toast
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import com.google.firebase.Firebase
//import com.google.firebase.firestore.FirebaseFirestore
//import com.google.firebase.firestore.firestore
//import java.security.KeyStore
//import javax.crypto.Cipher
//import javax.crypto.KeyGenerator
//import javax.crypto.SecretKey
//
//// createWallet(db ,context, "1020")
//fun createWallet(db: FirebaseFirestore, context: Context, user_id: String) {
//    val dbHandler: DBHandler = DBHandler(context)
//    val newWallet = hashMapOf(
//        "user_id" to user_id,
//        "available" to 0,
//        "frozen" to 0,
//        "walletPin" to null,
//    )
//
//    dbHandler.updateAuthUser(user_id)
//    db.collection("wallets")
//        .add(newWallet)
//        .addOnSuccessListener {documentReference ->
//            Toast.makeText(context, "Wallet added to Firestore with ID: ${documentReference.id}", Toast.LENGTH_SHORT).show()
//            dbHandler.createWallet(documentReference.id, user_id)
//        }
//        .addOnFailureListener {
//            Toast.makeText(context, "Error adding wallet to Firestore", Toast.LENGTH_SHORT).show()
//        }
//}
//
//fun checkWalletPin(db: FirebaseFirestore, dbHandler: DBHandler):Boolean {
//    val user_id = dbHandler.getAuthUser()
//    var walletPin: ByteArray? = null
//
//    db.collection("wallets")
//        .whereEqualTo("user_id", user_id)
//        .get()
//        .addOnSuccessListener { documents ->
//            if (!documents.isEmpty) {
//                walletPin = documents.documents[0].get("walletPin") as? ByteArray
//            }
//        }
//        .addOnFailureListener { e ->
//            // Handle failure
//            Toast.makeText(
//                null,
//                "Error querying documents: $e",
//                Toast.LENGTH_SHORT
//            ).show()
//        }
//    if (walletPin != null) {
//        return true
//    } else {
//        return false
//    }
//}
//
//fun updateWalletPIN(db: FirebaseFirestore, context: Context, pin: String) {
//    val dbHandler: DBHandler = DBHandler(context)
//    val user_id = dbHandler.getAuthUser()
//
//    val collectionRef = db.collection("wallets")
//
//    val encryptedPin = encrypt(pin)
//    // Query for documents where user_id matches the provided value
//    collectionRef.whereEqualTo("user_id", user_id)
//        .get()
//        .addOnSuccessListener { documents ->
//            for (document in documents) {
//                // Get the document ID
//                val documentId = document.id
//
//                collectionRef.document(documentId)
//                    .update("walletPin", encryptedPin)
//                    .addOnSuccessListener {
//                        Toast.makeText(
//                            context,
//                            "WalletPin updated for document with ID: $documentId",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                        dbHandler.updateWalletPin(encryptedPin)
//                    }
//                    .addOnFailureListener { e ->
//                        Toast.makeText(
//                            context,
//                            "Error updating walletPin for document with ID: $documentId: $e",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//            }
//        }
//        .addOnFailureListener { e ->
//            // Handle failure
//            Toast.makeText(
//                context,
//                "Error querying documents: $e",
//                Toast.LENGTH_SHORT
//            ).show()
//        }
//}
//fun reloadBalance(
//    db: FirebaseFirestore,
//    context: Context,
//    amount: String,
//    pin: String
//): Boolean {
//    val dbHandler: DBHandler = DBHandler(context)
//    val user_id = dbHandler.getAuthUser()
//    val collectionRef = db.collection("wallets")
//    var is_valid = false
//
//    // Query for documents where user_id matches the provided value
//    collectionRef.whereEqualTo("user_id", user_id)
//        .get()
//        .addOnSuccessListener { documents ->
//            for (document in documents) {
//                // Get the document ID
//                val documentId = document.id
//                var available = document.get("available") as Double
//                available += amount.toDouble()
//                val encryptedPin = document.get("walletPin") as ByteArray
//                val decryptedPin = decrypt(encryptedPin)
//
//                if (decryptedPin == pin) {
//                    collectionRef.document(documentId)
//                        .update("available", available)
//                        .addOnSuccessListener { documentReference ->
//                            dbHandler.reloadBalance(available)
//                            createTransaction(db, context, "Reload", available, documentId)
//                            is_valid = true
//                            Toast.makeText(
//                                context,
//                                "Amount updated for document with ID: $documentId",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        }
//                        .addOnFailureListener { e ->
//                            Toast.makeText(
//                                context,
//                                "Error updating Amount for document with ID: $documentId: $e",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        }
//                }
//            }
//        }
//        .addOnFailureListener { e ->
//            Toast.makeText(context, "Error querying documents: $e", Toast.LENGTH_SHORT).show()
//        }
//    return is_valid
//}
//
//private const val ANDROID_KEY_STORE = "AndroidKeyStore"
//private const val AES_MODE = "AES/GCM/NoPadding"
//private const val KEY_ALIAS = "WalletPIN"
//
//fun encrypt(pin: String): ByteArray {
//    val keyStore = KeyStore.getInstance(ANDROID_KEY_STORE)
//    keyStore.load(null)
//
//    val key = keyStore.getKey(KEY_ALIAS, null) as? SecretKey
//        ?: generateKey()
//
//    val cipher = Cipher.getInstance(AES_MODE)
//    cipher.init(Cipher.ENCRYPT_MODE, key)
//
//    return cipher.doFinal(pin.toByteArray(Charsets.UTF_8))
//}
//
//fun decrypt(encryptedData: ByteArray): String {
//    val keyStore = KeyStore.getInstance(ANDROID_KEY_STORE)
//    keyStore.load(null)
//
//    val key = keyStore.getKey(KEY_ALIAS, null) as SecretKey
//
//    val cipher = Cipher.getInstance(AES_MODE)
//    cipher.init(Cipher.DECRYPT_MODE, key)
//
//    val decryptedData = cipher.doFinal(encryptedData)
//    return String(decryptedData, Charsets.UTF_8)
//}
//
//private fun generateKey(): SecretKey {
//    val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEY_STORE)
//    val keyGenParameterSpec = KeyGenParameterSpec.Builder(
//        KEY_ALIAS,
//        KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
//    )
//        .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
//        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
//        .build()
//    keyGenerator.init(keyGenParameterSpec)
//    return keyGenerator.generateKey()
//}