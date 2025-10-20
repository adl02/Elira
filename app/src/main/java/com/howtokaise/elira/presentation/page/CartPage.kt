package com.howtokaise.elira.presentation.page

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.howtokaise.elira.model.UserModel
import com.howtokaise.elira.presentation.components.CartItemView

@Composable
fun CartPage(modifier: Modifier = Modifier) {

    val userModel = remember { mutableStateOf(UserModel()) }

    DisposableEffect(Unit) {
        var listener = Firebase.firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid!!)
            .addSnapshotListener { it, _ ->
                if (it != null) {
                    val result = it.toObject(UserModel::class.java)
                    if (result != null) {
                        userModel.value = result
                    }
                }
            }

        onDispose {
            listener.remove()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Your cart", fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        LazyColumn {
            items(userModel.value.cartItems.toList(), key = {it.first}) { (productId, qty) ->
                CartItemView(productId = productId, qty = qty)
            }
        }
    }
}