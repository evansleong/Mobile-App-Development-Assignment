package com.example.travelerapp.viewModel

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.travelerapp.repo.AgencyUserFirebase
import com.example.travelerapp.data.AgencyUser
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.storage
import java.util.UUID

class AgencyViewModel : ViewModel() {
    var loggedInAgency: AgencyUser? = null
    private val database = AgencyUserFirebase()

    fun addAgency(context: Context, db: FirebaseFirestore, agencyId: String, agencyUsername: String, agencyEmail: String, agencyPassword: String, agencyPicture: String?, agencySecureQst: String){
        database.addDataToFirestore(
            context = context,
            db = db,
            agencyId = agencyId,
            agencyUsername = agencyUsername,
            agencyEmail = agencyEmail,
            agencyPassword = agencyPassword,
            agencyPicture = agencyPicture,
            agencySecureQst = agencySecureQst
        )
    }

    fun readAgencyData(db: FirebaseFirestore, callback: (List<AgencyUser>) -> Unit) {
        database.readAgencyDataFromFirestore(
            db = db,
            callback = callback
        )
    }

    fun readSingleAgencyByEmail(db: FirebaseFirestore, agencyEmail: String, callback: (AgencyUser?) -> Unit) {
        database.readSingleAgencyByEmail(
            db = db,
            callback = callback,
            email = agencyEmail
        )
    }

    fun readSingleAgencyData(db: FirebaseFirestore, agencyId: String, callback: (AgencyUser?) -> Unit) {
        database.readSingleAgencyFromFirestore(
            db = db,
            agencyId = agencyId,
            callback = callback
        )
    }

    fun editAgencyPicture(context: Context, db: FirebaseFirestore, agencyId: String, newPicture: String){
        database.editAgencyProfilePicture(
            context = context,
            db = db,
            agencyId = agencyId,
            newAgencyPicture = newPicture
        )
    }

    fun updateProfilePictureUri(newUri: String) {
        loggedInAgency?.agencyPicture = newUri
    }

    fun uploadImage(context: Context, imageUri: Uri?, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
        if (imageUri != null) {
            val storageRef = Firebase.storage.reference
            val imageRef = storageRef.child("images/${UUID.randomUUID()}")
            val uploadTask = imageRef.putFile(imageUri)

            uploadTask.addOnSuccessListener { taskSnapshot ->
                // Image uploaded successfully
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    // Get the download URL
                    val downloadUrl = uri.toString()
                    updateProfilePictureUri(downloadUrl) // Update the loggedInAgency
                    onSuccess(downloadUrl)
                }.addOnFailureListener { exception ->
                    onFailure(exception)
                }
            }.addOnFailureListener { exception ->
                onFailure(exception)
            }
        }
    }

    fun checkLoginCredentials(
        email: String,
        password: String,
        agencyUsers: List<AgencyUser>
    ): AgencyUser? {
        // Check if there is any user with the provided email and password
        return agencyUsers.find { it.agencyEmail == email && it.agencyPassword == password }
    }

    fun checkSecureCredentials(
        email: String,
        secureQst: String,
        agencyUsers: List<AgencyUser>
    ): AgencyUser? {
        // Check if there is any user with the provided email and password
        return agencyUsers.find { it.agencyEmail == email && it.agencySecureQst == secureQst }
    }

    fun saveLoginDetails(context: Context, email: String, password: String) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("agency_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("email", email)
        editor.putString("password", password)
        editor.putBoolean("isLoggedIn", true)
        editor.apply()
    }

    fun clearSavedLoginDetails(context: Context) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("agency_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("email")
        editor.remove("password")
        editor.putBoolean("isLoggedIn", false)
        editor.apply()
    }

    fun getLoginDetails(context: Context): Pair<String, String>? {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("agency_prefs", Context.MODE_PRIVATE)
        val email = sharedPreferences.getString("email", null)
        val password = sharedPreferences.getString("password", null)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        return if (email != null && password != null && isLoggedIn) {
            Pair(email, password)
        } else {
            null
        }
    }

    // Function to check if the username is available
    fun isUsernameAvailable(username: String, agencyUsers: List<AgencyUser>): Boolean {
        return agencyUsers.none { it.agencyUsername == username }
    }

    // Function to check if the email is available
    fun isEmailAvailable(email: String, agencyUsers: List<AgencyUser>): Boolean {
        return agencyUsers.none { it.agencyEmail == email }
    }

    // Function to validate email format
    fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
        return email.matches(emailRegex.toRegex())
    }

    // Function to validate password format
    fun isValidPassword(password: String): Boolean {
        // Password must contain at least 1 uppercase, 1 lowercase, and be at least 8 characters long
        val passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#\$%^&*()-_=+\\\\|\\[{\\]};:'\",<.>/?]).{8,}\$"
        return password.matches(passwordRegex.toRegex())
    }

    fun isConfirmPasswordMatch(password: String, confirmPassword: String): Boolean {
        return password.isNotBlank() && confirmPassword.isNotBlank() && password == confirmPassword
    }

    // Function to ensure all fields are not empty
    fun areFieldsNotEmpty(username: String, email: String, password: String, confirmPassword: String): Boolean {
        return username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()
    }

    fun getEmailRegexPattern(): String {
        return "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
    }

    fun getPasswordRegexPattern(): String {
        return "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}\$"
    }
}