package com.example.weather

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
import androidx.compose.runtime.MutableState
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
import com.example.weather.data.WeatherModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun WeekWeather(daysList: MutableState<List<WeatherModel>>) {
    Column(
        modifier = Modifier
            .padding(top = 20.dp, end = 20.dp, start = 20.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(colorResource(id = R.color.browni))
            .padding(10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        )
        {
            Image(
                painter = painterResource(id = R.drawable.calendar),
                contentDescription = null,
                modifier = Modifier
                    .size(20.dp)
                    .padding(end = 8.dp)
            )
            Text(
                text = "3-ДЕННИЙ ПРОГНОЗ",
                fontSize = 12.sp,
                fontFamily = ubuntuBold,
                color = Color.Gray
            )
        }
        HorizontalDivider(
            color = Color.LightGray,
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)

        )


        daysList.value.forEachIndexed { index, item ->
            WeekWeatherRow(item)

            if (index != daysList.value.size - 1) { // перевірка на останній елемент
                HorizontalDivider( // лінія для розмежування елементів
                    color = Color.LightGray,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                )
            }
        }
    }
}

@Composable
fun WeekWeatherRow(item: WeatherModel) {

    val weatherIconMap = mapOf(
        //для заміни на свої іконки
        "Sunny" to R.drawable.sun,
        "Clear" to R.drawable.sun,//додумати може
        "Cloudy" to R.drawable.white_cloud,
        "Overcast" to R.drawable.white_cloud,
        "Partly Cloudy" to R.drawable.partly_cloud,
        "Rainy" to R.drawable.white_cloud_rain_br,
        "Light drizzle" to R.drawable.white_cloud_rain_br,
        "Patchy rain nearby" to R.drawable.white_cloud_rain_br,
        "Light freezing rain" to R.drawable.white_cloud_rain_br,
        "Thunder" to R.drawable.white_cloud_thunder,
        "Snow" to R.drawable.snowflake,
        "Light snow showers" to R.drawable.snowflake,
        "Moderate or heavy snow showers" to R.drawable.snowflake,
        "Blizzard" to R.drawable.snowflake,
        "Patchy light snow" to R.drawable.snowflake,
        "Light snow" to R.drawable.snowflake,
        "Heavy snow" to R.drawable.snowflake,
        "Patchy heavy snow" to R.drawable.snowflake,
        "Moderate snow" to R.drawable.snowflake,
    )
    val normalizedCondition = item.condition.trim()//видалення пробілів в condition
    val iconResource = weatherIconMap[normalizedCondition] ?: R.drawable.white_cloud

    val formattedTimeWeek = item.time.split(" ")[0]

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()

    ) {
        Text(
            text = formatFromDateToDay(formattedTimeWeek),
            fontSize = 15.sp,
            fontFamily = ubuntuBold,
            modifier = Modifier.weight(0.4f),
            color = Color.White,
        )

        AsyncImage(
            model = iconResource,
            contentDescription = null,
            modifier = Modifier
                .weight(0.5f)
                .size(30.dp)
                .offset(y = 2.dp)


        )


        Text(
            text = item.minTemp.toFloat().toInt().toString() + "°/",
            fontSize = 15.sp,
            fontFamily = ubuntuRegular,
            modifier = Modifier
                .padding(start = 8.dp)
                .width(35.dp),
            color = Color.White,
            textAlign = TextAlign.Center
        )



        Text(
            text = item.maxTemp.toFloat().toInt().toString() + "°С",
            fontSize = 15.sp,
            fontFamily = ubuntuBold,
            modifier = Modifier.width(35.dp),//доробити довжину температур
            color = Color.White,
            textAlign = TextAlign.Center
        )
    }
}

fun formatFromDateToDay(dateString: String): String {
    // Парсимо рядок дати у форматі LocalDate
    val date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    // Отримуємо назву дня тижня та повертаємо у бажаному форматі
    return date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH)
}