package com.example.travelerapp.repo

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.travelerapp.data.User
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.reflect.InvocationTargetException

class NormalUserFirebase {
    fun addUDatatoFirestore(
        context: Context,
        db: FirebaseFirestore,
        userName: String,
        userEmail: String,
        userPw: String,
        userSecureQst: String,
        userImgUri: String?,
        callback: (String) -> Unit
    ) {

        val userData = hashMapOf(
            "userName" to userName,
            "userEmail" to userEmail,
            "userPw" to userPw,
            "userSecureQst" to userSecureQst,
            "userUri" to userImgUri
        )

        db.collection("User")
            .add(userData)
            .addOnSuccessListener { documentReference ->
                Log.d("Firestore", "User added with ID: ${documentReference.id}")
                Toast.makeText(context, "User added to Firestore with ID: ${documentReference.id}", Toast.LENGTH_SHORT).show()
                callback(documentReference.id)
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error adding user", e)
                Toast.makeText(context, "Error adding user to Firestore", Toast.LENGTH_SHORT).show()
                callback("null")
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
                    } catch (e: InvocationTargetException) {
                        Log.e("Firestore", "InvocationTargetException: ${e.targetException.message}", e.targetException)
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

    fun editUserProfilePicture(
        context: Context,
        db: FirebaseFirestore,
        userId: String,
        newUserPicture: String,
    ) {
        val agencyRef = db.collection("User").document(userId)

        val newData = hashMapOf<String, Any>(
            "userUri" to newUserPicture,
        )

        agencyRef
            .update(newData)
            .addOnSuccessListener {
                Log.d("Firestore", "UserData edited successfully")
                Toast.makeText(context, "UserData edited successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error editing UserData: ${e.message}", e)
                Toast.makeText(context, "Error editing UserData", Toast.LENGTH_SHORT).show()
            }
    }

    fun changeUserData(
        context: Context,
        db: FirebaseFirestore,
        userId: String,
        newUsername: String,
        newUserPw: String,
    ){
        val newUData = hashMapOf<String, Any?>(
            "userPw" to newUserPw
        )

        db.collection("User").document(userId)
            .update(newUData)
            .addOnSuccessListener {
                Log.d("Firestore","User Data Updated")
                Toast.makeText(context,"User Data Updated",Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error occur in fetching document, $e", Toast.LENGTH_LONG).show()
            }
    }
}