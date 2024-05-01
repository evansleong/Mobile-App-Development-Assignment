package com.example.travelerapp

import ReuseComponents
import android.content.Context
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ReviewScreen(
    navController: NavController,
    context: Context
) {
    var dbHandler: DBHandler = DBHandler(context)

//    val reviewList = dbHandler.getAllReview()

    val reviewList = listOf(
        Review(
            id = 1,
            title = "Great Experience",
            rating = 4.5,
            comment = "Had a wonderful experience with this service!",
            is_public = 1,
            imageUrl = "https://example.com/image1.jpg",
            created_at = 1620322112L // Assuming this is a timestamp
        ),
        Review(
            id = 2,
            title = "Needs Improvement",
            rating = 3.0,
            comment = "Service was okay, but could be better.",
            is_public = 0,
            imageUrl = "https://example.com/image2.jpg",
            created_at = 1620410212L // Assuming this is a timestamp
        ),
        Review(
            id = 3,
            title = "Excellent Service",
            rating = 5.0,
            comment = "Couldn't be happier with the service provided!",
            is_public = 1,
            imageUrl = "https://example.com/image3.jpg",
            created_at = 1620573012L // Assuming this is a timestamp
        )
    )
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
                items(reviewList) { review ->
                    ReviewItem(review = review, navController)
                }
            }
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
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
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
//        Text(text = "Trip Name: ${trip.tripName}, Fees: ${trip.tripFees}, Deposit: ${trip.tripDeposit}, Desc: ${trip.tripDesc}")
}
@Composable
@Preview(showSystemUi = true, showBackground = true)
fun ReviewScreenPreview() {
    ReviewScreen(
        navController = rememberNavController(),
        context = LocalContext.current
    )
}