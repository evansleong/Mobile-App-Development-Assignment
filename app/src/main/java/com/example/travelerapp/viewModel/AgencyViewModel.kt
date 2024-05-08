package com.example.travelerapp.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.travelerapp.repo.AgencyUserFirebase
import com.example.travelerapp.data.AgencyUser
import com.google.firebase.firestore.FirebaseFirestore

class AgencyViewModel : ViewModel() {
    var loggedInAgency: AgencyUser? = null
    private val database = AgencyUserFirebase()

    fun addAgency(context: Context, db: FirebaseFirestore, agencyUsername: String, agencyEmail: String, agencyPassword: String){
        database.addDataToFirestore(
            context = context,
            db = db,
            agencyUsername = agencyUsername,
            agencyEmail = agencyEmail,
            agencyPassword
        )
    }

    fun readAgencyData(db: FirebaseFirestore, callback: (List<AgencyUser>) -> Unit) {
        database.readAgencyDataFromFirestore(
            db = db,
            callback = callback
        )
    }

    fun checkLoginCredentials(
        email: String,
        password: String,
        agencyUsers: List<AgencyUser>
    ): AgencyUser? {
        // Check if there is any user with the provided email and password
        return agencyUsers.find { it.agencyEmail == email && it.agencyPassword == password }
    }

    // Function to check if the username is available
    fun isUsernameAvailable(username: String, agencyUsers: List<AgencyUser>): Boolean {
        return agencyUsers.none { it.agencyUsername == username }
    }

    // Function to check if the email is available
    fun isEmailAvailable(email: String, agencyUsers: List<AgencyUser>): Boolean {
        return agencyUsers.none { it.agencyEmail == email }
    }
}