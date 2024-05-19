package com.example.travelerapp.normalUserScreen

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.travelerapp.screen.Screen
import com.example.travelerapp.ui.theme.CusFont3
import com.example.travelerapp.viewModel.TripViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

@Composable
fun UserViewTripScreen(
    navController: NavController,
    context: Context,
    tripViewModel: TripViewModel
) {
    val db = Firebase.firestore
    val activity = context as Activity
    val tripState = remember { mutableStateOf<Trip?>(null) }

    val selectedPackage = tripViewModel.selectedTripId

    LaunchedEffect(selectedPackage) {
        tripViewModel.readSingleTrip(db, selectedPackage.toString()) { trip ->
            tripState.value = trip
        }
    }

    tripState.value?.let { trip ->
        Scaffold(
            topBar = {
                ReuseComponents.TopBar(title = trip.tripName, navController, showBackButton = true)
            }
        ) { contentPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(trip.tripUri),
                        contentDescription = trip.tripUri,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16f / 9f)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = trip.tripName,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Card(
                        modifier = Modifier
                            .fillMaxSize(),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "${trip.tripLength} - RM${String.format("%.2f", trip.tripFees)}/pax",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold,
                                fontFamily = CusFont3
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
                            Text(
                                text = "Available: ${trip.isAvailable}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = CusFont3
                            )

                            Column (
                                verticalArrangement = Arrangement.Bottom,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Row (
                                    horizontalArrangement = Arrangement.Center,
                                    modifier = Modifier.fillMaxWidth()
                                ){
                                    Button(onClick = {
                                        navController.navigate(route = Screen.UserBookingTripScreen.route)
                                    }) {
                                        Text(text = "Book Now")
                                    }
                                }
                            }
                        }
                    }


                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewUserBuyTripScreen() {
    UserViewTripScreen(navController = rememberNavController(), context = LocalContext.current, tripViewModel = TripViewModel())
}

