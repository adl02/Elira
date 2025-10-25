package com.howtokaise.elira.presentation.page

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.howtokaise.elira.AppUtil
import com.howtokaise.elira.model.ProductModel
import com.howtokaise.elira.model.UserModel

@Composable
fun CheckoutPage(modifier: Modifier = Modifier) {

    val userModel = remember { mutableStateOf(UserModel()) }
    val productList = remember { mutableStateListOf(ProductModel()) }
    val subTotal = remember { mutableStateOf(0f) }
    val discount = remember { mutableStateOf(0f) }
    val tax = remember { mutableStateOf(0f) }
    val total = remember { mutableStateOf(0f) }

    val isDarkTheme = isSystemInDarkTheme()
    val backgroundColor = if (isDarkTheme) Color.Black else Color.White

    fun calculateAndAssign() {
        productList.forEach {
            if (it.actualPrice.isNotEmpty()) {
                val qyt = userModel.value.cartItems[it.id] ?: 0
                subTotal.value += it.actualPrice.toFloat() * qyt
            }
        }

        discount.value = subTotal.value * (AppUtil.getDiscountPercentage()) / 100
        tax.value = subTotal.value * (AppUtil.getTaxPercentage() / 100)
        total.value = "%.2f".format(subTotal.value - discount.value + tax.value).toFloat()
    }

    LaunchedEffect(Unit) {
        Firebase.firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid!!)
            .get().addOnCompleteListener {
                if (it.isSuccessful) {
                    val result = it.result.toObject(UserModel::class.java)
                    if (result != null) {
                        userModel.value = result

                        Firebase.firestore.collection("data")
                            .document("stock").collection("products")
                            .whereIn("id", userModel.value.cartItems.keys.toList())
                            .get().addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    if (task.isSuccessful) {
                                        val resultProducts =
                                            task.result.toObjects(ProductModel::class.java)
                                        productList.addAll(resultProducts)
                                        calculateAndAssign()
                                    }
                                }
                            }
                    }
                }
            }
    }

    val address = userModel.value.address

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp)
    ) {
        Text(text = "Checkout", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Deliver to:", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Spacer(Modifier.height(4.dp))
        Text(text = address.fullName, fontWeight = FontWeight.Medium)
        Text(text = address.phone)
        Text(text = "${address.roadName}, ${address.city}, ${address.state} - ${address.pincode}")

        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(16.dp))
        RowCheckoutItems(title = "Subtotal", value = subTotal.value.toString())
        Spacer(modifier = Modifier.height(8.dp))
        RowCheckoutItems(title = "Discount (-)", value = discount.value.toString())
        Spacer(modifier = Modifier.height(8.dp))
        RowCheckoutItems(title = "Tax (+)", value = tax.value.toString())
        HorizontalDivider()

        Spacer(Modifier.height(16.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "To Pay",
            textAlign = TextAlign.Center
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "₹" + total.value.toString(),
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                AppUtil.startPayment(total.value)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFF349137))
        ) {
            Text(text = "Pay Now", color = Color.White, fontSize = 16.sp)
        }
    }
}

@Composable
fun RowCheckoutItems(title: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = title, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
        Text(text = "₹" + value, fontSize = 18.sp)
    }
}