package com.howtokaise.elira.presentation.page

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.howtokaise.elira.model.OrderModel
import com.howtokaise.elira.presentation.components.OrderView

@Composable
fun OrdersPage(modifier: Modifier = Modifier) {
    val orderList = remember { mutableStateOf<List<OrderModel>>(emptyList()) }

    LaunchedEffect(Unit) {
        Firebase.firestore.collection("orders")
            .whereEqualTo("userId", FirebaseAuth.getInstance().currentUser?.uid!!)
            .get().addOnCompleteListener {
                if (it.isSuccessful) {
                    val resultList = it.result.documents.mapNotNull { doc ->
                        doc.toObject(OrderModel::class.java)
                    }
                    orderList.value = resultList
                }
            }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        Text(
            text = "Your orders",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(orderList.value) {
                OrderView(it)
            }
        }
    }
}