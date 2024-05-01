package com.example.travelerapp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun PickUserTypeScreen(
    navController: NavController
) {
    var selectedOption by remember { mutableStateOf("User") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Choose User Type",
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(
                    color = if (selectedOption == "User") Color.Blue else Color.LightGray,
                    shape = MaterialTheme.shapes.medium
                )
                .clickable {
                    navController.navigate(route = Screen.Login.route)
                }
        ) {
            Text(
                text = "User",
                color = Color.White,
                modifier = Modifier.padding(16.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(
                    color = if (selectedOption == "Agency") Color.Blue else Color.LightGray,
                    shape = MaterialTheme.shapes.medium
                )
                .clickable {
                    navController.navigate(route = Screen.AgencyLogin.route)
                }
        ) {
            Text(
                text = "Agency",
                color = Color.White,
                modifier = Modifier.padding(16.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Selected Option: $selectedOption",
        )
    }
}