package com.example.travelerapp.agencyScreen

//import androidx.compose.ui.graphics.Paint
import ReuseComponents
import ReuseComponents.TopBar
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.travelerapp.R
import com.example.travelerapp.data.Trip
import com.example.travelerapp.screen.Screen
import com.example.travelerapp.ui.theme.CusFont3
import com.example.travelerapp.viewModel.AgencyViewModel
import com.example.travelerapp.viewModel.TripViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AgencyHomeScreen(
    navController: NavController,
    viewModel: AgencyViewModel,
    tripViewModel: TripViewModel
) {
    val db = Firebase.firestore

    val loggedInAgency = viewModel.loggedInAgency

    val top3TripsState = remember { mutableStateOf<List<Trip>>(emptyList()) }
    val tripListState = remember { mutableStateOf<List<Trip>>(emptyList()) }
    val totalUsersState = remember { mutableStateOf(0) }


    // Intercept the back button press to prevent navigating back
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // Intercept the back button press to prevent navigating back and show a Snackbar
    BackHandler {
        coroutineScope.launch {
            snackbarHostState.showSnackbar("Cannot log out by pressing the back button")
        }
    }

    LaunchedEffect(key1 = true) {
        tripViewModel.readTrip(db) { trips ->
            val filteredTrips = trips.filter { trip ->
                trip.agencyId == loggedInAgency?.agencyId
            }
            // Update the tripListState with the fetched trips
            tripListState.value = filteredTrips
        }

        tripViewModel.readPurchasedTrips(
            db, loggedInAgency?.agencyId ?: "",
            loggedInAgency?.agencyId ?: ""
        ) { totalUsers ->
            totalUsersState.value = totalUsers
        }

        tripViewModel.readTripsWithBookingCount(
            db, loggedInAgency?.agencyUsername ?: "",
            loggedInAgency?.agencyId ?: ""
        ) { trips ->
            tripListState.value = trips
            val sortedTrips = trips.sortedByDescending { it.noOfUserBooked }
            top3TripsState.value = sortedTrips.take(3)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopBar(
                title = "Welcome, ${loggedInAgency?.agencyUsername}",
                navController,
                showLogoutButton = true,
                isAgencySide = true,
                onLogout = {
                    navController.navigate(route = Screen.UserOrAdmin.route) {
                        popUpTo(Screen.UserOrAdmin.route) {
                            inclusive = true
                        }
                    }
                }
            )
        },
        bottomBar = {
            ReuseComponents.AgencyNavBar(text = "AgencyHome", navController = navController)
        },
        floatingActionButtonPosition = FabPosition.End
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(40f / 9f)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(R.drawable.traveler_banner),
                    contentDescription = "Traveler banner",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            val currentDate = remember {
                SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(Date())
            }
            Text(
                text = "Today",
                fontFamily = CusFont3,
                modifier = Modifier.padding(bottom = 4.dp, start = 15.dp),
                fontSize = 14.sp,
            )
            Text(
                text = currentDate,
                fontFamily = CusFont3,
                modifier = Modifier.padding(bottom = 4.dp, start = 15.dp),
                fontSize = 18.sp // Larger font size for "current date" text
            )

            Spacer(modifier = Modifier.height(10.dp))
            Divider(color = Color.LightGray, thickness = 2.dp)

            Spacer(modifier = Modifier.height(20.dp))


            // Top 3 most booked trips
            Text(
                text = "Top 3 Most Booked Trips",
                modifier = Modifier.padding(bottom = 8.dp, start = 15.dp),
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = CusFont3
            )

            // User travel package list slider
            Box(
                modifier = Modifier
                    .padding(vertical = 16.dp)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 10.dp
                    )
                ) {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 8.dp)
                    ) {
                        if (top3TripsState.value.isEmpty()) {
                            item {
                                Text(
                                    text = "No trips available...",
                                    modifier = Modifier
                                        .padding(vertical = 50.dp, horizontal = 90.dp)
                                )
                            }
                        } else {
                            itemsIndexed(top3TripsState.value) { index, trip ->
                                if (trip.noOfUserBooked > 0) {
                                    AgencyHomeTop3Item(
                                        trip = trip,
                                        navController = navController,
                                        tripViewModel = tripViewModel,
                                        rank = index + 1 // Pass the rank (1, 2, or 3)
                                    )
                                } else {
                                    Text(
                                        text = "",
                                        modifier = Modifier.padding(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Your Travel Package List",
                    modifier = Modifier.padding(start = 15.dp),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = CusFont3
                )

                // Navigate to Package List button
                if (tripListState.value.isNotEmpty()) {
                    IconButton(onClick = {
                        navController.navigate(route = Screen.AgencyPackageList.route)
                    }
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowRight,
                            contentDescription = "Navigate to Package List",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                } else {
                    IconButton(onClick = {
                        navController.navigate(route = Screen.AgencyAddPackage.route)
                    }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Navigate to Add Package",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }

            // User travel package list slider
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 16.dp)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 10.dp
                    )
                ) {
                    // Horizontal list of user travel packages
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 8.dp)
                    ) {
                        // Add Package button if no packages
                        if (tripListState.value.isEmpty()) {
                            item {
                                Text(
                                    text = "No Travel Packages Yet...",
                                    modifier = Modifier
                                        .padding(vertical = 50.dp, horizontal = 90.dp)
                                )
                            }
                        } else {
                            items(tripListState.value.take(3)) { trip ->
                                AgencyHomeTripItem(
                                    trip = trip,
                                    navController = navController,
                                    tripViewModel
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalCoilApi::class)
@Composable
fun AgencyHomeTripItem(
    trip: Trip,
    navController: NavController,
    tripViewModel: TripViewModel,
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
//    val imageUrl = trip.tripUri
//    val painter = rememberAsyncImagePainter(
//        ImageRequest.Builder(LocalContext.current)
//            .data(trip.tripUri.takeIf { it.isNotEmpty() })
//            .crossfade(true)
//            .build()
//    )

    Card(
        modifier = Modifier
            .padding(15.dp)
            .width(200.dp)
            .clickable {
                tripViewModel.selectedTripId = trip.tripId
                navController.navigate(route = Screen.AgencyPackageDetail.route)
            },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 23.dp
        )
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .width(200.dp)
                .clickable {
                    tripViewModel.selectedTripId = trip.tripId
                    navController.navigate(route = Screen.AgencyPackageDetail.route)
                }
        ) {
            Image(
                painter = painter,
                contentDescription = trip.tripName,
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
        }
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = trip.tripName,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = trip.tripLength,
                fontSize = 13.sp,
                fontFamily = CusFont3
            )
        }
    }

}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun AgencyHomeTop3Item(
    trip: Trip,
    navController: NavController,
    tripViewModel: TripViewModel,
    rank: Int
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

    // Determine the border color based on the rank
    val borderColor = when (rank) {
        1 -> Color(0xFFFFD700) // Gold
        2 -> Color(0xFFC0C0C0) // Silver
        3 -> Color(0xFFCD7F32) // Bronze
        else -> Color.Transparent // No border for other items or if rank is null
    }

    Card(
        modifier = Modifier
            .padding(15.dp)
            .width(200.dp)
            .border(
                width = 4.dp,
                color = borderColor,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable {
                tripViewModel.selectedTripId = trip.tripId
                navController.navigate(route = Screen.AgencyPackageDetail.route)
            },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 23.dp
        )
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .width(200.dp)
                .clickable {
                    tripViewModel.selectedTripId = trip.tripId
                    navController.navigate(route = Screen.AgencyPackageDetail.route)
                }
        ) {
            Image(
                painter = painter,
                contentDescription = trip.tripName,
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop,
            )

            // Add logos for top 3 trips
            when (rank) {
                1 -> {
                    Text(
                        text = "Top1",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .background(Color(0xFFFFD700)) // Gold background color
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .align(Alignment.Start)
                    )
                }

                2 -> {
                    Text(
                        text = "Top2",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .background(Color(0xFFC0C0C0)) // Silver background color
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .align(Alignment.Start)
                    )
                }

                3 -> {
                    Text(
                        text = "Top3",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .background(Color(0xFFCD7F32)) // Bronze background color
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .align(Alignment.Start)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 5.dp)
            ) {
                Text(
                    text = trip.tripName,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Total no. of user booked: ${trip.noOfUserBooked}",
                    fontSize = 13.sp,
                    fontFamily = CusFont3
                )
            }
        }
    }
}


@Preview
@Composable
fun PreviewAgencyHomeScreen() {
    AgencyHomeScreen(
        navController = rememberNavController(),
        viewModel = AgencyViewModel(),
        tripViewModel = TripViewModel()
    )
}
