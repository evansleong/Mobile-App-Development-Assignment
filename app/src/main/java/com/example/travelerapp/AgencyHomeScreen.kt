package com.example.travelerapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberAsyncImagePainter
import com.example.travelerapp.data.Trip
import com.example.travelerapp.viewModel.AgencyViewModel
import com.example.travelerapp.viewModel.TripViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

@Composable
fun AgencyHomeScreen(
    navController: NavController,
    viewModel: AgencyViewModel,
    tripViewModel: TripViewModel
) {
    val db = Firebase.firestore

    val isLoggedIn = remember { mutableStateOf(true) }
    val loggedInAgency = viewModel.loggedInAgency

    val tripListState = remember { mutableStateOf<List<Trip>>(emptyList()) }

    LaunchedEffect(key1 = true) {
        tripViewModel.readTrip(db) { trips ->
            val filteredTrips = trips.filter { trip ->
                trip.agencyUsername == loggedInAgency?.agencyUsername
            }
            // Update the tripListState with the fetched trips
            tripListState.value = filteredTrips
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val title = "Welcome, ${loggedInAgency?.agencyUsername}"
        ReuseComponents.TopBar(
            title = title,
            navController,
            showLogoutButton = true,
            onLogout = {
            navController.navigate(route = Screen.UserOrAdmin.route) {
                popUpTo(Screen.UserOrAdmin.route) {
                    inclusive = true
                }
            }
        })

        Spacer(modifier = Modifier.height(20.dp))

        // Current date and sold packages statistic
        val currentDate = remember {
            SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(Date())
        }
        Text(
            text = "Today",
            modifier = Modifier.padding(bottom = 4.dp, start = 15.dp),
            fontSize = 14.sp // Smaller font size for "Today" text
        )
        Text(
            text = currentDate,
            modifier = Modifier.padding(bottom = 4.dp, start = 15.dp),
            fontSize = 18.sp // Larger font size for "current date" text
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = " Pkgs booked",
            modifier = Modifier
                .align(Alignment.End),
            color = Color.Red,
            fontWeight = FontWeight.Bold,
            fontSize = 25.sp
        )

        // Divider below the "pkgs booked" text
        Divider(
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "Your travel package list",
            modifier = Modifier.padding(start = 15.dp),
            fontSize = 18.sp // Smaller font size for "Today" text
        )

        // User travel package list slider
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 16.dp)
        ) {
            // Horizontal list of user travel packages
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 8.dp)
            ) {
                items(tripListState.value.take(3)) { trip ->
                    HomeTripItem(trip = trip, navController = navController, tripViewModel)
                }
            }

            Button(
                onClick = {
                    navController.navigate(route = Screen.AgencyPackageList.route) {
                        popUpTo(Screen.Home.route) {
                            inclusive = true
                        }
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .height(90.dp)
                    .width(120.dp)
                    .padding(20.dp)
                    .offset(y = (-300).dp)
            ) {
                Text(
                    text = ">",
                    fontSize = 25.sp
                )
            }

            Button(
                onClick = {
                        navController.navigate(route = Screen.AgencyAddPackage.route) {

                        }
                    },
                    modifier = Modifier.align(Alignment.BottomEnd)
                ) {
                    Text(
                        text = "Add package",
                        fontSize = 25.sp
                    )
                }
        }
        ReuseComponents.AgencyNavBar(text = title, navController = navController)
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun HomeTripItem(
    trip: Trip,
    navController: NavController,
    tripViewModel: TripViewModel,
    ) {
    val painter: Painter = rememberAsyncImagePainter(trip.tripUri)

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
                .fillMaxWidth(),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = trip.tripName,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = trip.tripLength
            )
        }
    }
}


@Preview
@Composable
fun PreviewAgencyHomeScreen() {
    val email = "John Doe"
    AgencyHomeScreen(
        navController = rememberNavController(),
        viewModel = AgencyViewModel(),
        tripViewModel = TripViewModel()
    )
}
