package com.example.travelerapp

import ReuseComponents
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.travelerapp.data.Review
import com.example.travelerapp.data.Trip
import com.example.travelerapp.viewModel.ReviewViewModel
import com.example.travelerapp.viewModel.TransactionViewModel
import com.example.travelerapp.viewModel.TripViewModel
import com.example.travelerapp.viewModel.UserViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ReviewScreen(
    navController: NavController,
    context: Context,
    viewModel: UserViewModel,
    reviewViewModel: ReviewViewModel,
    transactionViewModel: TransactionViewModel
) {
    val db = Firebase.firestore

    val user = viewModel.loggedInUser

    val reviews = remember { mutableStateOf<List<Review>>(emptyList()) }

    LaunchedEffect(key1 = true) {
        reviewViewModel.readReviews(db) { lists ->
            val filteredReview = lists.filter { review ->
                review.user_id == user?.userId
            }
            // Update the tripListState with the fetched trips
            reviews.value = filteredReview
        }
    }
    val tripIds = remember { mutableStateOf<List<String>>(emptyList()) }

    LaunchedEffect(key1 = true) {
        transactionViewModel.readTxs(db) { lists ->
            val filteredId = lists.filter { tx ->
                user?.userId == tx?.user_id && tx?.trip_id != "null"
            }
            // Update the tripListState with the fetched trips
            tripIds.value = filteredId.map { it.trip_id }
        }
    }
    reviewViewModel.tripPurchasedId = tripIds.value

    var dbHandler: DBHandler = DBHandler(context)
//    val reviews = dbHandler.getAllReview()
//    val reviews = listOf(
//        Review(
//            id = "1",
//            trip_id = "",
//            user_id = "",
//            trip_name = "Pangkor",
//            title = "Great Experience",
//            rating = 4.0,
//            comment = "Had a wonderful experience with this service!",
//            is_public = 1,
//            imageUrls = "https://example.com/image1.jpg",
//            created_at = 1620322112L // Assuming this is a timestamp
//        ),
//        Review(
//            id = "2",
//            trip_id = "",
//            user_id = "",
//            trip_name = "Pangkor",
//            title = "Needs Improvement",
//            rating = 3.0,
//            comment = "Service was okay, but could be better.",
//            is_public = 0,
//            imageUrls = "https://example.com/image2.jpg",
//            created_at = 1620410212L // Assuming this is a timestamp
//        ),
//        Review(
//            id = "3",
//            trip_id = "",
//            user_id = "",
//            trip_name = "Pangkor",
//            title = "Excellent Service",
//            rating = 5.0,
//            comment = "Couldn't be happier with the service provided!",
//            is_public = 1,
//            imageUrls = "https://example.com/image3.jpg",
//            created_at = 1620573012L // Assuming this is a timestamp
//        )
//    )
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        val title = "Review"
        ReuseComponents.TopBar(title = title, navController)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                if (reviews.value.isEmpty()) {
                    item {
                        Text(
                            text = "No Review",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                } else {
                    items(reviews.value) { review ->
                        ReviewItem(review = review, navController)
                    }
                }
            }
        }
        FloatingActionButton(
            onClick = {
                if (tripIds.value.isEmpty()){
                    Toast.makeText(context, "No Purchased Trip", Toast.LENGTH_SHORT).show()
                } else {
                    reviewViewModel.review = null
                    navController.navigate(Screen.EditReview.route)

                }
            },
            modifier = Modifier
                .padding(16.dp)
                .padding(bottom = 16.dp, end = 16.dp)
                .size(56.dp)
                .background(color = Color.Transparent, shape = CircleShape),
            contentColor = Color.Black,
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Review",
                modifier = Modifier,
            )
        }
        ReuseComponents.NavBar(text = title, navController = navController)
    }
}

@Composable
fun ReviewItem(review: Review, navController: NavController) {
    // Display the trip information in each item

    Card(
        colors = CardDefaults.cardColors(Color.White),
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        val date = Date(review.created_at * 1000)
        val sdf = SimpleDateFormat("MMMM dd, yyyy EEE", Locale.getDefault())
        val formattedDate = sdf.format(date)
        Text(
            text = formattedDate,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Box(
            modifier = Modifier
                .padding(16.dp)
                .clickable {
                    navController.navigate("${Screen.EditReview.route}/${review.id.toString()}")
                },
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.wallet),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = review.title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    maxLines = 1,
                    modifier = Modifier
                        .background(color = Color.White)
                        .fillMaxWidth()
                )
            }
        }
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun ReviewScreenPreview() {
//    ReviewScreen(
//        navController = rememberNavController(),
//        context = LocalContext.current
//    )
}