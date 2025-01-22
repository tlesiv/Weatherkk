package com.example.weather

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
import com.example.weather.data.WeatherModel

@Composable
fun TodayWeather(
    currentDay: MutableState<WeatherModel>,
    daysList: MutableState<List<WeatherModel>>,
    hourlyList: MutableState<List<WeatherModel>>,
    isSearchDialogOpen: MutableState<Boolean>,
    onClickSearch: () -> Unit,
) {
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
                onClick = { onClickSearch.invoke() }
            ) {
                Icon(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(id = R.drawable.search),
                    contentDescription = null,
                    tint = Color.White
                )
            }

            if (isSearchDialogOpen.value) {
                DialogSearch(
                    onDismiss = { isSearchDialogOpen.value = false },
                    onConfirm = { city ->
                        isSearchDialogOpen.value = false
                    }
                )
            }




            Text(
                text = currentDay.value.city,
                fontSize = 20.sp,
                color = Color.White,
                fontFamily = ubuntuBold,
                modifier = Modifier.align(Alignment.Center)
            )


            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 16.dp)

            ) {
                Text(
                    text = currentDay.value.minTemp.toFloat().toInt().toString() + "°/",
                    fontSize = 20.sp,
                    color = Color.White,
                    fontFamily = ubuntuRegular,
                )

                Text(
                    text = currentDay.value.maxTemp.toFloat().toInt().toString() + "°C",
                    fontSize = 20.sp,
                    color = Color.White,
                    fontFamily = ubuntuBold,
                )
            }
        }

        Text(
            text = currentDay.value.currentTemp.toFloat().toInt().toString() + "°С",
            fontSize = 43.sp,
            color = Color.White,
            fontFamily = ubuntuBold,
            modifier = Modifier.padding(bottom = 2.dp)
        )

        Text(
            text = currentDay.value.condition,
            fontSize = 12.sp,
            color = Color.White,
            fontFamily = ubuntuBold,
            modifier = Modifier.padding(top = 3.dp)
        )

        LazyRow(
            modifier = Modifier
                .padding(end = 20.dp, start = 20.dp, top = 30.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(
                    colorResource(id = R.color.browni)
                ),
            contentPadding = PaddingValues(horizontal = 8.dp)
        ) {
            itemsIndexed(hourlyList.value) { _, item ->
                ListItem(item, currentDay)
            }
        }
    }
}


@Composable
fun ItemRow(item: WeatherModel, currentDay: MutableState<WeatherModel>) {

    val ubuntuBold: FontFamily = FontFamily(
        Font(R.font.ubuntu_bold, weight = FontWeight.Bold)
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(3.dp)
            .background(colorResource(id = R.color.browni))
    ) {
        Text(
            text = "12",
            fontSize = 10.sp,
            color = Color.Gray,
            fontFamily = ubuntuBold,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.white_cloud),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(3.dp)
                .size(27.dp)
        )
        Text(
            text = "12",
            color = Color.White,
            fontFamily = ubuntuBold,
            fontSize = 10.sp,
        )
    }
}