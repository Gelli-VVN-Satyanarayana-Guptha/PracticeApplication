package com.example.myandroidapp.navigation

sealed class Pages(val route : String){
    object Login : Pages("login_screen")
    object SignUp : Pages("signup_screen")
    object Home : Pages("home_screen")
}
