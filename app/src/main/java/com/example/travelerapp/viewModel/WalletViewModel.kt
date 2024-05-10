package com.example.travelerapp.viewModel

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.lifecycle.ViewModel
import com.example.travelerapp.data.Wallet
import com.example.travelerapp.repo.WalletFirebase
import com.google.firebase.firestore.FirebaseFirestore
import java.security.KeyStore
import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

private const val ANDROID_KEY_STORE = "AndroidKeyStore"
private const val AES_MODE = "AES/GCM/NoPadding"
private const val KEY_ALIAS = "WalletPin"

class WalletViewModel : ViewModel() {
    var userWallet: Wallet? = null
    private val database = WalletFirebase()

    fun createWallet(db: FirebaseFirestore, context: Context, user_id: String) {
        database.createWallet(db, context, user_id)
    }

    fun readWallets(db: FirebaseFirestore, callback: (List<Wallet>) -> Unit){
        database.readDataFromFirestore(
            db = db,
            callback = callback
        )
    }

    fun checkWallet(user_id: String, wallets: List<Wallet>): Wallet? {
        return wallets.find { it.user_id == user_id }
    }

    fun updatePin(db: FirebaseFirestore, context: Context, newPin: String){
//        val pin: ByteArray = encrypt(newPin)
//        val base64String = Base64.encodeToString(pin, Base64.DEFAULT)
        this.userWallet?.walletPin = newPin
        database.updateWalletPIN(db, context, newPin, userWallet?.user_id)
    }
    fun reload(db: FirebaseFirestore, context: Context, amount: String) {
        val available = this.userWallet?.available?.toDouble()
        val reloadAmount = amount.toDouble()
        val total = available?.plus(reloadAmount)

        if (available != null) {
            this.userWallet?.available = total.toString()
        }
        database.reloadBalance(db, context, total.toString(), userWallet?.user_id)
    }

    fun checkPin(pin: String): Boolean {
//        val encryptedPin = encrypt(pin)
//        // Convert ByteArray to Base64 String
//        val base64String = Base64.encodeToString(encryptedPin, Base64.DEFAULT)

//        val byteArray = Base64.decode(userWallet?.walletPin, Base64.DEFAULT)
//        val decryptedPin = decrypt(byteArray)

//        return decryptedPin.equals(pin)
        return userWallet?.walletPin.equals(pin)
    }

    fun checkReloadSuccess(amount: String): Int {
        val available = userWallet?.available?.toDouble()
        val amount = amount.toDouble()
        if (available != null) {
            return available.compareTo(amount)
        }
        return 0
    }
//    private fun encrypt(pin: String): ByteArray {
//        val keyStore = KeyStore.getInstance(ANDROID_KEY_STORE)
//        keyStore.load(null)
//
//        val key = keyStore.getKey(KEY_ALIAS, null) as? SecretKey
//            ?: generateKey()
//
//        val cipher = Cipher.getInstance(AES_MODE)
//        cipher.init(Cipher.ENCRYPT_MODE, key)
//
//        return cipher.doFinal(pin.toByteArray(Charsets.UTF_8))
//    }
//    private fun decrypt(encryptedData: ByteArray): String {
//        val keyStore = KeyStore.getInstance(ANDROID_KEY_STORE)
//        keyStore.load(null)
//
//        val key = keyStore.getKey(KEY_ALIAS, null) as SecretKey
//
//        val cipher = Cipher.getInstance(AES_MODE)
//        cipher.init(Cipher.DECRYPT_MODE, key)
//
//        val decryptedData = cipher.doFinal(encryptedData)
//        return String(decryptedData, Charsets.UTF_8)
//    }
//
//    private fun generateKey(): SecretKey {
//        val keyGenerator =
//            KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEY_STORE)
//        val keyGenParameterSpec = KeyGenParameterSpec.Builder(
//            KEY_ALIAS,
//            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
//        )
//            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
//            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
//            .build()
//        keyGenerator.init(keyGenParameterSpec)
//        return keyGenerator.generateKey()
//    }

}