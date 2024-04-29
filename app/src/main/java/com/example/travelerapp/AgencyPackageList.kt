package com.example.travelerapp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
//import com.example.travelerapp.Screen.AgencyAddPackage

@Composable
fun AgencyPackageList(
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val title = "Travel Package Management"
        ReuseComponents.TopBar(title = title, navController, showBackButton = true)

        Spacer(modifier = Modifier.height(20.dp))

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Button(onClick = {
            }) {

            }
        }
    }
}

@Preview
@Composable
fun PreviewAgencyPackageList() {
    AgencyPackageList(navController = rememberNavController())
}