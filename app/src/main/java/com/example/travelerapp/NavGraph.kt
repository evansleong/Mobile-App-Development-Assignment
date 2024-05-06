package com.example.travelerapp

import AddPINScreen
import SignUpScreen
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

@Composable
fun SetUpNavGraph(
    navController: NavHostController,
    dbHandler: DBHandler
) {
    val agencyViewModel: AgencyViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.UserOrAdmin.route
//        startDestination = Screen.Review.route
    ) {
        composable(
            route = Screen.UserOrAdmin.route
        ){
            PickUserTypeScreen(navController)
        }
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
            route = Screen.AddPIN.route
        ){
            AddPINScreen(navController, context = LocalContext.current)
        }
        composable(
            route = Screen.Review.route
        ){
            ReviewScreen(navController, context = LocalContext.current)
        }
        composable(
             route = Screen.EditReview.route + "/{id}"
        ){ backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
            EditReviewScreen(navController, id ?: "", context = LocalContext.current)
        }
        composable(
            route = Screen.Package.route
        ){
            PackageScreen(navController)
        }
        composable(
            route = Screen.Wallet.route
        ){
            WalletScreen(navController, context = LocalContext.current)
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
            route = Screen.AgencyLogin.route
        ){
            AgencyLoginScreen(navController, context = LocalContext.current, viewModel = agencyViewModel)
        }
        composable(
            route = Screen.AgencySignup.route
        ){
            AgencySignUpScreen(navController, context = LocalContext.current)
        }
        composable(
            route = Screen.Login.route
        ){
            LoginScreen(navController,dbHandler)
        }
        composable(
            route = Screen.AgencyHome.route
        ){
            AgencyHomeScreen(navController, viewModel = agencyViewModel)
        }
        composable(
            route = Screen.AgencyAddPackage.route
        ) {
            AgencyAddPackageScreen(navController = navController, context = LocalContext.current, viewModel = agencyViewModel)
        }
        composable(
            route = Screen.AgencyPackageList.route
        ) {
            AgencyPackageList(navController, context = LocalContext.current, viewModel = agencyViewModel)
        }
        composable(
            route = Screen.Reload.route
        ) {
            ReloadScreen(navController, context = LocalContext.current)
        }
//        composable(
//            route = Screen.Testing.route
//        ) {
//            Testing(Modifier)
//        }
    }
}
