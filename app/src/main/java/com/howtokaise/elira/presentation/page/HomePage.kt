package com.howtokaise.elira.presentation.page

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.howtokaise.elira.model.ProductModel
import com.howtokaise.elira.presentation.components.BannerView
import com.howtokaise.elira.presentation.components.CategoriesView
import com.howtokaise.elira.presentation.components.HeaderView
import com.howtokaise.elira.presentation.components.HomeItemView

@Composable
fun HomePage(modifier: Modifier = Modifier) {

    val isDarkTheme = isSystemInDarkTheme()
    val backgroundColor = if (isDarkTheme) Color.Black else Color.White
    val productList = remember { mutableStateOf<List<ProductModel>>(emptyList()) }

    LaunchedEffect(Unit) {
        Firebase.firestore.collection("data")
            .document("stock")
            .collection("products")
            .get().addOnCompleteListener {
                if (it.isSuccessful) {
                    val resultList = it.result.documents.mapNotNull { doc ->
                        doc.toObject(ProductModel::class.java)
                    }
                    productList.value = resultList
                }
            }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(start = 16.dp, end = 16.dp, bottom = 80.dp)
    ) {
        item {
            HeaderView(modifier)
            Spacer(modifier = Modifier.height(10.dp))
            BannerView(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            )
            Text("Categories", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(5.dp))
            CategoriesView(modifier)
            Spacer(modifier = Modifier.height(10.dp))
        }
        items(productList.value.chunked(2)) { rowItems ->
            Row {
                rowItems.forEach {
                    HomeItemView(product = it, modifier = Modifier.weight(1f))
                }
                if (rowItems.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}