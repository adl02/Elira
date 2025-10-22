package com.howtokaise.elira

import android.app.AlertDialog
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.howtokaise.elira.presentation.navigation.AppNavigation
import com.howtokaise.elira.presentation.navigation.GlobalNavigation
import com.howtokaise.elira.ui.theme.EliraTheme
import com.razorpay.PaymentResultListener

class MainActivity : ComponentActivity(), PaymentResultListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val isDarkTheme = isSystemInDarkTheme()
            val backgroundColor = if (isDarkTheme) Color.Black else Color.White
            EliraTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(backgroundColor)
                        .systemBarsPadding()
                ) { innerPadding ->
                    AppNavigation(Modifier.padding(innerPadding))
                }
            }
        }
    }

    override fun onPaymentSuccess(p0: String?) {
        AppUtil.clearCartAndAddToOrders()

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Payment Successful")
            .setMessage("Thank you! Your payment was completed successfully and your order has been placed")
            .setPositiveButton("OK"){_,_->
                val navController = GlobalNavigation.navController
                navController.popBackStack()
                navController.navigate("home")
            }
            .setCancelable(false)
            .show()

    }

    override fun onPaymentError(p0: Int, p1: String?) {
        AppUtil.showToast(this, "Payment Failed")

    }
}

