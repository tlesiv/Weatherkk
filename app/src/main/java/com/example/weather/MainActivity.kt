package com.example.weather

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.weather.data.WeatherModel
import org.json.JSONArray
import org.json.JSONObject


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
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "0.0"
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
                .verticalScroll(rememberScrollState())
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
                "",
                "",
                "",
                item.getJSONObject("day").getString("totalsnow_cm"),
                item.getJSONObject("day").getString("totalprecip_mm"),
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
        pressure_mb = mainObject.getJSONObject("current").getString("pressure_mb"),
        is_day = mainObject.getJSONObject("current").getString("is_day"),
        uv = mainObject.getJSONObject("current").getString("uv"),
        humidity = mainObject.getJSONObject("current").getString("humidity"),
        dewpoint_c = mainObject.getJSONObject("current").getString("dewpoint_c"),
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





