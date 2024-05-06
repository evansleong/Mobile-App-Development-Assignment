package com.example.travelerapp

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.travelerapp.data.AgencyUser

class AgencyViewModel : ViewModel() {
    var loggedInAgency: AgencyUser? = null
}