package com.howtokaise.elira.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.howtokaise.elira.presentation.authentication.AuthScreen
import com.howtokaise.elira.presentation.authentication.LoginScreen
import com.howtokaise.elira.presentation.authentication.SignupScreen
import com.howtokaise.elira.presentation.homescreen.HomeScreen
import com.howtokaise.elira.presentation.page.AddressPage
import com.howtokaise.elira.presentation.page.CategoryProductsPage
import com.howtokaise.elira.presentation.page.CheckoutPage
import com.howtokaise.elira.presentation.page.EditAddress
import com.howtokaise.elira.presentation.page.EditProfilePage
import com.howtokaise.elira.presentation.page.WishlistPage
import com.howtokaise.elira.presentation.page.OrdersPage
import com.howtokaise.elira.presentation.page.ProductDetailsPage

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {

    val navController = rememberNavController()
    GlobalNavigation.navController = navController
    val isLoggedIn = Firebase.auth.currentUser!=null
    val firstPage = if (isLoggedIn) "home" else "auth"

    NavHost(navController = navController, startDestination = firstPage) {

        composable("auth"){
            AuthScreen(modifier,navController)
        }

        composable("signup"){
            SignupScreen(modifier,navController)
        }

        composable("login"){
            LoginScreen(modifier,navController)
        }

        composable("home"){
            HomeScreen(modifier,navController)
        }

        composable("category-products/{categoryId}"){
            var categoryId = it.arguments?.getString("categoryId")
            CategoryProductsPage(modifier,categoryId?:"")
        }

        composable("product-details/{productId}"){
            var productId = it.arguments?.getString("productId")
            ProductDetailsPage(modifier,productId?:"")
        }

        composable("checkout"){
            CheckoutPage(modifier)
        }

        composable("orders"){
            OrdersPage(modifier)
        }

        composable("wishlist"){
            WishlistPage(modifier)
        }

        composable("address"){
            AddressPage(modifier)
        }

        composable("editaddress"){
            EditAddress(modifier)
        }

        composable("editProfile"){
            EditProfilePage(modifier)
        }
    }
}


object GlobalNavigation{
    lateinit var navController : NavHostController
}