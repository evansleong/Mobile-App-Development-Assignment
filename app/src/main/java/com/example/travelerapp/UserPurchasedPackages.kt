package com.example.travelerapp

import ReuseComponents
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.travelerapp.viewModel.TripViewModel
import com.example.travelerapp.viewModel.UserViewModel

@Composable
fun UserPackagePurchased(
    navController: NavController,
    userViewModel: UserViewModel,
    tripViewModel: TripViewModel
){
    val title = "Your Packages"
    val currentUser = userViewModel.loggedInUser?.userEmail
    val purchasedTrips =
    Column (modifier = Modifier
        .fillMaxSize()
    ){
        ReuseComponents.TopBar(title = title, navController = navController)

        Text(text = "Hi")


        Spacer(modifier = Modifier.weight(1f))
        ReuseComponents.NavBar(text = title, navController = navController)
    }

}

@Preview
@Composable
fun PPPreview(){
    UserPackagePurchased(
        navController = rememberNavController(),
        userViewModel = UserViewModel(),
        tripViewModel = TripViewModel()
    )
}