package com.howtokaise.elira.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.howtokaise.elira.AppUtil
import com.howtokaise.elira.model.ProductModel
import com.howtokaise.elira.presentation.navigation.GlobalNavigation

@Composable
fun ProductItemView(modifier: Modifier = Modifier, product: ProductModel) {

    var context = LocalContext.current

    val isDarkTheme = isSystemInDarkTheme()
    val backgroundColor = if (isDarkTheme) Color.Black else Color.White

    Card(
        modifier = modifier
            .padding(5.dp)
            .clickable {
                GlobalNavigation.navController.navigate("product-details/" + product.id)
            },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.elevatedCardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        border = BorderStroke(1.dp, Color.Gray)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            AsyncImage(
                model = product.images.firstOrNull(),
                contentDescription = null,
                modifier = Modifier
                    .height(130.dp)
                    .fillMaxWidth()
            )

            Text(
                text = product.title,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 8.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
            ) {

                Icon(
                    Icons.Default.ArrowDownward, contentDescription = null,
                    tint = Color(0xFF349137)
                )
                Text(
                    text = "40%",
                    fontSize = 16.sp,
                    color = Color(0xFF349137)
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = "₹" + product.price,
                    fontSize = 14.sp,
                    textDecoration = TextDecoration.LineThrough
                )
                Spacer(modifier = Modifier.width(2.dp))

                Text(
                    text = "₹" + product.actualPrice,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(modifier = Modifier.height(4.dp))

            Row {
                RatingStars(4.5f)
                Spacer(modifier = Modifier.width(2.dp))
                Text("✔ Assured", fontSize = 12.sp, color = Color(0xFF2874F0))
            }

            Button(
                onClick = { AppUtil.addToCart(context, product.id) },
                modifier = Modifier

                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = if (isDarkTheme) Color.Black else Color.White),
                border = BorderStroke(1.dp, Color.Gray),
                shape = RectangleShape
            ) {
                Text("Add to Cart", color = Color(0xFF2874F0))
            }
        }
    }
}