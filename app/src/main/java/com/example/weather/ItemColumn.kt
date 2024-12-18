package com.example.weather

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp


@Composable
fun ItemColumn(item: ItemModel) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(3.dp)
            .background(colorResource(id = R.color.grayer))
    ) {
        Image(
            painter = painterResource(id = item.imageId),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(3.dp)
                .size(60.dp)
                .clip(CircleShape)
        )
        Text(text = item.title,
            color = Color.White)
    }
}