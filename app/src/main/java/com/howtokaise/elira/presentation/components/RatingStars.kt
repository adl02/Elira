package com.howtokaise.elira.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun RatingStars(
    rating: Float,
    maxStars: Int = 5,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        val fullStars = rating.toInt()
        val hasHalfStar = rating - fullStars >= 0.5

        repeat(fullStars) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "Star",
                tint = Color(0xFF349137),
                modifier = Modifier.size(16.dp)
            )
        }

        if (hasHalfStar) {
            Icon(
                imageVector = Icons.Default.StarHalf,
                contentDescription = "Half Star",
                tint = Color(0xFF349137),
                modifier = Modifier.size(16.dp)
            )
        }

        repeat(maxStars - fullStars - if (hasHalfStar) 1 else 0) {
            Icon(
                imageVector = Icons.Default.StarOutline,
                contentDescription = "Empty Star",
                tint = Color(0xFFFFC107),
                modifier = Modifier.size(16.dp)
            )
        }
    }
}