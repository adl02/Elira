package com.howtokaise.elira.presentation.page

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.howtokaise.elira.model.ProductModel
import com.howtokaise.elira.model.UserModel
import com.howtokaise.elira.presentation.components.WishlistItemView
import com.howtokaise.elira.presentation.navigation.GlobalNavigation

@Composable
fun WishlistPage(modifier: Modifier = Modifier) {

    val isDarkTheme = isSystemInDarkTheme()
    val backgroundColor = if (isDarkTheme) Color.Black else Color.White

    var productList by remember { mutableStateOf<List<ProductModel>>(emptyList()) }
    val firestore = FirebaseFirestore.getInstance()
    val uid = FirebaseAuth.getInstance().currentUser?.uid

    DisposableEffect(Unit) {
        if (uid == null) {
            productList = emptyList()
            // Nothing more to do, just provide a DisposableEffectResult
            onDispose { }  // ✅ this satisfies the return type
        } else {
            // Normal logic for listening to wishlist
            var userListener: ListenerRegistration? = null
            var productListeners: MutableList<ListenerRegistration> = mutableListOf()

            userListener = firestore.collection("users").document(uid)
                .addSnapshotListener { userSnapshot, _ ->
                    val user = userSnapshot?.toObject(UserModel::class.java)
                    val wishlistIds = user?.wishlist ?: emptyList()

                    productListeners.forEach { it.remove() }
                    productListeners.clear()
                    productList = emptyList()

                    wishlistIds.forEach { productId ->
                        val listener = firestore.collection("data")
                            .document("stock")
                            .collection("products")
                            .document(productId)
                            .addSnapshotListener { productSnapshot, _ ->
                                if (productSnapshot != null && productSnapshot.exists()) {
                                    val product = productSnapshot.toObject(ProductModel::class.java)
                                    product?.let {
                                        productList = productList.filter { it.id != product.id } + product
                                    }
                                } else {
                                    productList = productList.filter { it.id != productId }
                                }
                            }
                        productListeners.add(listener)
                    }
                }

            onDispose {
                userListener?.remove()
                productListeners.forEach { it.remove() }
            }
        }
    }


    Column(modifier = Modifier.background(backgroundColor)) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier
                    .size(30.dp)
                    .clickable { GlobalNavigation.navController.popBackStack() }
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Your Wishlist",
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium,
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
        ) {
            items(productList.chunked(2)) { rowItems ->
                Row {
                    rowItems.forEach { p ->
                        WishlistItemView(product = p, modifier = Modifier.weight(1f))
                    }
                    if (rowItems.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}