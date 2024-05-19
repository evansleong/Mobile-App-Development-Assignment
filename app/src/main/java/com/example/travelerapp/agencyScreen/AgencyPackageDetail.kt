package com.example.travelerapp.agencyScreen

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.travelerapp.R
import com.example.travelerapp.data.Trip
import com.example.travelerapp.screen.Screen
import com.example.travelerapp.ui.theme.CusFont3
import com.example.travelerapp.viewModel.TripViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

@Composable
fun AgencyPackageDetail(
    navController: NavController,
    context: Context,
    tripViewModel: TripViewModel
) {
    val db = Firebase.firestore
    val tripState = remember { mutableStateOf<Trip?>(null) }

    val selectedPackage = tripViewModel.selectedTripId

    val tripId = tripViewModel.selectedTripId.toString()

    var isDialogOpen by remember { mutableStateOf(false) }


    // Store all the pax and user data received from Firestore
    var purchasedTripsData by remember { mutableStateOf<List<Pair<Int, String>>?>(null) }

    // LaunchedEffect block to read purchased trips data
    LaunchedEffect(tripId) {
        tripViewModel.readPurchasedTripsUserAndNoPax(db, tripId) { result ->
            purchasedTripsData = result
//            fetchUsername(db, result)
        }
    }

    LaunchedEffect(selectedPackage) {
        tripViewModel.readSingleTrip(db, selectedPackage.toString()) { trip ->
            tripState.value = trip
        }
    }

    tripState.value?.let { trip ->
        Scaffold(
            topBar = {
                ReuseComponents.TopBar(
                    title = trip.tripName,
                    navController,
                    showBackButton = true,
                    showLogoutButton = true,
                    isAtSettingPage = true,
                    onLogout = {
                        navController.navigate(route = Screen.UserOrAdmin.route) {
                            popUpTo(Screen.UserOrAdmin.route) {
                                inclusive = true
                            }
                        }
                    })
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
                        val painter =
                            rememberAsyncImagePainter(
                                ImageRequest.Builder
                                    (LocalContext.current).data(data = trip.tripUri)
                                    .apply(block = fun ImageRequest.Builder.() {
                                        crossfade(true)
                                        placeholder(R.drawable.loading)
                                    }).build()
                            )

                        Image(
                            painter = painter,
                            contentDescription = trip.tripUri,
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(16f / 9f),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Card(
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFF5DB075), //Card background color
                                contentColor = Color.White  //Card content color,e.g.text
                            ),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 10.dp
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = trip.tripName,
                                    fontSize = 30.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )

                                Text(
                                    text = "${trip.depDate} to ${trip.retDate}",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Light,
                                    fontFamily = CusFont3
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Card(
                            modifier = Modifier
                                .height(350.dp)
                                .fillMaxWidth(),
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
                                    text = "Deposit : RM${String.format("%.2f", trip.tripDeposit)}",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
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
                                            imageVector = Icons.Filled.ArrowForward,
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

                        Row (
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ){
                            Button(
                                onClick = {
                                    navController.navigate(route = Screen.AgencyEditPackage.route)
                                }
                            ) {
                                Text("Edit Package")
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Button to open the dialog
                            Button(
                                onClick = { isDialogOpen = true }
                            ) {
                                Text("Check Buyer Details")
                            }
                        }

                        // Dialog to display buyer details
                        if (isDialogOpen) {
                            AlertDialog(
                                onDismissRequest = { isDialogOpen = false },
                                title = { Text("Buyer Details") },
                                text = {
                                    LazyColumn(
                                        modifier = Modifier.padding((16.dp))
                                    ) {
                                        // Display the pax and user IDs received from Firestore
                                        purchasedTripsData?.let { data ->
                                            var totalPax = 0
                                            item {
                                                Column {
                                                    data.forEachIndexed { index, (pax, user) ->
                                                        totalPax += pax
                                                        Text("${index + 1}. \t \t $user x $pax")
                                                        Spacer(
                                                            modifier = Modifier
                                                                .height(20.dp)
                                                        )
                                                    }
                                                }
                                            }
                                            // Display the total pax count after the list of user IDs
                                            item {
                                                Spacer(modifier = Modifier.height(20.dp))
                                                Text(
                                                    text = "Total Number Of User Booked: $totalPax",
                                                    fontSize = 15.sp,
                                                    fontWeight = FontWeight.ExtraBold
                                                    )
                                            }
                                        }
                                    }
                                },
                                confirmButton = {
                                    Button(
                                        onClick = { isDialogOpen = false }
                                    ) {
                                        Text("Close")
                                    }
                                }
                            )
                        }

                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewAgencyPackageDetail() {
    AgencyPackageDetail(navController = rememberNavController(), context = LocalContext.current, tripViewModel = TripViewModel())
}

