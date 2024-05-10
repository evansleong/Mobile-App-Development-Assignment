package com.example.travelerapp

import AddPINScreen
import SignUpScreen
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.travelerapp.data.Trip
import com.example.travelerapp.viewModel.AgencyViewModel
import com.example.travelerapp.viewModel.ReviewViewModel
import com.example.travelerapp.viewModel.TransactionViewModel
import com.example.travelerapp.viewModel.TripViewModel
import com.example.travelerapp.viewModel.WalletViewModel
import com.example.travelerapp.viewModel.UserViewModel

@Composable
fun SetUpNavGraph(
    navController: NavHostController,
    dbHandler: DBHandler
) {
    val agencyViewModel: AgencyViewModel = viewModel()
    val tripViewModel: TripViewModel = viewModel()
    val userViewModel: UserViewModel = viewModel()
    val walletViewModel: WalletViewModel = viewModel()
    val reviewViewModel: ReviewViewModel = viewModel()
    val transactionViewModel: TransactionViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.UserOrAdmin.route
//        startDestination = Screen.Login.route
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
            LoginScreen(navController, context = LocalContext.current, userViewModel, walletViewModel)
        }
        composable(
            route = Screen.Signup.route
        ){
            SignUpScreen(navController, context = LocalContext.current, dbHandler, userViewModel)
        }
        composable(
            route = Screen.AddPIN.route
        ){
            AddPINScreen(navController, context = LocalContext.current, walletViewModel)
        }
        composable(
            route = Screen.Review.route
        ){
            ReviewScreen(navController, context = LocalContext.current, userViewModel, reviewViewModel, transactionViewModel)
        }
        composable(
             route = Screen.EditReview.route
        ){ backStackEntry ->
            EditReviewScreen(navController, context = LocalContext.current, reviewViewModel, tripViewModel)
        }
        composable(
            route = Screen.Package.route
        ){
            PackageScreen(navController)
        }
        composable(
            route = Screen.Wallet.route
        ){
            WalletScreen(navController, context = LocalContext.current, walletViewModel, transactionViewModel)
        }
        composable(
            route = Screen.Transaction.route
        ) {
            TransactionScreen(navController, context = LocalContext.current, transactionViewModel)
        }
        composable(
            route = Screen.Reload.route
        ) {
            ReloadScreen(navController, context = LocalContext.current, walletViewModel, transactionViewModel)
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
            AgencySignUpScreen(navController, context = LocalContext.current, viewModel = agencyViewModel)
        }
        composable(
            route = Screen.AgencyHome.route
        ){
            AgencyHomeScreen(navController, viewModel = agencyViewModel, tripViewModel = tripViewModel)
        }
        composable(
            route = Screen.AgencyAddPackage.route
        ) {
            AgencyAddPackageScreen(navController = navController, context = LocalContext.current, viewModel = agencyViewModel, tripViewModel = tripViewModel)
        }
        composable(
            route = Screen.AgencyPackageList.route
        ) {
            AgencyPackageList(navController, context = LocalContext.current, viewModel = agencyViewModel, tripViewModel = tripViewModel)
        }
        composable(
            route = Screen.AgencyPackageDetail.route
        ) {
            AgencyPackageDetail(navController, context = LocalContext.current, tripViewModel = tripViewModel)
        }
        composable(
            route = Screen.AgencyEditPackage.route
        ) {
            AgencyEditPackageScreen(navController, trip = Trip(), context = LocalContext.current, tripViewModel = tripViewModel)
        }
        composable(
            route = Screen.Reload.route
        ) {
            ReloadScreen(navController, context = LocalContext.current, walletViewModel, transactionViewModel)
        }
    }
}
