package com.example.travelerapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AgencyHomeScreen(
    navController: NavController,
    loggedInUserName: String,
    soldPackagesCount: Int,
    userTravelPackages: List<String>,
    onPackageClicked: (String) -> Unit,
    onExpandClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Welcome message
        Text(
            text = "Welcome, $loggedInUserName",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Current date and sold packages statistic
        val currentDate = remember {
            SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(Date())
        }
        Text(
            text = "Today\n$currentDate\nSold Packages: $soldPackagesCount",
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // User travel package list slider
        Box(
            modifier = Modifier
                .weight(1f)
                .background(Color.LightGray)
                .padding(vertical = 16.dp)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(userTravelPackages) { packageItem ->
                    Text(
                        text = packageItem,
                        modifier = Modifier.padding(vertical = 8.dp),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Button(
                onClick = onExpandClicked,
                modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp)
            ) {
                Text(text = "Expand")
            }
        }
    }
}

@Preview
@Composable
fun PreviewAgencyHomeScreen() {
    val loggedInUserName = "John Doe"
    val soldPackagesCount = 10
    val userTravelPackages = listOf("Package 1", "Package 2", "Package 3")
    AgencyHomeScreen(
        navController = rememberNavController(),
        loggedInUserName = loggedInUserName,
        soldPackagesCount = soldPackagesCount,
        userTravelPackages = userTravelPackages,
        onPackageClicked = {},
        onExpandClicked = {}
    )
}
