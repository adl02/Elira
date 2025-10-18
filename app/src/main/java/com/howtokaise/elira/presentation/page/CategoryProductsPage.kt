package com.howtokaise.elira.presentation.page

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController

@Composable
fun CategoryProductsPage(modifier: Modifier = Modifier, categoryId : String) {
    Text(text = "Category products page " +categoryId)

}