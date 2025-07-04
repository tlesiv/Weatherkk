package com.example.weather

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.weather.data.WeatherModel
import java.time.LocalTime


@Composable
fun MainList(list: List<WeatherModel> , currentDay: MutableState<WeatherModel>){
    LazyRow(modifier = Modifier.fillMaxSize()) {
        itemsIndexed(
            list
        ){_, item ->
            ListItem(item, currentDay )
        }
    }
}

fun isNightTime(): Boolean {
    val currentTime = LocalTime.now()
    val nightStart = LocalTime.of(20, 0) // 22:00
    val nightEnd = LocalTime.of(5, 0)   // 05:00
    return currentTime.isAfter(nightStart) || currentTime.isBefore(nightEnd)
}


@Composable
fun ListItem(item: WeatherModel,  currentDay: MutableState<WeatherModel>) {

    val ubuntuBold: FontFamily = FontFamily(
        Font(R.font.ubuntu_bold, weight = FontWeight.Bold)
    )

    val formattedTime = item.time.split(" ")[1]
    val isNight = item.is_day?.toIntOrNull() == 0

    val weatherIconMap = mapOf(//для заміни на свої іконки
        "Sunny" to  R.drawable.sun,
        //"Sunny" to if (isNight) R.drawable.moon else R.drawable.sun,
        "Clear" to  R.drawable.sun,
        //"Clear" to if(isNight)  R.drawable.moon else  R.drawable.sun,
        "Cloudy" to R.drawable.white_cloud,
        "Overcast" to R.drawable.white_cloud,
        "Partly Cloudy" to  R.drawable.partly_cloud,
       //"Partly Cloudy" to if (isNight) R.drawable.moon else R.drawable.partly_cloud,
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
        "Moderate snow" to R.drawable.snowflake
    )
    val normalizedCondition = item.condition.trim()
    val iconResource = weatherIconMap[normalizedCondition] ?: R.drawable.white_cloud//іконка за замовчуванням якщо я не правильно задам WeatherIconMap

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding((2).dp)
            .background(colorResource(id = R.color.browni))
    ) {
        Text(
            text = formattedTime,
            fontSize = 10.sp,
            color = Color.Gray,
            fontFamily = ubuntuBold,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        AsyncImage(
            model = iconResource,
            contentDescription = null,
            modifier = Modifier
                .padding(3.dp)
                .size(27.dp)
        )
        Text(
            text = item.currentTemp.ifEmpty { "${item.maxTemp}/${item.minTemp}" },
            color = Color.White,
            fontFamily = ubuntuBold,
            fontSize = 10.sp,
        )
    }
}

@Composable
fun DialogSearch(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
){
    var text by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            TextButton(onClick = { onConfirm(text) }) {
                Text(text = "OK")
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(text = "Cancel")
            }
        },
        title = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(text = "Введіть назву міста: ")
                TextField(
                    value = text,
                    onValueChange = { newText -> text = newText }
                )
            }
        }
    )
}

@Composable
fun Panel(isSearchDialogOpen: MutableState<Boolean>, onClickSearch: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize())
    {
        Image(
            painter = painterResource(id = R.drawable.panel),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp, bottomStart = 0.dp)),
            contentScale = ContentScale.Crop
        )

        IconButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp),
            onClick = { onClickSearch.invoke() }) {
            Icon(
                modifier = Modifier.size(30.dp),
                painter = painterResource(id = R.drawable.search),
                contentDescription = null,
                tint = Color.White
            )



            if (isSearchDialogOpen.value) {
                DialogSearch(
                    onDismiss = { isSearchDialogOpen.value = false },
                    onConfirm = { city ->
                        isSearchDialogOpen.value = false
                    })

            }
        }

    }

}




