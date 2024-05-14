package com.example.travelerapp.viewModel

import androidx.lifecycle.ViewModel
import com.example.travelerapp.data.User
import android.content.Context
import com.example.travelerapp.repo.NormalUserFirebase
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

    //check if username is used or not
    fun isUNameAv(uname: String, users: List<User>): Boolean {
        return users.none { it.userName == uname }
    }

    fun isEmailAv(email: String, users: List<User>): Boolean {
        return users.none { it.userEmail == email }
    }
}