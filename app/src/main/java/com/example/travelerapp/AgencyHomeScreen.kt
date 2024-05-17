package com.example.travelerapp

import ReuseComponents.TopBar
import android.graphics.Paint
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
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.travelerapp.data.PieChartInput
import com.example.travelerapp.data.Test
import com.example.travelerapp.data.Trip
import com.example.travelerapp.ui.theme.blueGray
import com.example.travelerapp.ui.theme.brightBlue
import com.example.travelerapp.ui.theme.gray
import com.example.travelerapp.ui.theme.green
import com.example.travelerapp.ui.theme.orange
import com.example.travelerapp.ui.theme.purple
import com.example.travelerapp.ui.theme.redOrange
import com.example.travelerapp.ui.theme.white
import com.example.travelerapp.viewModel.AgencyViewModel
import com.example.travelerapp.viewModel.TripViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlin.math.PI
import kotlin.math.atan2

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
    val totalUsersState = remember { mutableStateOf(0) }
    val pieChartDataState = remember { mutableStateOf<List<PieChartInput>>(emptyList()) }

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

//        tripViewModel.readPurchasedTripsForPieChart(db, loggedInAgency?.agencyUsername ?: "") { pieChartData ->
//            pieChartDataState.value = pieChartData
//        }
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

//            PieChart(
//                modifier = Modifier
//                    .size(150.dp),
//                input = pieChartDataState.value,
//                centerText = "${pieChartDataState.value.sumOf { it.value }} persons were asked"
//            )

            Text(
                text = " Pkgs booked: ${totalUsersState.value}",
                modifier = Modifier
                    .padding(bottom = 4.dp, start = 10.dp),
                color = Color.Red,
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp
            )

            // Divider below the "pkgs booked" text
            Divider(
                modifier = Modifier.padding(bottom = 5.dp)
            )

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Your travel package list",
                    modifier = Modifier.padding(start = 15.dp),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold
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
                                AgencyHomeTripItem(trip = trip, navController = navController, tripViewModel)
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
//fun PieChart(
//    modifier: Modifier = Modifier,
//    radius:Float = 175f,
//    innerRadius:Float = 90f,
//    transparentWidth:Float = 50f,
//    input:List<PieChartInput>,
//    centerText:String = ""
//) {
//    var circleCenter by remember {
//        mutableStateOf(Offset.Zero)
//    }
//
//    var inputList by remember {
//        mutableStateOf(input)
//    }
//    var isCenterTapped by remember {
//        mutableStateOf(false)
//    }
//
//    Box(
//        modifier = modifier,
//        contentAlignment = Alignment.Center
//    ){
//        Canvas(
//            modifier = Modifier
//                .fillMaxSize()
//                .pointerInput(true) {
//                    detectTapGestures(
//                        onTap = { offset ->
//                            val tapAngleInDegrees = (-atan2(
//                                x = circleCenter.y - offset.y,
//                                y = circleCenter.x - offset.x
//                            ) * (180f / PI).toFloat() - 90f).mod(360f)
//                            val centerClicked = if (tapAngleInDegrees < 90) {
//                                offset.x < circleCenter.x + innerRadius && offset.y < circleCenter.y + innerRadius
//                            } else if (tapAngleInDegrees < 180) {
//                                offset.x > circleCenter.x - innerRadius && offset.y < circleCenter.y + innerRadius
//                            } else if (tapAngleInDegrees < 270) {
//                                offset.x > circleCenter.x - innerRadius && offset.y > circleCenter.y - innerRadius
//                            } else {
//                                offset.x < circleCenter.x + innerRadius && offset.y > circleCenter.y - innerRadius
//                            }
//
//                            if (centerClicked) {
//                                inputList = inputList.map {
//                                    it.copy(isTapped = !isCenterTapped)
//                                }
//                                isCenterTapped = !isCenterTapped
//                            } else {
//                                val anglePerValue = 360f / input.sumOf {
//                                    it.value
//                                }
//                                var currAngle = 0f
//                                inputList.forEach { pieChartInput ->
//
//                                    currAngle += pieChartInput.value * anglePerValue
//                                    if (tapAngleInDegrees < currAngle) {
//                                        val description = pieChartInput.description
//                                        inputList = inputList.map {
//                                            if (description == it.description) {
//                                                it.copy(isTapped = !it.isTapped)
//                                            } else {
//                                                it.copy(isTapped = false)
//                                            }
//                                        }
//                                        return@detectTapGestures
//                                    }
//                                }
//                            }
//                        }
//                    )
//                }
//        ){
//            val width = size.width
//            val height = size.height
//            circleCenter = Offset(x= width/2f,y= height/2f)
//
//            val totalValue = input.sumOf {
//                it.value
//            }
//            val anglePerValue = 360f/totalValue
//            var currentStartAngle = 0f
//
//            inputList.forEach { pieChartInput ->
//                val scale = if(pieChartInput.isTapped) 1.1f else 1.0f
//                val angleToDraw = pieChartInput.value * anglePerValue
//                scale(scale){
//                    drawArc(
//                        color = pieChartInput.color,
//                        startAngle = currentStartAngle,
//                        sweepAngle = angleToDraw,
//                        useCenter = true,
//                        size = Size(
//                            width = radius*2f,
//                            height = radius*2f
//                        ),
//                        topLeft = Offset(
//                            (width-radius*2f)/2f,
//                            (height - radius*2f)/2f
//                        )
//                    )
//                    currentStartAngle += angleToDraw
//                }
//                var rotateAngle = currentStartAngle-angleToDraw/2f-90f
//                var factor = 1f
//                if(rotateAngle>90f){
//                    rotateAngle = (rotateAngle+180).mod(360f)
//                    factor = -0.92f
//                }
//
//                val percentage = (pieChartInput.value/totalValue.toFloat()*100).toInt()
//
//                drawContext.canvas.nativeCanvas.apply {
//                    if(percentage>3){
//                        rotate(rotateAngle){
//                            drawText(
//                                "$percentage %",
//                                circleCenter.x,
//                                circleCenter.y+(radius-(radius-innerRadius)/2f)*factor,
//                                Paint().apply {
//                                    textSize = 13.sp.toPx()
//                                    textAlign = Paint.Align.CENTER
//                                    color = white.toArgb()
//                                }
//                            )
//                        }
//                    }
//                }
//                if(pieChartInput.isTapped){
//                    val tabRotation = currentStartAngle - angleToDraw - 90f
//                    rotate(tabRotation){
//                        drawRoundRect(
//                            topLeft = circleCenter,
//                            size = Size(12f,radius*1.2f),
//                            color = gray,
//                            cornerRadius = CornerRadius(15f,15f)
//                        )
//                    }
//                    rotate(tabRotation+angleToDraw){
//                        drawRoundRect(
//                            topLeft = circleCenter,
//                            size = Size(12f,radius*1.2f),
//                            color = gray,
//                            cornerRadius = CornerRadius(15f,15f)
//                        )
//                    }
//                    rotate(rotateAngle){
//                        drawContext.canvas.nativeCanvas.apply {
//                            drawText(
//                                "${pieChartInput.description}: ${pieChartInput.value}",
//                                circleCenter.x,
//                                circleCenter.y + radius*1.3f*factor,
//                                Paint().apply {
//                                    textSize = 22.sp.toPx()
//                                    textAlign = Paint.Align.CENTER
//                                    color = white.toArgb()
//                                    isFakeBoldText = true
//                                }
//                            )
//                        }
//                    }
//                }
//            }
//
//            if(inputList.first().isTapped){
//                rotate(-90f){
//                    drawRoundRect(
//                        topLeft = circleCenter,
//                        size = Size(12f,radius*1.2f),
//                        color = gray,
//                        cornerRadius = CornerRadius(15f,15f)
//                    )
//                }
//            }
//            drawContext.canvas.nativeCanvas.apply {
//                drawCircle(
//                    circleCenter.x,
//                    circleCenter.y,
//                    innerRadius,
//                    Paint().apply {
//                        color = white.copy(alpha = 0.6f).toArgb()
//                        setShadowLayer(10f,0f,0f, gray.toArgb())
//                    }
//                )
//            }
//
//            drawCircle(
//                color = white.copy(0.2f),
//                radius = innerRadius+transparentWidth/2f
//            )
//
//        }
//        Text(
//            centerText,
//            modifier = Modifier
//                .width(Dp(innerRadius / 1.5f))
//                .padding(25.dp),
//            fontWeight = FontWeight.SemiBold,
//            fontSize = 17.sp,
//            textAlign = TextAlign.Center
//        )
//
//    }
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
