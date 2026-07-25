package com.example.weather.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.weather.R
import com.example.weather.ui.components.TodayWeather
import com.example.weather.ui.components.DialogSearch
import com.example.weather.ui.components.WeatherCards
import com.example.weather.ui.components.WeekWeather
import com.example.weather.ui.theme.ubuntuBold

@Composable
fun WeatherApp(
    uiState: WeatherUiState,
    onSearch: (String) -> Unit,
) {
    var isSearchDialogOpen by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher2k_background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )
    }

    val forecast = uiState.forecast

    if (forecast != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            TodayWeather(
                forecast = forecast,
                onClickSearch = { isSearchDialogOpen = true },
            )
            WeekWeather(days = forecast.days)
            WeatherCards(
                current = forecast.current,
                today = forecast.days.first(),
            )
        }
    } else {
        // No data yet: loading or error. Search stays available.
        Box(modifier = Modifier.fillMaxSize()) {
            IconButton(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 16.dp, top = 55.dp),
                onClick = { isSearchDialogOpen = true },
            ) {
                Icon(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(id = R.drawable.search),
                    contentDescription = null,
                    tint = Color.White,
                )
            }

            Text(
                text = when {
                    uiState.isLoading -> "Loading…"
                    uiState.errorMessage != null -> "Error: ${uiState.errorMessage}"
                    else -> ""
                },
                color = Color.White,
                fontFamily = ubuntuBold,
                modifier = Modifier.align(Alignment.Center),
            )
        }
    }

    if (isSearchDialogOpen) {
        DialogSearch(
            onDismiss = { isSearchDialogOpen = false },
            onConfirm = { city ->
                isSearchDialogOpen = false
                onSearch(city)
            },
        )
    }
}