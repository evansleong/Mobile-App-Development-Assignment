package com.example.travelerapp

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User

fun addUDatatoFirestore(
    context: Context,
    db: FirebaseFirestore,
    userName: String,
    userEmail: String,
    userPw: String,
){

    val userData = hashMapOf(
        "userName" to userName,
        "userEmail" to userEmail,
        "userPw" to userPw,
    )

    db.collection("User")
        .add(userData)
        .addOnSuccessListener { documentReference ->
            Log.d("Firestore","User added with ID: ${documentReference.id}")
            Toast.makeText(context,"User added to Firestore with ID: ${documentReference.id}",Toast.LENGTH_SHORT).show()
        }
        .addOnFailureListener { e ->
            Log.e("Firestore","Error adding user", e)
            Toast.makeText(context,"Error adding user to Firestore",Toast.LENGTH_SHORT).show()
        }

}

@SuppressLint("RestrictedApi")
fun readUserDataFromFirestore(db: FirebaseFirestore, callback:(List<User>) -> Unit){
    db.collection("user")
        .get()
        .addOnSuccessListener { documents ->
            val users = mutableListOf<User>()
            for (document in documents) {
                try {
                    val user: User = document.toObject(User::class.java)
                    users.add(user)
                } catch (e: Exception){
                    Log.e("Firestore","Error converting doc to User: ${e.message}")
                }
            }
            callback(users)
        }
        .addOnFailureListener { e ->
            Log.e("Firestore","Error getting docs: ${e.message}",e)
        }
}

//check if email is used or not
fun checkULoginCred(email: String, password: String, users: List<com.example.travelerapp.data.User>): com.example.travelerapp.data.User? {
    return users.find { it.userEmail == email && it.userPw == password }
}

//check if username is used or not
fun isUNameAv(uname: String, users: List<com.example.travelerapp.data.User>):Boolean{
    return users.none { it.userName == uname }
}

fun isEmailAv(email:String,users: List<com.example.travelerapp.data.User>):Boolean {
    return users.none { it.userEmail == email }
}