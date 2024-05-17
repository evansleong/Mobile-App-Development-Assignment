package com.example.travelerapp.viewModel

import androidx.lifecycle.ViewModel
import com.example.travelerapp.data.User
import android.content.Context
import android.content.SharedPreferences
import com.example.travelerapp.repo.NormalUserFirebase
import com.google.firebase.firestore.FirebaseFirestore

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
        userUri: String? = null,
        userWalletPin: Int? = null,
        callback: (String) -> Unit
    ){
        database.addUDatatoFirestore(
            context = context,
            db = db,
            userName = userName,
            userEmail = userEmail,
            userPw = userPw,
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
        val pwRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}\$"
        return pw.matches(pwRegex.toRegex())
    }
}