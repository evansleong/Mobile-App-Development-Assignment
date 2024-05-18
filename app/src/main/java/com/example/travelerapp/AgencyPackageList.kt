package com.example.travelerapp

import ReuseComponents
import android.app.Activity
import android.content.Context
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.travelerapp.ui.theme.CusFont3
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

    val noPackages = remember { mutableStateOf(false) }

    val (refreshTrigger, setRefreshTrigger) = remember { mutableIntStateOf(0) }

    val tripListState = remember { mutableStateOf<List<Trip>>(emptyList()) }

    fun refreshScreen() {
        setRefreshTrigger(refreshTrigger + 1)
    }

    LaunchedEffect(key1 = refreshTrigger) {
        tripViewModel.readTrip(db) { trips ->
            val filteredTrips = trips.filter { trip ->
                trip.agencyUsername == loggedInAgency?.agencyUsername
            }
            tripListState.value = filteredTrips
            noPackages.value = filteredTrips.isEmpty()
        }
    }

    Scaffold(
        topBar = {
            ReuseComponents.TopBar(
                title = "Travel Package Management",
                navController,
                showBackButton = true,
                isAgencySide = true,
            )
        },
        bottomBar = {
            ReuseComponents.AgencyNavBar(text = "AgencyPackage", navController = navController)
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
        if (noPackages.value) {
            // Show a message when there are no packages
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("No packages found.", fontSize = 18.sp)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
            ) {
                items(tripListState.value) { trip ->
                    TripItem(
                        trip = trip,
                        navController = navController,
                        tripViewModel,
                        onDeleteTrip = {
                        tripViewModel.deleteTrip(
                            db,
                            trip.tripId,
                            onSuccess = {
                                refreshScreen()
                            },
                            onFailure = {}
                        )
                    },
                        onRefresh = { refreshScreen() }
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
    onDeleteTrip: () -> Unit,
    onRefresh: () -> Unit
) {
    val painter: Painter = rememberAsyncImagePainter(model = Uri.parse(trip.tripUri))

    val showDialog = remember { mutableStateOf(false) }
    val showDeletedMessage = remember { mutableStateOf(false) }

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
                        fontSize = 12.sp,
                        fontFamily = CusFont3
                    )
                    Text(
                        text = "No. of booked user: ${trip.noOfUserBooked}/${trip.noOfUserBooked+trip.isAvailable}",
                        fontSize = 13.sp,
                        fontFamily = CusFont3
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
                    showDeletedMessage.value = true
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

    // Show a message when trip is successfully deleted
    if (showDeletedMessage.value) {
        AlertDialog(
            onDismissRequest = { showDeletedMessage.value = false },
            title = { Text("Trip Package has been successfully deleted") },
            text = { Text("The trip package '${trip.tripName}' has been deleted") },
            confirmButton = {
                Button(onClick = { showDeletedMessage.value = false }) {
                    Text("OK")
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