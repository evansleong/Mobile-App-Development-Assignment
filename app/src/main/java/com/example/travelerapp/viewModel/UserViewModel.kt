package com.example.travelerapp.viewModel

import androidx.lifecycle.ViewModel
import com.example.travelerapp.data.User
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import com.example.travelerapp.data.AgencyUser
import com.example.travelerapp.repo.NormalUserFirebase
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.storage
import java.util.UUID

class UserViewModel: ViewModel() {
    var loggedInUser: User? = null
    private val database = NormalUserFirebase()
    private val USER_PREFS_KEY = "user_prefs"
    private val ADMIN_PREFS_KEY = "admin_prefs"

    fun addUser(
        context: Context,
        db:FirebaseFirestore,
        userName: String,
        userEmail: String,
        userPw: String,
        userSecureQst: String,
        userUri: String? = "null",
        callback: (String) -> Unit
    ){
        database.addUDatatoFirestore(
            context = context,
            db = db,
            userName = userName,
            userEmail = userEmail,
            userPw = userPw,
            userSecureQst = userSecureQst,
            userImgUri = userUri,
            callback
        )
    }

    fun readUData(
        db: FirebaseFirestore,
        callback: (List<User>)->Unit){
        database.readUserDataFromFirestore(
            db = db,
            callback = callback
        )
    }

    fun checkULoginCred(
        email: String,
        password: String,
        users: List<User>
    ): User? {
        return users.find { it.userEmail == email && it.userPw == password }
    }

    fun saveLoginDetails(context: Context, email: String, password: String) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("email", email)
        editor.putString("password", password)
        editor.putBoolean("isLoggedIn", true)
        editor.apply()
    }

    fun getLoginDetails(context: Context): Pair<String, String>? {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val email = sharedPreferences.getString("email", null)
        val password = sharedPreferences.getString("password", null)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        return if (email != null && password != null && isLoggedIn) {
            Pair(email, password)
        } else {
            null
        }
    }

    fun clearSavedLoginDetails(context: Context) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("agency_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("email")
        editor.remove("password")
        editor.putBoolean("isLoggedIn", false)
        editor.apply()
    }

    fun editAgencyPicture(context: Context, db: FirebaseFirestore, userId: String, newPicture: String){
        database.editUserProfilePicture(
            context = context,
            db = db,
            userId = userId,
            newUserPicture = newPicture
        )
    }
    fun updateProfilePictureUri(newUri: String) {
        loggedInUser?.userUri = newUri
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

    fun checkSecureCredentials(
        email: String,
        secureQst: String,
        users: List<User>
    ): User? {
        // Check if there is any user with the provided email and secureQuestion
        return users.find { it.userEmail == email && it.userSecureQst == secureQst }
    }

    fun updateData(
        context: Context,
        db: FirebaseFirestore,
        userId: String,
        userName: String = "",
        userPw: String = "",
    ) {
        database.changeUserData(context, db, userId, userName, userPw)
    }

    //check if username is used or not
    fun isUNameAv(uname: String, users: List<User>): Boolean {
        return users.none { it.userName == uname }
    }

    fun isEmailAv(email: String, users: List<User>): Boolean {
        return users.none { it.userEmail == email }
    }

    fun isUEmailV(email:String): Boolean{
        val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
        return email.matches(emailRegex.toRegex())
    }

    fun isUPwV(pw:String):Boolean{
        val pwRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#\$%^&*()-_=+\\\\|\\[{\\]};:'\",<.>/?]).{8,}\$"
        return pw.matches(pwRegex.toRegex())
    }

    fun isConfirmPasswordMatch(password: String, confirmPassword: String): Boolean {
        return password.isNotBlank() && confirmPassword.isNotBlank() && password == confirmPassword
    }
}