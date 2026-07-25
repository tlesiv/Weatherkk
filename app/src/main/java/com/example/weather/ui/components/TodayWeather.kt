package com.example.weather.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.weather.R
import com.example.weather.domain.model.HourForecast
import com.example.weather.domain.model.WeatherForecast
import com.example.weather.ui.theme.ubuntuBold
import com.example.weather.ui.theme.ubuntuRegular
import kotlin.math.roundToInt

@Composable
fun TodayWeather(
    forecast: WeatherForecast,
    onClickSearch: () -> Unit,
) {
    val current = forecast.current
    val today = forecast.days.first()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(top = 45.dp)
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp, top = 10.dp)
        ) {
            IconButton(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 16.dp),
                onClick = onClickSearch,
            ) {
                Icon(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(id = R.drawable.search),
                    contentDescription = null,
                    tint = Color.White,
                )
            }

            Text(
                text = forecast.city,
                fontSize = 20.sp,
                color = Color.White,
                fontFamily = ubuntuBold,
                modifier = Modifier.align(Alignment.Center),
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 16.dp),
            ) {
                Text(
                    text = "${today.minTempC.roundToInt()}°/",
                    fontSize = 20.sp,
                    color = Color.White,
                    fontFamily = ubuntuRegular,
                )
                Text(
                    text = "${today.maxTempC.roundToInt()}°C",
                    fontSize = 20.sp,
                    color = Color.White,
                    fontFamily = ubuntuBold,
                )
            }
        }

        Text(
            text = "${current.tempC.roundToInt()}°C",
            fontSize = 43.sp,
            color = Color.White,
            fontFamily = ubuntuBold,
            modifier = Modifier.padding(bottom = 2.dp),
        )

        Text(
            text = current.condition,
            fontSize = 12.sp,
            color = Color.White,
            fontFamily = ubuntuBold,
            modifier = Modifier.padding(top = 3.dp),
        )

        LazyRow(
            modifier = Modifier
                .padding(end = 20.dp, start = 20.dp, top = 30.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(colorResource(id = R.color.browni)),
            contentPadding = PaddingValues(horizontal = 8.dp),
        ) {
            items(today.hours) { hour ->
                HourlyItem(hour)
            }
        }
    }
}

@Composable
fun HourlyItem(hour: HourForecast) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(2.dp)
            .background(colorResource(id = R.color.browni)),
    ) {
        Text(
            text = hour.time.substringAfter(" "), // "2026-07-19 14:00" -> "14:00"
            fontSize = 10.sp,
            color = Color.Gray,
            fontFamily = ubuntuBold,
            modifier = Modifier.padding(bottom = 4.dp),
        )
        AsyncImage(
            model = weatherIconFor(hour.condition),
            contentDescription = null,
            modifier = Modifier
                .padding(3.dp)
                .size(27.dp),
        )
        Text(
            text = "${hour.tempC.roundToInt()}°C",
            color = Color.White,
            fontFamily = ubuntuBold,
            fontSize = 10.sp,
        )
    }
}