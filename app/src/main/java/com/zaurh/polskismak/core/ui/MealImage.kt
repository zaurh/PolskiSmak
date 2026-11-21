package com.zaurh.polskismak.core.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** Dish photos are round top-down plates; a soft shadow lifts them off the background. */
@Composable
fun PlateImage(
    @DrawableRes image: Int,
    modifier: Modifier = Modifier,
    elevation: Dp = 10.dp,
) {
    Image(
        painter = painterResource(image),
        contentDescription = null,
        contentScale = ContentScale.Fit,
        modifier = modifier.shadow(elevation, CircleShape, clip = false),
    )
}

@Composable
fun IngredientImage(
    @DrawableRes image: Int,
    modifier: Modifier = Modifier,
) {
    Image(
        painter = painterResource(image),
        contentDescription = null,
        contentScale = ContentScale.Fit,
        modifier = modifier,
    )
}
