package com.example.travelerapp

sealed class Screen(val route: String) {
    object Home: Screen(route = "home_screen")
    object Login: Screen(route = "login_screen")
    object Signup: Screen(route = "signup_screen")


}