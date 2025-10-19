package com.howtokaise.elira.presentation.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CartItemView(modifier: Modifier = Modifier, productId : String, qty : Long) {

    Text(text = productId+" ->>>> "+qty)

}