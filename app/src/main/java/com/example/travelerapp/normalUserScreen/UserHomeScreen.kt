package com.example.travelerapp.normalUserScreen

import ReuseComponents
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.FlowColumnScopeInstance.align
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
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
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    navController: NavController,
    tripViewModel: TripViewModel
) {
    val db = Firebase.firestore

    val tripListState = remember { mutableStateOf<List<Trip>>(emptyList()) }

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
        ReuseComponents.TopBar(title = title, navController,
            showLogoutButton = true,
            onLogout = {
                navController.navigate(route = Screen.UserOrAdmin.route) {
                    popUpTo(Screen.UserOrAdmin.route) {
                        inclusive = true
                    }
                }
            })

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .weight(1f),
//            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Recent Activities",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold, fontFamily = CusFont3),
                color = MaterialTheme.colorScheme.onSecondary
            )
//            Text(text = "calendar")
            Box(modifier = Modifier.padding(16.dp)
                    .clickable { navController.navigate(
                        route = Screen.UserPackagePurchased.route) }) {
                ReuseComponents.DatePicker()
            }
//            HeadingTxt("Explore Travel Package")
            Row (
                modifier = Modifier.fillMaxWidth()
//                    .padding(16.dp)
                ,
                verticalAlignment = Alignment.CenterVertically

            ){

                Text(
                    text = "Explore Travel Package",
                    modifier = Modifier
                        .padding(16.dp)
//                        .fillMaxWidth()
                        ,
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = CusFont3
                    ),
                    color = MaterialTheme.colorScheme.onSecondary
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = {
                    navController.navigate(route = Screen.UserDisplayPackageList.route)
                }
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = "Navigate to Package List",
                        tint = MaterialTheme.colorScheme.onBackground)
                }
            }

            // travel package list slider
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 16.dp)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    // Horizontal list of user travel packages
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .background(MaterialTheme.colorScheme.background),
                        contentPadding = PaddingValues(horizontal = 8.dp)
                    ) {
                        items(tripListState.value.take(3)) { trip ->
                            HomeTripItem(trip = trip, navController = navController, tripViewModel)
                        }
                    }
                }

//                IconButton(
//                    onClick = {
//                        navController.navigate(route = Screen.UserDisplayPackageList.route)
//                    },
//                    modifier = Modifier
//                        .align(Alignment.BottomEnd)
//                        .padding(20.dp)
//                        .offset(y = (-198).dp)
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.ArrowForward,
//                        contentDescription = "Navigate to Package List",
//                        tint = MaterialTheme.colorScheme.onBackground
//                    )
//                }
            }

//            Text(
//                modifier = Modifier.clickable {
//                    navController.navigate(route = Screen.AgencyHome.route)
//                },
//                text = "Agency Home Screen",
//                color = MaterialTheme.colorScheme.onSecondary)
//
//            Text(
//                modifier = Modifier.clickable {
//                    navController.navigate(route = Screen.AgencyAddPackage.route)
//                },
//                text = "Add Package Screen",
//                color = MaterialTheme.colorScheme.onSecondary)
        }

        ReuseComponents.NavBar(text = title, navController = navController)
        SnackbarHost(snackbarHostState)
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
            .clip(RoundedCornerShape(20.dp))
            .background(color = Color.Transparent)
            .clickable {
                tripViewModel.selectedTripId = trip.tripId
                navController.navigate(route = Screen.AgencyPackageDetail.route)
            }
    ) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
        ) {
            Surface(
                modifier = Modifier.fillMaxSize(),
//                color = Color(0xff48444d),
//                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .clickable {
                            tripViewModel.selectedTripId = trip.tripId
                            navController.navigate(route = Screen.UserViewTrip.route)
                        },
                ) {
                    Image(
                        painter = painter,
                        contentDescription = trip.tripName,
                        modifier = Modifier
                            .height(100.dp)
                            .clip(RoundedCornerShape(20.dp, 20.dp, 0.dp, 0.dp))
                            .fillMaxWidth()
                        ,
                        contentScale = ContentScale.Crop
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Text(
                            text = trip.tripName,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "RM${String.format("%.2f", trip.tripFees)}/pax",
                            style = TextStyle(fontSize = 11.sp, fontFamily = CusFont3)
                        )
                        Text(
                            text = trip.tripLength,
                            fontSize = 13.sp,
                            fontFamily = CusFont3
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview(){
    HomeScreen(
        navController = rememberNavController(),
        tripViewModel = TripViewModel()
    )
}