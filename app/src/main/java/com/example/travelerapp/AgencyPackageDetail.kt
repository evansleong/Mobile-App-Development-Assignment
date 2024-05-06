package com.example.travelerapp

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.travelerapp.data.Trip
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

@Composable
fun AgencyPackageDetail(
    navController: NavController,
    context: Context,
    viewModel: AgencyViewModel
) {
    val db = Firebase.firestore
    val activity = context as Activity

    val loggedInAgency = viewModel.loggedInAgency

    // State to hold the list of trips
    val tripListState = remember { mutableStateOf<List<Trip>>(emptyList()) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

    }
}



@Preview
@Composable
fun PreviewAgencyPackageDetail() {
    AgencyPackageList(navController = rememberNavController(), context = LocalContext.current, viewModel = AgencyViewModel())
}