package com.example.travelerapp

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.travelerapp.data.Trip
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

//import com.example.travelerapp.Screen.AgencyAddPackage

@Composable
fun AgencyPackageList(
    navController: NavController,
    context: Context,
    viewModel: AgencyViewModel
) {
    val db = Firebase.firestore
    val activity = context as Activity

    val loggedInAgency = viewModel.loggedInAgency

    // State to hold the list of trips
    val tripListState = remember { mutableStateOf<List<Trip>>(emptyList()) }

    // Call readDataFromFirestore when this composable is composed
    LaunchedEffect(key1 = true) {
        readDataFromFirestore(db) { trips ->
            val filteredTrips = trips.filter { trip ->
                trip.agencyUsername == loggedInAgency?.agencyUsername
            }
            // Update the tripListState with the fetched trips
            tripListState.value = filteredTrips
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        val dbHandler = DBHandler(context)
        val title = "Travel Package Management"
        ReuseComponents.TopBar(title = title, navController, showBackButton = true)

        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(tripListState.value) { trip ->
                TripItem(trip = trip)
            }
        }
    }
}



@OptIn(ExperimentalCoilApi::class)
@Composable
fun TripItem(trip: Trip) {
    // Load the image using Coil
    val painter: Painter = rememberImagePainter(trip.tripUri)

    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically // Align items vertically
        ) {
            // Display the image with specified content scale
            Image(
                painter = painter,
                contentDescription = trip.tripName,
                modifier = Modifier
                    .height(100.dp) // Adjust the height of the image
                    .width(100.dp) // Adjust the width of the image
                    .padding(end = 8.dp), // Add padding to the right of the image
                contentScale = ContentScale.Crop
            )
            // Display trip name and length in a column
            Column(
                modifier = Modifier.weight(1f) // Expand to fill remaining space
            ) {
                Text(
                    text = trip.tripName,
                    fontWeight = FontWeight.Bold // Optionally, make the name bold
                )
                Text(
                    text = trip.tripLength
                )
            }
        }
    }
}



@Preview
@Composable
fun PreviewAgencyPackageList() {
    AgencyPackageList(navController = rememberNavController(), context = LocalContext.current, viewModel = AgencyViewModel())
}