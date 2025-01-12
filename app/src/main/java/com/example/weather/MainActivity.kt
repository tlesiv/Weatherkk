package com.example.weather

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.weather.data.WeatherModel
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale


const val API_KEY = "e162f19b5aa040cb80e181753241010"


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val daysList = remember {
                mutableStateOf(listOf<WeatherModel>())
            }
            val currentDay = remember {
                mutableStateOf(
                    WeatherModel(
                        "",
                        "",
                        "0.0",
                        "",
                        "",
                        "0.0",
                        "0.0",
                        "",
                        "",
                        "",
                        "",
                        "",
                        ""
                    )
                )
            }
            val hourlyList = remember { mutableStateOf(listOf<WeatherModel>()) }


            getData("Zolochiv", this, daysList, currentDay, hourlyList)
            WeatherApp(currentDay, daysList, hourlyList, this)
        }
    }


    @Composable
    fun WeatherApp(
        currentDay: MutableState<WeatherModel>,
        daysList: MutableState<List<WeatherModel>>,
        hourlyList: MutableState<List<WeatherModel>>,
        context: Context,
    ) {
        val isSearchDialogOpen = remember { mutableStateOf(false) }

        // Фон
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher2k_background),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()) // Додано вертикальну прокрутку
        ) {
            TodayWeather(
                currentDay,
                daysList,
                hourlyList,
                isSearchDialogOpen = isSearchDialogOpen,
                onClickSearch = { isSearchDialogOpen.value = true }
            )
            WeekWeather(daysList)
            WeatherCards(currentDay.value)

        }

        // Діалог пошуку
        if (isSearchDialogOpen.value) {
            DialogSearch(
                onDismiss = { isSearchDialogOpen.value = false },
                onConfirm = { city ->
                    isSearchDialogOpen.value = false
                    getData(city, context, daysList, currentDay, hourlyList)
                }
            )
        }
    }
}

    val ubuntuLight: FontFamily = FontFamily(
    Font(R.font.ubuntu_light, weight = FontWeight.Light)
)
val ubuntuRegular: FontFamily = FontFamily(
    Font(R.font.ubuntu_regular, weight = FontWeight.Light)
)
val ubuntuBold: FontFamily = FontFamily(
    Font(R.font.ubuntu_bold, weight = FontWeight.Bold)
)


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
            // Іконка пошуку в верхньому лівому куті
            IconButton(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 16.dp), // Відступи від краю
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


@Composable
fun WeatherCards(item: WeatherModel) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // FEELING TEMP
                Card(
                    modifier = Modifier
                        .size(191.8.dp)
                        .padding(start = 20.dp, top = 20.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(colorResource(id = R.color.browni))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp),
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(bottom = 8.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.temperature),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(20.dp)
                                        .padding(end = 8.dp)
                                )

                                Text(
                                    text = "ВІДЧУТТЯ ЯК",
                                    fontSize = 12.sp,
                                    fontFamily = ubuntuBold,
                                    color = Color.Gray
                                )
                            }

                            Text(
                                text = item.formattedFeelingTemp() + "°С",
                                fontSize = 25.sp,
                                fontFamily = ubuntuBold,
                                color = Color.White,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )

                            Text(
                                text = "Фактична: ${item.currentTemp.toFloat().toInt()}°С",
                                fontSize = 16.sp,
                                fontFamily = ubuntuBold,
                                color = Color.LightGray,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                        }

                        val feelsLikeRounded = item.feelslike_c?.toFloatOrNull()?.toInt()
                        val currentTempRounded = item.currentTemp?.toFloatOrNull()?.toInt()

                        val description_of_filling_temp = when {
                            feelsLikeRounded == null || currentTempRounded == null ->
                                "Дані про температуру наразі недоступні."

                            feelsLikeRounded > currentTempRounded ->
                                "Погода видається теплішою ніж насправді."

                            feelsLikeRounded < currentTempRounded ->
                                "Через вітер погода видається холоднішою."

                            else ->
                                "Збігається зі справжньою температурою."
                        }

                        Text(
                            text = description_of_filling_temp,
                            fontSize = 12.sp,
                            color = Color.White,
                            fontFamily = ubuntuBold,
                            modifier = Modifier
                                .padding(start = 8.dp, bottom = 20.dp)
                                .align(Alignment.BottomStart)
                        )
                    }
                }

                // VISIBILITY
                Card(
                    modifier = Modifier
                        .size(191.8.dp)
                        .padding(end = 20.dp, top = 20.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(colorResource(id = R.color.browni))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp),
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(bottom = 8.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.visibility__2_),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(25.dp)
                                        .padding(end = 8.dp)
                                )

                                Text(
                                    text = "ВИДИМІСТЬ",
                                    fontSize = 12.sp,
                                    fontFamily = ubuntuBold,
                                    color = Color.Gray
                                )
                            }

                            Text(
                                text = item.formattedVisibilityKm() + " км",
                                fontSize = 25.sp,
                                fontFamily = ubuntuBold,
                                color = Color.White,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                        }

                        val vis = item.vis_km?.toFloatOrNull()?.toInt()

                        val description_of_visibility = when {
                            vis == null -> "Дані про видимість недоступні."
                            vis > 15 -> "Абсолютно ясно."
                            vis in 10..15 -> "Ясно."
                            vis in 5..9 -> "Легка імла зараз зменшує видимість."
                            else -> "Видимість дуже низька."
                        }

                        Text(
                            text = description_of_visibility,
                            fontSize = 12.sp,
                            color = Color.White,
                            fontFamily = ubuntuBold,
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(start = 8.dp, bottom = 20.dp)
                        )
                    }
                }
            }

            // WIND
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(191.8.dp)
                    .padding(end = 20.dp, start = 20.dp, top = 20.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(colorResource(id = R.color.brownik))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),

                        ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 8.dp)
                        ) {


                            Image(
                                painter = painterResource(id = R.drawable.wind_test),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(25.dp)
                                    .padding(end = 8.dp)
                            )
                            Text(
                                text = "ВІТЕР",
                                fontSize = 12.sp,
                                fontFamily = ubuntuBold,
                                color = Color.Gray
                            )
                        }


                        Row(
                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Вітер",
                                fontSize = 16.sp,
                                fontFamily = ubuntuBold,
                                color = Color.White
                            )
                            Text(
                                text = item.formattedWindKMToMc + " м/с",
                                fontSize = 16.sp,
                                fontFamily = ubuntuBold,
                                color = Color.White
                            )

                        }

                        HorizontalDivider(
                            color = Color.LightGray,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height((0.2).dp)

                        )

                        Row(
                            modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Пориви",
                                fontSize = 16.sp,
                                fontFamily = ubuntuBold,
                                color = Color.White
                            )
                            Text(
                                text = item.formattedGustKMToMc + " м/с",
                                fontSize = 16.sp,
                                fontFamily = ubuntuBold,
                                color = Color.White
                            )

                        }

                        HorizontalDivider(
                            color = Color.LightGray,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height((0.2).dp)

                        )

                        val WindDirToUA = mapOf(
                            "N" to "Північ",
                            "NE" to "Північ-Схід",
                            "E" to "Схід",
                            "SE" to "Південь-Схід",
                            "S" to "Південь",
                            "SW" to "Південь-Захід",
                            "W" to "Захід",
                            "NW" to "Північ-Захід",
                            "NNE" to "Північ-Північ-Схід",
                            "ENE" to "Схід-Північ-Схід",
                            "ESE" to "Схід-Південь-Схід",
                            "SSE" to "Південь-Південь-Схід",
                            "SSW" to "Південь-Південь-Захід",
                            "WSW" to "Захід-Південь-Захід",
                            "WNW" to "Захід-Північ-Захід",
                            "NNW" to "Північ-Північ-Захід"

                            // "N" to "Пн",
                            // "NE" to "ПнСх",
                            // "E" to "Сх",
                            //"SE" to "ПдСх",
                            //"S" to "Пд",
                            // "SW" to "ПдЗх",
                            // "W" to "Зх",
                            //  "NW" to "ПнЗх",
                            // "NNE" to "ПнПнСх",
                            // "ENE" to "СхПнСх",
                            //  "ESE" to "СхПдСх",
                            //  "SSE" to "ПдПдСх",
                            //  "SSW" to "ПдПдЗх",
                            // "WSW" to "ЗхПдЗх",
                            // "WNW" to "ЗхПнЗх",
                            //  "NNW" to "ПнПнЗх",

                        )
                        val normalizedWindDir = item.wind_dir.trim()
                        val defaultWindDir = WindDirToUA[normalizedWindDir] ?: "Невідомо"

                        Row(
                            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Напрямок",
                                fontSize = 16.sp,
                                fontFamily = ubuntuBold,
                                color = Color.White
                            )
                            Text(
                                text = defaultWindDir,
                                fontSize = 16.sp,
                                fontFamily = ubuntuBold,
                                color = Color.White
                            )
                        }
                    }
                }
            }

        }
    }
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


private fun getData(
    city: String, context: Context,
    daysList: MutableState<List<WeatherModel>>,
    currentDay: MutableState<WeatherModel>,
    hourlyList: MutableState<List<WeatherModel>>,
) {
    val url = "https://api.weatherapi.com/v1/forecast.json?key=$API_KEY" +
            "&q=$city" +
            "&days=" +
            "7" +
            "&aqi=no&alerts=no"
    val queue = Volley.newRequestQueue(context)
    val sRequest = StringRequest(
        Request.Method.GET,
        url,
        { response ->
            Log.d("MyLog", "Response: $response")
            val list = getWeatherByDays(response)
            currentDay.value = list[0]
            daysList.value = list

            val hourlyData = getWeatherByHours(list[0].hours)
            hourlyList.value = hourlyData
        },
        {
            Log.d("MyLog", "VolleyError: $it")
        }
    )
    queue.add(sRequest)
}

private fun getWeatherByDays(response: String): List<WeatherModel> {
    if (response.isEmpty()) return listOf()
    val list = ArrayList<WeatherModel>()
    val mainObject = JSONObject(response)
    val city = mainObject.getJSONObject("location").getString("name")
    val days = mainObject.getJSONObject("forecast").getJSONArray("forecastday")

    for (i in 0 until days.length()) {
        val item = days[i] as JSONObject
        list.add(
            WeatherModel(
                city,
                item.getString("date"),
                "",
                item.getJSONObject("day").getJSONObject("condition")
                    .getString("text"),
                item.getJSONObject("day").getJSONObject("condition")
                    .getString("icon"),
                item.getJSONObject("day").getString("maxtemp_c"),
                item.getJSONObject("day").getString("mintemp_c"),
                item.getJSONArray("hour").toString(),
                "",
                "",
                "",
                "",
                ""

            )
        )
    }
    list[0] = list[0].copy(
        time = mainObject.getJSONObject("current").getString("last_updated"),
        currentTemp = mainObject.getJSONObject("current").getString("temp_c"),
        feelslike_c = mainObject.getJSONObject("current").getString("feelslike_c"),
        vis_km = mainObject.getJSONObject("current").getString("vis_km"),
        wind_kph = mainObject.getJSONObject("current").getString("wind_kph"),
        gust_kph = mainObject.getJSONObject("current").getString("gust_kph"),
        wind_dir = mainObject.getJSONObject("current").getString("wind_dir"),
    )
    return list
}


private fun getWeatherByHours(hours: String): List<WeatherModel> {
    if (hours.isEmpty()) return listOf()
    val hoursArray = JSONArray(hours)
    val list = ArrayList<WeatherModel>()
    for (i in 0 until hoursArray.length()) {
        val item = hoursArray[i] as JSONObject
        list.add(
            WeatherModel(
                "",
                item.getString("time"),
                item.getString("temp_c").toFloat().toInt().toString() + "ºC",
                item.getJSONObject("condition").getString("text"),
                item.getJSONObject("condition").getString("icon"),
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                ""
            )
        )
    }
    return list
}





