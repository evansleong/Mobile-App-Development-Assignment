package com.example.travelerapp

import ReuseComponents
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.travelerapp.data.Trip
import com.example.travelerapp.ui.theme.HeadingTxt
import com.example.travelerapp.viewModel.TripViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

@Composable
fun HomeScreen(
    navController: NavController,
    tripViewModel: TripViewModel
) {
    val db = Firebase.firestore

    val tripListState = remember { mutableStateOf<List<Trip>>(emptyList()) }

    LaunchedEffect(key1 = true) {
        tripViewModel.readTrip(db) { trips ->
            tripListState.value = trips.filter { trip ->
                trip.isAvailable != 0
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val title = "Home"
        ReuseComponents.TopBar(title = title, navController)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .weight(1f),
//            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HeadingTxt(text = "Recent Activities")
//            Text(text = "calendar")
            Box(modifier = Modifier.padding(16.dp)) {
                ReuseComponents.DatePicker()
            }
            HeadingTxt("Explore Travel Package")

            // travel package list slider
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 16.dp)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
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
                }

                IconButton(
                    onClick = {
                        navController.navigate(route = Screen.UserDisplayPackageList.route)
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(20.dp)
                        .offset(y = (-300).dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Navigate to Package List"
                    )
                }
            }

            Text(
                modifier = Modifier.clickable {
                    navController.navigate(route = Screen.AgencyHome.route)
                },
                text = "Agency Home Screen")

            Text(
                modifier = Modifier.clickable {
                    navController.navigate(route = Screen.AgencyAddPackage.route)
                },
                text = "Add Package Screen")
        }

        ReuseComponents.NavBar(text = title, navController = navController)
    }
}

@Composable
fun HomeTripItem(
    trip: Trip,
    navController: NavController,
    tripViewModel: TripViewModel,
) {
    val painter: Painter = rememberAsyncImagePainter(trip.tripUri)

    Card(
        modifier = Modifier
            .padding(8.dp)
            .width(200.dp)
            .clickable {
                tripViewModel.selectedTripId = trip.tripId
                navController.navigate(route = Screen.AgencyPackageDetail.route)
            }
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .width(200.dp)
                .clickable {
                    tripViewModel.selectedTripId = trip.tripId
                    navController.navigate(route = Screen.UserViewTrip.route)
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
                    text = "RM${trip.tripFees}/pax", // Displaying the price
                    style = TextStyle(fontSize = 12.sp) // Adjust font size as needed
                )
                Text(
                    text = trip.tripLength
                )
            }
        }
    }
}


@Composable
@Preview
fun HomeScreenPreview(){
    HomeScreen(
        navController = rememberNavController(),
        tripViewModel = TripViewModel()
    )
}