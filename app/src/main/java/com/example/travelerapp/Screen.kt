package com.example.travelerapp

sealed class Screen(val route: String) {

    object UserOrAdmin: Screen(route = "user_or_admin_screen")
    object Home: Screen(route = "home_screen")
    object Login: Screen(route = "login_screen")
    object Signup: Screen(route = "signup_screen")
    object AddPIN: Screen(route = "addPIN_screen")
    object Review: Screen(route = "review_screen")
    object EditReview: Screen(route = "edit_review_screen")
    object Package: Screen(route = "package_screen")
    object Wallet: Screen(route = "wallet_screen")
    object Profile: Screen(route = "profile_screen")
    object Settings: Screen(route = "settings_screen")
    object AgencyLogin: Screen(route = "agency_login_screen")
    object AgencySignup: Screen(route = "agency_signup_screen")
    object AgencyHome: Screen(route = "agency_home_screen")
    object AgencyAddPackage: Screen(route = "agency_add_package_screen")
    object AgencyPackageList: Screen(route = "agency_package_list_screen")
    object AgencyPackageDetail: Screen(route = "agency_package_detail_screen")
    object Reload: Screen(route = "reload_screen")

}