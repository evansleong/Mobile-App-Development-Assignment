package com.example.travelerapp

import SignUpScreen
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun SetUpNavGraph(
    navController: NavHostController,
    dbHandler: DBHandler
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
            LoginScreen(navController,dbHandler)
        }
        composable(
            route = Screen.Signup.route
        ){
            SignUpScreen(navController)
        }
        composable(
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
        ) {
            SettingsScreen(navController)
        }
        composable(
            route = Screen.AgencyHome.route
        ){ backStackEntry ->
            // Extracting arguments from NavBackStackEntry
            val loggedInUserName = backStackEntry.arguments?.getString("loggedInUserName")
            // Passing the argument to AgencyHomeScreen
            AgencyHomeScreen(navController, loggedInUserName ?: "", soldPackagesCount = 0)
        }
        composable(
            route = Screen.AgencyAddPackage.route
        ) {
            AgencyAddPackageScreen(navController = navController, context = LocalContext.current)
        }
        composable(
            route = Screen.AgencyPackageList.route
        ) {
            AgencyPackageList(navController, context = LocalContext.current)
        }
        composable(
            route = Screen.Reload.route
        ) {
            ReloadScreen(navController)
        }
    }
}
