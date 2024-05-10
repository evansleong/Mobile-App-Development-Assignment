package com.example.travelerapp

import ReuseComponents
import android.app.Activity
import android.content.Context
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.travelerapp.data.Trip
import com.example.travelerapp.viewModel.AgencyViewModel
import com.example.travelerapp.viewModel.TripViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

@Composable
fun AgencyPackageList(
    navController: NavController,
    context: Context,
    viewModel: AgencyViewModel,
    tripViewModel: TripViewModel
) {
    val db = Firebase.firestore
    val activity = context as Activity

    val loggedInAgency = viewModel.loggedInAgency

    val tripListState = remember { mutableStateOf<List<Trip>>(emptyList()) }

    LaunchedEffect(key1 = true) {
        tripViewModel.readTrip(db) { trips ->
            val filteredTrips = trips.filter { trip ->
                trip.agencyUsername == loggedInAgency?.agencyUsername
            }
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
                TripItem(trip = trip, navController = navController, tripViewModel) {
                    tripViewModel.deleteTrip(
                        db,
                        trip.tripId,
                        onSuccess = {},
                        onFailure = {}
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
                ReuseComponents.CustomButton(text = "Add new package") {
                    navController.navigate(route = Screen.AgencyAddPackage.route) {
                    }
                }
            }
        }
    }
}

@Composable
fun TripItem(
    trip: Trip,
    navController: NavController,
    tripViewModel: TripViewModel,
    onDeleteTrip: () -> Unit
) {
    val painter: Painter = rememberAsyncImagePainter(model = Uri.parse(trip.tripUri))

    val showDialog = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable {
                tripViewModel.selectedTripId = trip.tripId
                navController.navigate(route = Screen.AgencyPackageDetail.route)
            }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                painter = painter,
                contentDescription = trip.tripName,
                modifier = Modifier
                    .height(100.dp)
                    .width(100.dp)
                    .padding(end = 8.dp),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = trip.tripName,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = trip.tripLength
                )
            }
            IconButton(
                onClick = { showDialog.value = true },
                content = {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete Trip"
                    )
                }
            )
        }
    }

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text("Confirm Deletion") },
            text = { Text("Are you sure you want to delete this trip?") },
            confirmButton = {
                Button(onClick = {
                    showDialog.value = false
                    onDeleteTrip()
                }) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog.value = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}


@Preview
@Composable
fun PreviewAgencyPackageList() {
    AgencyPackageList(navController = rememberNavController(), context = LocalContext.current, viewModel = AgencyViewModel(), tripViewModel = TripViewModel())
}