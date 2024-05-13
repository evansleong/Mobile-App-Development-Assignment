package com.example.travelerapp.repo

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.travelerapp.data.User
import com.google.firebase.firestore.FirebaseFirestore

class NormalUserFirebase {
    fun addUDatatoFirestore(
        context: Context,
        db: FirebaseFirestore,
        userName: String,
        userEmail: String,
        userPw: String,
        userImgUri: String?
    ) {

        val userData = hashMapOf(
            "userName" to userName,
            "userEmail" to userEmail,
            "userPw" to userPw,
            "userUri" to userImgUri
        )

        db.collection("User")
            .add(userData)
            .addOnSuccessListener { documentReference ->
                Log.d("Firestore", "User added with ID: ${documentReference.id}")
                Toast.makeText(
                    context,
                    "User added to Firestore with ID: ${documentReference.id}",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error adding user", e)
                Toast.makeText(context, "Error adding user to Firestore", Toast.LENGTH_SHORT).show()
            }
    }

//    fun editUserFirebase(
//        context: Context,
//        db: FirebaseFirestore,
//        userEmail: String,
//        newUserName: String,
//        newUserPw: String,
//        newUserImgUri: String
//    ){
//        val userRef = db.collection("User").document(userEmail)
//
//        val newUData = hashMapOf(
//            "userUri" to newUserImgUri,
//            "userName" to newUserName,
//            "userPw" to newUserPw
//        )
//
//        userRef.update(newUData)
//            .addOnSuccessListener {
//                Log.d("Firestore","User Data Updated")
//                Toast.makeText(context,"User Data Updated",Toast.LENGTH_SHORT).show()
//            }
//    }


    fun readUserDataFromFirestore(db: FirebaseFirestore, callback: (List<User>) -> Unit) {
        db.collection("User")
            .get()
            .addOnSuccessListener { documents ->
                val users = mutableListOf<User>()
                for (document in documents) {
                    try {
                        val user: User = document.toObject(User::class.java)
                        user.userId = document.id
                        users.add(user)
                    } catch (e: Exception) {
                        Log.e("Firestore", "Error converting doc to User: ${e.message}")
                    }
                }
                callback(users)
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error getting docs: ${e.message}", e)
            }
    }

    //check if email is used or not
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