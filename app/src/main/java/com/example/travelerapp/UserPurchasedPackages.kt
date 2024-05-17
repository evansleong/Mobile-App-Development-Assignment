package com.example.travelerapp

import ReuseComponents
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.FlowColumnScopeInstance.weight
//import androidx.compose.foundation.layout.FlowColumnScopeInstance.weight
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.travelerapp.data.PurchasedTrip
import com.example.travelerapp.data.Trip
import com.example.travelerapp.viewModel.PurchassedTripViewModel
import com.example.travelerapp.viewModel.TripViewModel
import com.example.travelerapp.viewModel.UserViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

@Composable
fun UserPackagePurchased(
    navController: NavController,
    context: Context,
    userViewModel: UserViewModel,
    tripViewModel: TripViewModel,
    purchassedTripViewModel: PurchassedTripViewModel
) {
    val title = "Your Packages"
    val db = Firebase.firestore
    val currentUser = userViewModel.loggedInUser?.userEmail
    val tripListStatePT = remember {
        mutableStateOf<List<Trip>>(emptyList())
    }

    val pTState = remember {
        mutableStateOf<List<PurchasedTrip>>(emptyList())
    }

    LaunchedEffect(key1 = true) {
        purchassedTripViewModel.readPTTrip(db) { purchasedTrips ->
            val filteredPT = purchasedTrips.filter {
                it.userEmail == currentUser
            }
//            Log.d("Check","$currentUser purchases ${filteredPT.size} + ${filteredPT}" )
            pTState.value = filteredPT
            val tripIds = filteredPT.map { it.tripId }.toSet()

            tripViewModel.readTrip(db) { trips ->
                val filteredTrips = trips.filter {
                    it.tripId in tripIds
                }
                tripListStatePT.value = filteredTrips
                Log.d("UserPackagePurchased", "Trips loaded: ${filteredTrips.size}")

            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column {

            ReuseComponents.TopBar(title = title, navController = navController)

//        Text(text = "Hi")
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(16.dp),
            ) {
                items(tripListStatePT.value) { trip ->
                    PTripListItem(
                        trip = trip,
                        navController = navController,
                        tripViewModel = tripViewModel
                    )
                }
                item {
                    Text("hi")
                }
            }
//                Spacer(modifier = Modifier.weight(1f))
                ReuseComponents.NavBar(text = title, navController = navController)

        }
    }
}

@Composable
fun PTripListItem(
    trip: Trip,
    navController: NavController,
    tripViewModel: TripViewModel,
    onRefresh: () -> Unit ={}
) {
    val painter: Painter = rememberAsyncImagePainter(model = Uri.parse(trip.tripUri))

//    val showDialog = remember { mutableStateOf(false) }

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
                    Text(
                        text = "No. of booked user: ",
                        fontSize = 15.sp
                    )
                }

            }
        }
    }
}

@Preview
@Composable
fun PPPreview(){
    UserPackagePurchased(
        navController = rememberNavController(),
        context = LocalContext.current,
        userViewModel = UserViewModel(),
        tripViewModel = TripViewModel(),
        purchassedTripViewModel = PurchassedTripViewModel()
    )
}