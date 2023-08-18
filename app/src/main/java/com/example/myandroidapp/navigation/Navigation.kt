package com.example.myandroidapp.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.myandroidapp.screens.Home
import com.example.myandroidapp.screens.Login
import com.example.myandroidapp.screens.SignUp

@Composable
fun Navigation(context: Context){
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Pages.Login.route
    ) {
        composable(route = Pages.Login.route){
            Login(navController)
        }
        composable(route = Pages.SignUp.route){
            SignUp(context, navController)
        }
        composable(route = Pages.Home.route){
            Home(navController)
        }
    }
}