package com.example.travelerapp

import ReuseComponents
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.travelerapp.ui.theme.HeadingTxt

@Composable
fun HomeScreen(
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val title = "Home"
        ReuseComponents.TopBar(title = title, navController)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .weight(1f),
//            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HeadingTxt(text = "Recent Activities")
            Text(text = "calendar")
            HeadingTxt("Explore Travel Package")

            Text(
                modifier = Modifier.clickable {
                    navController.navigate(route = Screen.AgencyHome.route)
                },
                text = "Agency Home Screen")
            // Add other components as needed
        }

        ReuseComponents.NavBar(text = title, navController = navController)
    }
}

@Composable
@Preview
fun HomeScreenPreview(){
    HomeScreen(
        navController = rememberNavController()
    )
}