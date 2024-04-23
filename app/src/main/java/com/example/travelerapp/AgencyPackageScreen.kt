package com.example.travelerapp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun AgencyPackageScreen(
    navController : NavController
){
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val title = "Package"
        ReuseComponents.TopBar(title = title, navController, showBackButton = true)
    }
}

