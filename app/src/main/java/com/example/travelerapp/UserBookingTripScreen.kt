package com.example.travelerapp

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.travelerapp.data.Trip
import com.example.travelerapp.viewModel.TripViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

import androidx.compose.foundation.lazy.LazyColumn

@Composable
fun UserBookingTripScreen(
    navController: NavController,
    context: Context,
    tripViewModel: TripViewModel
) {
    val db = Firebase.firestore
    val activity = context as Activity
    val tripState = remember { mutableStateOf<Trip?>(null) }

    val selectedPackage = tripViewModel.selectedTripId

    var numPax by remember { mutableStateOf(0) }

    LaunchedEffect(selectedPackage) {
        tripViewModel.readSingleTrip(db, selectedPackage.toString()) { trip ->
            tripState.value = trip
        }
    }

    // Coroutine scope for asynchronous operations
    val coroutineScope = rememberCoroutineScope()

    tripState.value?.let { trip ->
        Scaffold(
            topBar = {
                ReuseComponents.TopBar(title = trip.tripName, navController, showBackButton = true)
            }
        ) { contentPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
            ) {
                item {
                    Image(
                        painter = rememberAsyncImagePainter(trip.tripUri),
                        contentDescription = trip.tripUri,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16f / 9f)
                    )
                }

                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = trip.tripName,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Card(
                            modifier = Modifier
                                .height(400.dp)
                                .width(350.dp),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = "${trip.tripLength} - RM${trip.tripFees}/pax",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )

                                // Display trip departure date
                                Text(
                                    text = "Departure Date: ${trip.depDate}",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                // Display trip return date
                                Text(
                                    text = "Return Date: ${trip.retDate}",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                Text(
                                    text = trip.tripDesc,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold
                                )

                                Spacer(modifier = Modifier.height(30.dp))

                                Text(
                                    text = "Packages Includes:",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(5.dp))

                                trip.options.forEach { option ->
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Filled.Info,
                                            contentDescription = "Check Icon",
                                            modifier = Modifier
                                                .size(30.dp)
                                                .padding(end = 11.dp)
                                        )

                                        Spacer(modifier = Modifier.height(30.dp))

                                        Text(
                                            text = option,
                                            fontSize = 18.sp
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))

                        // TextField for number of passengers
                        TextField(
                            value = numPax.toString(),
                            onValueChange = {
                                numPax = it.toInt()
                            },
                            label = { Text(text = "Enter number of passengers") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Text(
                            text = "Booking Fee: RM ${trip.tripDeposit}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Button(onClick = {
                            // Update Firestore isAvailable value
                            val updatedAvailable = trip.isAvailable - numPax
                            if (updatedAvailable >= 0) {
                                db.collection("trips").document(trip.tripId).update("isAvailable", updatedAvailable)
                                    .addOnSuccessListener {
                                        // Update successful, proceed with payment or other actions
                                    }
                                    .addOnFailureListener { e ->
                                        // Handle failure
                                    }
                            } else {
                                // Handle insufficient availability error
                            }
                        }) {
                            Text(text = "Pay")
                        }
                    }
                }
            }
        }
    }
}


@Preview
@Composable
fun PreviewUserBookingTripScreen() {
    UserBookingTripScreen(navController = rememberNavController(), context = LocalContext.current, tripViewModel = TripViewModel())
}

