package com.example.travelerapp

import ReuseComponents
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun AddReviewScreen(
    navController: NavController
){
    val title = "Review"
    var rating by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        ReuseComponents.TopBar(title = title, navController, showBackButton = true)

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            StarRatingInput(onRatingChanged = { newRating ->
                rating = newRating
            })
        }

        ReuseComponents.NavBar(text = title, navController = navController)
    }
}

//@Composable
//fun StarRatingInput(onRatingChanged: (Int) -> Unit) {
//    var selectedRating by remember { mutableStateOf(0) }
//
//    Row(verticalAlignment = Alignment.CenterVertically){
//        for(i in 1..5){
//            StarButton(
//                isSelected = i <= selectedRating,
//                onClick = { selectedRating = i },
//                modifier = Modifier.padding(end = 4.dp)
//            )
//        }
//    }
//
//    LaunchedEffect(selectedRating) {
//        onRatingChanged(selectedRating)
//    }
//}
//
//@Composable
//fun StarButton(
//    isSelected: Boolean,
//    onClick: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    val starIcon = if(isSelected) Icons.Filled.Star else Icons.Outlined.Star
//    val starColor = if(isSelected) Color.Yellow else Color.Gray
//
//    IconButton(
//        onClick = onClick,
//        modifier = Modifier.size(48.dp)
//    ) {
//        Icon(imageVector = starIcon, contentDescription = null, tint = starColor)
//    }
//}