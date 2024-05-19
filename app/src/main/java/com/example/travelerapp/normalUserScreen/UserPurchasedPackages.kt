package com.example.travelerapp.normalUserScreen

import ReuseComponents
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.FlowColumnScopeInstance.weight
//import androidx.compose.foundation.layout.FlowColumnScopeInstance.weight
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.travelerapp.data.PurchasedTrip
import com.example.travelerapp.data.Trip
import com.example.travelerapp.screen.Screen
import com.example.travelerapp.viewModel.PurchasedTripViewModel
import com.example.travelerapp.viewModel.TripViewModel
import com.example.travelerapp.viewModel.UserViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun UserPackagePurchased(
    navController: NavController,
    context: Context,
    userViewModel: UserViewModel,
    tripViewModel: TripViewModel,
    purchasedTripViewModel: PurchasedTripViewModel
) {
    val title = "Your Packages"
    val db = Firebase.firestore
    val currentUser = userViewModel.loggedInUser?.userId
    val tripListStatePT = remember {
        mutableStateOf<List<Trip>>(emptyList())
    }

    val pTState = remember {
        mutableStateOf<List<PurchasedTrip>>(emptyList())
    }

    LaunchedEffect(key1 = true) {
        purchasedTripViewModel.readPTTrip(db) { purchasedTrips ->
            val filteredPT = purchasedTrips.filter {
                it.userId == currentUser
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
            ReuseComponents.TopBar(title = title, navController = navController, showBackButton = true)

            Text(
                text = "Purchased Trip",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp)
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(16.dp),
            ) {
                items(tripListStatePT.value) { trip ->
                    val pt = pTState.value.filter { pt -> pt.tripId.equals(trip.tripId) }
                    PTripListItem(
                        bookedUsers = pt.get(0).noPax,
                        trip = trip,
                        navController = navController,
                        tripViewModel = tripViewModel,
                        purchasedTripViewModel = purchasedTripViewModel
                    )
                }
            }
                Text("Number of trips: \n ${tripListStatePT.value.size}",
                    modifier = Modifier
                        .fillMaxWidth(),
                    color = Color(0xff9f9f9f),
                    textAlign = TextAlign.Center
                )

                ReuseComponents.NavBar(text = title, navController = navController)
        }
    }
}

@Composable
fun PTripListItem(
    bookedUsers: Int,
    trip: Trip,
    navController: NavController,
    tripViewModel: TripViewModel,
    purchasedTripViewModel: PurchasedTripViewModel,
    onRefresh: () -> Unit ={}
) {
    val painter: Painter = rememberAsyncImagePainter(model = Uri.parse(trip.tripUri))

    Card(
        modifier = Modifier
            .height(130.dp)
            .clickable {
                purchasedTripViewModel.numPax = bookedUsers
                tripViewModel.selectedTripId = trip.tripId
                navController.navigate(route = Screen.UserPackageDetails.route)
            },
        shape = RoundedCornerShape(20.dp), // Rounded corners
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
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
                        text = formatDateRange(trip.depDate, trip.retDate),
                        fontSize = 14.sp
                    )
                    Text(
                        text = trip.tripLength,
                        fontSize = 14.sp
                    )
                }

            }
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}

fun formatDateRange(depDate: String, retDate: String): String {
    // Define the date format for parsing the original date strings
    val originalDateFormat = SimpleDateFormat("EEEE, d MMM, yyyy", Locale.ENGLISH)
    // Define the date format for formatting the new date strings
    val targetDateFormat = SimpleDateFormat("EEE, MMM d", Locale.ENGLISH)

    // Parse the departure date
    val depCal = Calendar.getInstance()
    depCal.time = originalDateFormat.parse(depDate) ?: Date()
    val depDayOfWeek = depCal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.ENGLISH)
    val depDayOfMonth = depCal.get(Calendar.DAY_OF_MONTH)
    val depMonth = depCal.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.ENGLISH)
    val depYear = depCal.get(Calendar.YEAR)

    // Parse the return date
    val retCal = Calendar.getInstance()
    retCal.time = originalDateFormat.parse(retDate) ?: Date()
    val retDayOfWeek = retCal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.ENGLISH)
    val retDayOfMonth = retCal.get(Calendar.DAY_OF_MONTH)
    val retMonth = retCal.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.ENGLISH)
    val retYear = retCal.get(Calendar.YEAR)

    // Format the dates in the desired format
    val depDateString = "${depDayOfWeek}, ${depMonth} ${depDayOfMonth}"
    val retDateString = "${retDayOfWeek}, ${retMonth} ${retDayOfMonth}, ${retYear}"

    return "${targetDateFormat.format(depCal.time)} - ${targetDateFormat.format(retCal.time)}, ${depYear}"
}

@Preview
@Composable
fun PPPreview(){
    UserPackagePurchased(
        navController = rememberNavController(),
        context = LocalContext.current,
        userViewModel = UserViewModel(),
        tripViewModel = TripViewModel(),
        purchasedTripViewModel = PurchasedTripViewModel()
    )
}