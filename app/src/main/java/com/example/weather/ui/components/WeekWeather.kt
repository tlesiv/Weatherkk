package com.example.weather.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.weather.R
import com.example.weather.domain.model.DayForecast
import com.example.weather.ui.theme.ubuntuBold
import com.example.weather.ui.theme.ubuntuRegular
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun WeekWeather(days: List<DayForecast>) {
    Column(
        modifier = Modifier
            .padding(top = 20.dp, end = 20.dp, start = 20.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(colorResource(id = R.color.browni))
            .padding(10.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.calendar),
                contentDescription = null,
                modifier = Modifier
                    .size(20.dp)
                    .padding(end = 8.dp),
            )
            Text(
                text = "3-DAY FORECAST",
                fontSize = 12.sp,
                fontFamily = ubuntuBold,
                color = Color.Gray,
            )
        }

        HorizontalDivider(
            color = Color.LightGray,
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp),
        )

        days.forEachIndexed { index, day ->
            WeekWeatherRow(day)

            if (index != days.size - 1) {
                HorizontalDivider(
                    color = Color.LightGray,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp),
                )
            }
        }
    }
}

@Composable
fun WeekWeatherRow(day: DayForecast) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth(),
    ) {
        Text(
            text = formatDayName(day.date),
            fontSize = 15.sp,
            fontFamily = ubuntuBold,
            modifier = Modifier.weight(0.4f),
            color = Color.White,
        )

        AsyncImage(
            model = weatherIconFor(day.condition),
            contentDescription = null,
            modifier = Modifier
                .weight(0.5f)
                .size(30.dp)
                .offset(y = 2.dp),
        )

        Text(
            text = "${day.minTempC.roundToInt()}°/",
            fontSize = 15.sp,
            fontFamily = ubuntuRegular,
            modifier = Modifier
                .padding(start = 8.dp)
                .width(35.dp),
            color = Color.White,
            textAlign = TextAlign.Center,
        )

        Text(
            text = "${day.maxTempC.roundToInt()}°C",
            fontSize = 15.sp,
            fontFamily = ubuntuBold,
            modifier = Modifier.width(35.dp),
            color = Color.White,
            textAlign = TextAlign.Center,
        )
    }
}

fun formatDayName(dateString: String): String {
    val date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    return date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH)
        .replaceFirstChar { it.uppercaseChar() }
}