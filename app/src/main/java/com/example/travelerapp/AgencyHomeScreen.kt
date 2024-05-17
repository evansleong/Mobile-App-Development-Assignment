package com.example.travelerapp

import ReuseComponents.TopBar
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
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
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.travelerapp.data.Test
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
//    Testing
//    val charts = listOf(
//        Test(value = 20f, color = Color.Black),
//        Test(value = 30f, color = Color.Gray),
//        Test(value = 40f, color = Color.Green),
//        Test(value = 10f, color = Color.Red),
//    )


    val db = Firebase.firestore

    val isLoggedIn = remember { mutableStateOf(true) }
    val loggedInAgency = viewModel.loggedInAgency

    val tripListState = remember { mutableStateOf<List<Trip>>(emptyList()) }
    val totalUsersState = remember { mutableStateOf(0) }

    val purchasedTripsState = remember { mutableStateOf<List<Trip>>(emptyList()) }

    LaunchedEffect(key1 = true) {
        tripViewModel.readTrip(db) { trips ->
            val filteredTrips = trips.filter { trip ->
                trip.agencyUsername == loggedInAgency?.agencyUsername
            }
            // Update the tripListState with the fetched trips
            tripListState.value = filteredTrips
        }

        tripViewModel.readPurchasedTrips(db, loggedInAgency?.agencyUsername ?: "") { totalUsers ->
            totalUsersState.value = totalUsers
        }
    }

    Scaffold(
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
            val currentDate = remember {
                SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(Date())
            }
            Text(
                text = "Today",
                modifier = Modifier.padding(bottom = 4.dp, start = 15.dp),
                fontSize = 14.sp
            )
            Text(
                text = currentDate,
                modifier = Modifier.padding(bottom = 4.dp, start = 15.dp),
                fontSize = 18.sp // Larger font size for "current date" text
            )

            Spacer(modifier = Modifier.height(20.dp))

//            ChartCirclePie(modifier = Modifier, charts)


            Text(
                text = " Pkgs booked: ${totalUsersState.value}",
                modifier = Modifier
                    .padding(bottom = 15.dp),
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
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold
            )

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
                                AgencyHomeTripItem(trip = trip, navController = navController, tripViewModel)
                            }
                        }
                    }
                }

                // Navigate to Package List button
                if (tripListState.value.isNotEmpty()) {
                    FloatingActionButton(
                        onClick = {
                            navController.navigate(route = Screen.AgencyPackageList.route) {
                                popUpTo(Screen.Home.route) {
                                    inclusive = true
                                }
                            }
                        },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(20.dp)
                            .offset(y = (-270).dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "Navigate to Package List"
                        )
                    }
                } else {
                    FloatingActionButton(
                        onClick = {
                            navController.navigate(route = Screen.AgencyAddPackage.route) {
                                popUpTo(Screen.Home.route) {
                                    inclusive = true
                                }
                            }
                        },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(20.dp)
                            .offset(y = (-350).dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Navigate to Add Package"
                        )
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
        rememberAsyncImagePainter(ImageRequest.Builder
            (LocalContext.current).data(data = trip.tripUri).apply(block = fun ImageRequest.Builder.() {
            crossfade(true)
            placeholder(R.drawable.loading)
        }).build()
        )

    Card(
        modifier = Modifier
            .padding(15.dp)
            .width(200.dp)
            .clickable {
                tripViewModel.selectedTripId = trip.tripId
                navController.navigate(route = Screen.AgencyPackageDetail.route)
            },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 50.dp
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
}

//@Composable
//private fun ChartCirclePie(
//    modifier: Modifier,
//    charts: List<Test>,
//    size: Dp = 200.dp,
//    strokeWidth: Dp = 16.dp
//) {
//    val myText = "ChartCirclePie"
//    val textMeasurer = rememberTextMeasurer()
//    val textLayoutResult = textMeasurer.measure(text = AnnotatedString(myText))
//    val textSize = textLayoutResult.size
//
//    Canvas(modifier = modifier
//        .size(size)
//        .background(Color.LightGray)
//        .padding(12.dp), onDraw = {
//
//        var startAngle = 0f
//        var sweepAngle = 0f
//
//        charts.forEach {
//            val brush = createStripeBrush(
//                stripeColor = it.color,
//                stripeWidth = 2.dp,
//                stripeToGapRatio = 2f
//            )
//
//            sweepAngle = (it.value / 100) * 360
//
//            drawArc(
//                color = Color.Red,
//                startAngle = startAngle,
//                sweepAngle = sweepAngle,
//                useCenter = false,
//                style = Stroke(
//                    width = strokeWidth.toPx(),
//                    cap = StrokeCap.Round,
//                    join = StrokeJoin.Round
//                )
//            )
//
//            startAngle += sweepAngle
//        }
//
//        drawText(
//            textMeasurer, myText,
//            topLeft = Offset(
//                (this.size.width - textSize.width) / 2f,
//                (this.size.height - textSize.height) / 2f
//            ),
//        )
//    })
//
//}
//
//fun createStripeBrush(
//    stripeColor: Color,
//    stripeWidth: Dp,
//    stripeToGapRatio: Float
//): Brush {
//    return Brush.verticalGradient(
//        colors = listOf(stripeColor.copy(alpha = 0.6f), stripeColor.copy(alpha = 0.8f), stripeColor.copy(alpha = 1f)),
//        startY = 0f,
//        endY = stripeWidth.value * stripeToGapRatio
//    )
//}



@Preview
@Composable
fun PreviewAgencyHomeScreen() {
    AgencyHomeScreen(
        navController = rememberNavController(),
        viewModel = AgencyViewModel(),
        tripViewModel = TripViewModel()
    )
}
