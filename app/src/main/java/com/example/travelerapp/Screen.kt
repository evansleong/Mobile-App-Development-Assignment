package com.example.travelerapp

sealed class Screen(val route: String) {
    object Home: Screen(route = "home_screen")
    object Login: Screen(route = "login_screen")
    object Signup: Screen(route = "signup_screen")
<<<<<<< Updated upstream
    object Trip: Screen(route = "trip_screen")
    object Package: Screen(route = "package_screen")
    object Wallet: Screen(route = "wallet_screen")
    object Profile: Screen(route = "profile_screen")
    object Settings: Screen(route = "settings_screen")
=======
    object AgencyHome: Screen(route = "agency_home_screen")

>>>>>>> Stashed changes

}