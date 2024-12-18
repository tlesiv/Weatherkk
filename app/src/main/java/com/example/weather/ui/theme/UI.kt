package com.example.weather.ui.theme

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.weather.R


@Composable
fun ListItem() {

    val ubuntuBold: FontFamily = FontFamily(
        Font(R.font.ubuntu_bold, weight = FontWeight.Bold)
    )

    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(end = 3.dp)) {

    }
}