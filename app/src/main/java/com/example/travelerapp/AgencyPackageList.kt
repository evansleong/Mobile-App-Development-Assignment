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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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

    Scaffold(
        topBar = {
            ReuseComponents.TopBar(title = "Travel Package Management", navController, showBackButton = true)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(route = Screen.AgencyAddPackage.route)
                },
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add New Package"
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { contentPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
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

    Card(
        modifier = Modifier
            .padding(8.dp)
            .height(130.dp)
            .clickable {
                tripViewModel.selectedTripId = trip.tripId
                navController.navigate(route = Screen.AgencyPackageDetail.route)
            },
        shape = RoundedCornerShape(20.dp), // Rounded corners
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
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
                        .padding(end = 8.dp)
                        .clip(RoundedCornerShape(20.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(13.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = trip.tripName,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp
                    )
                    Text(
                        text = trip.tripLength,
                        fontSize = 15.sp
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
    }
    
    Spacer(modifier = Modifier.height(15.dp))

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