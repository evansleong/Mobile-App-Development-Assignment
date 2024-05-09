package com.example.travelerapp

import androidx.lifecycle.ViewModel
import com.example.travelerapp.data.User
import android.content.Context
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore

class UserViewModel: ViewModel() {
    var loggedInUser: User? = null
    private val database = NormalUserFirebase()

    fun addUser(
        context: Context,
        db:FirebaseFirestore,
        userName: String,
        userEmail: String,
        userPw: String,
        userWalletPin: Int? = null ){
        database.addUDatatoFirestore(
            context = context,
            db = db,
            userName = userName,
            userEmail = userEmail,
            userPw = userPw
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

    fun checkULC(
        email: String,
        password: String,
        users: List<User>
    ): User? {
        return users.find {
            it.userEmail == email && it.userPw == password
        }
    }

    fun unameAvailable(
        username: String,
        users: List<User>) : Boolean
    {
        return users.none{it.userName == username}
    }

    fun isUEmailUsed(
        email: String,
        users: List<User>
    ): Boolean{
        return users.none{it.userEmail == email}
    }
}