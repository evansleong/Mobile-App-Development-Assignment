package com.example.travelerapp

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.travelerapp.data.Trip

//import com.example.travelerapp.Screen.AgencyAddPackage

@Composable
fun AgencyPackageList(
    navController: NavController,
    context: Context
) {
    val activity = context as Activity

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        var dbHandler: DBHandler = DBHandler(context)
        val title = "Travel Package Management"
        ReuseComponents.TopBar(title = title, navController, showBackButton = true)

        Spacer(modifier = Modifier.height(20.dp))

        // Call getAllTrips to get the list of trips
        val tripList = dbHandler.getAllTrips()

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(tripList) { trip ->
                TripItem(trip = trip)
            }
        }
    }
}

@Composable
fun TripItem(trip: Trip) {
    // Display the trip information in each item
    Button(onClick = { /* Handle item click if needed */ }) {
        Text(text = "Trip Name: ${trip.tripName}, Fees: ${trip.tripFees}, Deposit: ${trip.tripDeposit}, Desc: ${trip.tripDesc}")
    }
}

@Preview
@Composable
fun PreviewAgencyPackageList() {
    AgencyPackageList(navController = rememberNavController(), context = LocalContext.current)
}
