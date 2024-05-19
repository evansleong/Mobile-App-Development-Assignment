package com.example.travelerapp.normalUserScreen

import ReuseComponents
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun PackageScreen(
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val title = "Package"
        ReuseComponents.TopBar(title = title, navController)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = title)
        }

        ReuseComponents.NavBar(text = title, navController = navController)
    }
}

@Composable
@Preview
fun PackageScreenPreview(){
    PackageScreen(
        navController = rememberNavController()
    )
}