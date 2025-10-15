package com.howtokaise.elira.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.howtokaise.elira.presentation.authentication.AuthScreen
import com.howtokaise.elira.presentation.authentication.LoginScreen
import com.howtokaise.elira.presentation.authentication.SignupScreen

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "auth") {

        composable("auth"){
            AuthScreen(modifier,navController)
        }

        composable("signup"){
            SignupScreen(modifier)
        }

        composable("login"){
            LoginScreen(modifier)
        }
    }
}