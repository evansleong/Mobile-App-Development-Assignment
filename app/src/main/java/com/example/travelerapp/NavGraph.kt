package com.example.travelerapp

import SignUpScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun SetUpNavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(
            route = Screen.Home.route
        ){
            HomeScreen(navController)
        }
        composable(
            route = Screen.Login.route
        ){
            LoginScreen(navController)
        }
        composable(
            route = Screen.Signup.route
        ){
            SignUpScreen(navController)
        }
        composable(
<<<<<<< Updated upstream
            route = Screen.Trip.route
        ){
            TripScreen(navController)
        }
        composable(
            route = Screen.Package.route
        ){
            PackageScreen(navController)
        }
        composable(
            route = Screen.Wallet.route
        ){
            WalletScreen(navController)
        }
        composable(
            route = Screen.Profile.route
        ){
            ProfileScreen(navController)
        }
        composable(
            route = Screen.Settings.route
        ){
            SettingsScreen(navController)
=======
            route = Screen.AgencyHome.route
        ){ backStackEntry ->
            // Extracting arguments from NavBackStackEntry
            val loggedInUserName = backStackEntry.arguments?.getString("loggedInUserName")
            // Passing the argument to AgencyHomeScreen
            AgencyHomeScreen(navController, loggedInUserName ?: "", onExpandClicked = {}, onPackageClicked = {}, soldPackagesCount = 0, userTravelPackages = listOf())
>>>>>>> Stashed changes
        }
    }
}
