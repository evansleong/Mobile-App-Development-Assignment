package com.example.travelerapp.normalUserScreen

import ReuseComponents
import android.app.Activity
import android.content.Context
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.travelerapp.data.Trip
import com.example.travelerapp.localDb.DBHandler
import com.example.travelerapp.screen.Screen
import com.example.travelerapp.ui.theme.CusFont3
import com.example.travelerapp.viewModel.TripViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

@Composable
fun UserPackageListScreen(
    navController: NavController,
    context: Context,
    tripViewModel: TripViewModel
) {
    val db = Firebase.firestore
    val activity = context as Activity

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
            .background(MaterialTheme.colorScheme.background)
    ) {
        val dbHandler = DBHandler(context)
        val title = "Travel Package"
        ReuseComponents.TopBar(title = title, navController)
        Column (
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(tripListState.value) { trip ->
                    PurchaseableTripItem(trip = trip, navController = navController, tripViewModel)
                }
            }
        }
        ReuseComponents.NavBar(text = title, navController = navController)
    }
}

@Composable
fun PurchaseableTripItem(
    trip: Trip,
    navController: NavController,
    tripViewModel: TripViewModel,
) {
    val painter: Painter = rememberAsyncImagePainter(model = Uri.parse(trip.tripUri))

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable {
                tripViewModel.selectedTripId = trip.tripId
                navController.navigate(route = Screen.UserViewTrip.route)
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
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = "RM${String.format("%.2f", trip.tripFees)}/PAX",
                        style = TextStyle(fontSize = 13.sp, fontFamily = CusFont3)
                    )
                    Text(
                        text = trip.agencyUsername,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = trip.tripLength,
                        fontSize = 13.sp,
                        fontFamily = CusFont3
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Available: ${trip.isAvailable}",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}




@Preview
@Composable
fun PreviewUserPackageList() {
    UserPackageListScreen(navController = rememberNavController(), context = LocalContext.current, tripViewModel = TripViewModel())
}