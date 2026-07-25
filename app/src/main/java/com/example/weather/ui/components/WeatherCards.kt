package com.example.weather.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weather.R
import com.example.weather.domain.model.CurrentWeather
import com.example.weather.domain.model.DayForecast
import com.example.weather.ui.theme.ubuntuBold
import kotlin.math.roundToInt

private val snowConditions = setOf(
    "Snow", "Light snow showers", "Moderate or heavy snow showers", "Blizzard",
    "Patchy light snow", "Light snow", "Heavy snow", "Patchy heavy snow", "Moderate snow",
)

private val windDirToEnglish = mapOf(
    "N" to "North", "NE" to "Northeast", "E" to "East", "SE" to "Southeast",
    "S" to "South", "SW" to "Southwest", "W" to "West", "NW" to "Northwest",
    "NNE" to "North-Northeast", "ENE" to "East-Northeast",
    "ESE" to "East-Southeast", "SSE" to "South-Southeast",
    "SSW" to "South-Southwest", "WSW" to "West-Southwest",
    "WNW" to "West-Northwest", "NNW" to "North-Northwest",
)

@Composable
fun WeatherCards(current: CurrentWeather, today: DayForecast) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            // FEELS LIKE
            Card(
                modifier = Modifier
                    .size(191.8.dp)
                    .padding(start = 20.dp, top = 20.dp),
                shape = RoundedCornerShape(16.dp),
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
                            modifier = Modifier.padding(bottom = 8.dp),
                        ) {
                            Box(
                                modifier = Modifier.size(25.dp),
                                contentAlignment = Alignment.Center,
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.temperature),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(20.dp)
                                        .padding(end = 8.dp),
                                )
                            }
                            Text(
                                text = "FEELS LIKE",
                                fontSize = 12.sp,
                                fontFamily = ubuntuBold,
                                color = Color.Gray,
                            )
                        }

                        Text(
                            text = "${current.feelsLikeC.roundToInt()}°C",
                            fontSize = 25.sp,
                            fontFamily = ubuntuBold,
                            color = Color.White,
                            modifier = Modifier.padding(bottom = 4.dp),
                        )

                        Text(
                            text = "Actual: ${current.tempC.roundToInt()}°C",
                            fontSize = 16.sp,
                            fontFamily = ubuntuBold,
                            color = Color.LightGray,
                            modifier = Modifier.padding(bottom = 16.dp),
                        )
                    }

                    val feelsLike = current.feelsLikeC.roundToInt()
                    val actual = current.tempC.roundToInt()

                    val feelsLikeDescription = when {
                        feelsLike > actual -> "The weather feels warmer than it actually is."
                        feelsLike < actual -> "The wind makes it feel colder."
                        else -> "Matches the actual air temperature."
                    }

                    Text(
                        text = feelsLikeDescription,
                        fontSize = 12.sp,
                        color = Color.White,
                        fontFamily = ubuntuBold,
                        modifier = Modifier
                            .padding(start = 8.dp, bottom = 20.dp)
                            .align(Alignment.BottomStart),
                    )
                }
            }

            // UV INDEX
            Card(
                modifier = Modifier
                    .size(191.8.dp)
                    .padding(end = 20.dp, top = 20.dp),
                shape = RoundedCornerShape(16.dp),
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
                            modifier = Modifier.padding(bottom = 8.dp),
                        ) {
                            Box(
                                modifier = Modifier.size(25.dp),
                                contentAlignment = Alignment.Center,
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.uv_sun2),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(25.dp)
                                        .padding(end = 8.dp),
                                )
                            }
                            Text(
                                text = "UV INDEX",
                                fontSize = 12.sp,
                                fontFamily = ubuntuBold,
                                color = Color.Gray,
                            )
                        }

                        Text(
                            text = current.uv.toInt().toString(),
                            fontSize = 25.sp,
                            fontFamily = ubuntuBold,
                            color = Color.White,
                            modifier = Modifier.padding(bottom = 4.dp),
                        )

                        val uvIndex = current.uv.roundToInt()
                        val uvLevel = when {
                            uvIndex > 10 -> "Extreme"
                            uvIndex in 8..10 -> "Very high"
                            uvIndex in 6..7 -> "High"
                            uvIndex in 3..5 -> "Moderate"
                            else -> "Low"
                        }

                        Text(
                            text = uvLevel,
                            fontSize = 16.sp,
                            fontFamily = ubuntuBold,
                            color = Color.LightGray,
                            modifier = Modifier.padding(bottom = 16.dp),
                        )
                    }

                    val uvIndex = current.uv.roundToInt()
                    val uvDescription = when {
                        uvIndex > 10 -> "Be sure to protect yourself from the sun, maximum protection is a must!"
                        uvIndex in 8..10 -> "Avoid sun exposure, use maximum protection."
                        uvIndex in 6..7 -> "Avoid prolonged sun exposure."
                        uvIndex in 3..5 -> "Basic sun protection is recommended."
                        else -> "Being in the sun is currently safe."
                    }

                    Text(
                        text = uvDescription,
                        fontSize = 12.sp,
                        color = Color.White,
                        fontFamily = ubuntuBold,
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(start = 8.dp, bottom = 20.dp),
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
            shape = RoundedCornerShape(16.dp),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorResource(id = R.color.brownik))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 8.dp),
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.wind),
                            contentDescription = null,
                            modifier = Modifier
                                .size(25.dp)
                                .padding(end = 8.dp),
                        )
                        Text(
                            text = "WIND",
                            fontSize = 12.sp,
                            fontFamily = ubuntuBold,
                            color = Color.Gray,
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(text = "Wind", fontSize = 16.sp, fontFamily = ubuntuBold, color = Color.White)
                        Text(
                            text = "${(current.windKph / 3.6).roundToInt()} m/s",
                            fontSize = 16.sp,
                            fontFamily = ubuntuBold,
                            color = Color.White,
                        )
                    }

                    HorizontalDivider(
                        color = Color.LightGray,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height((0.2).dp),
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp, bottom = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(text = "Gusts", fontSize = 16.sp, fontFamily = ubuntuBold, color = Color.White)
                        Text(
                            text = "${(current.gustKph / 3.6).roundToInt()} m/s",
                            fontSize = 16.sp,
                            fontFamily = ubuntuBold,
                            color = Color.White,
                        )
                    }

                    HorizontalDivider(
                        color = Color.LightGray,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height((0.2).dp),
                    )

                    val windDirection = windDirToEnglish[current.windDir.trim()] ?: "Unknown"

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(text = "Direction", fontSize = 16.sp, fontFamily = ubuntuBold, color = Color.White)
                        Text(
                            text = windDirection,
                            fontSize = 16.sp,
                            fontFamily = ubuntuBold,
                            color = Color.White,
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            val isSnow = snowConditions.contains(today.condition)
            val snowOrPrecip = if (isSnow) {
                "${today.totalSnowCm.roundToInt()} cm"
            } else {
                "${today.totalPrecipMm.roundToInt()} mm"
            }

            val snow = today.totalSnowCm.roundToInt()
            val precip = today.totalPrecipMm.roundToInt()

            val precipDescription = when {
                snow > 0 && precip > 0 -> "Snow mixed with rain today."
                snow > 0 -> "Snow today."
                precip > 0 -> "Rain today."
                else -> "No precipitation today."
            }

            // PRECIPITATION
            Card(
                modifier = Modifier
                    .size(191.8.dp)
                    .padding(start = 20.dp, top = 20.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(colorResource(id = R.color.brownik))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 8.dp),
                        ) {
                            Box(
                                modifier = Modifier.size(25.dp),
                                contentAlignment = Alignment.Center,
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.water_drop),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(18.dp)
                                        .padding(end = 8.dp),
                                )
                            }
                            Text(
                                text = "PRECIPITATION",
                                fontSize = 12.sp,
                                fontFamily = ubuntuBold,
                                color = Color.Gray,
                            )
                        }

                        Text(
                            text = snowOrPrecip,
                            fontSize = 25.sp,
                            fontFamily = ubuntuBold,
                            color = Color.White,
                            modifier = Modifier.padding(bottom = 4.dp),
                        )
                    }

                    Text(
                        text = precipDescription,
                        fontSize = 12.sp,
                        color = Color.White,
                        fontFamily = ubuntuBold,
                        modifier = Modifier
                            .padding(start = 8.dp, bottom = 20.dp)
                            .align(Alignment.BottomStart),
                    )
                }
            }

            // VISIBILITY
            Card(
                modifier = Modifier
                    .size(191.8.dp)
                    .padding(end = 20.dp, top = 20.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(colorResource(id = R.color.brownik))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 8.dp),
                        ) {
                            Box(
                                modifier = Modifier.size(25.dp),
                                contentAlignment = Alignment.Center,
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.visibility),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(28.dp)
                                        .padding(end = 8.dp),
                                )
                            }
                            Text(
                                text = "VISIBILITY",
                                fontSize = 12.sp,
                                fontFamily = ubuntuBold,
                                color = Color.Gray,
                            )
                        }

                        Text(
                            text = "${current.visKm.roundToInt()} km",
                            fontSize = 25.sp,
                            fontFamily = ubuntuBold,
                            color = Color.White,
                            modifier = Modifier.padding(bottom = 4.dp),
                        )
                    }

                    val visibility = current.visKm.roundToInt()
                    val visibilityDescription = when {
                        visibility > 15 -> "Absolutely clear."
                        visibility in 10..15 -> "Clear."
                        visibility in 5..9 -> "Light haze is currently reducing visibility."
                        else -> "Visibility is very low."
                    }

                    Text(
                        text = visibilityDescription,
                        fontSize = 12.sp,
                        color = Color.White,
                        fontFamily = ubuntuBold,
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(start = 8.dp, bottom = 20.dp),
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            // PRESSURE
            Card(
                modifier = Modifier
                    .size(191.8.dp)
                    .padding(start = 20.dp, top = 20.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(colorResource(id = R.color.brownik))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 8.dp),
                        ) {
                            Box(
                                modifier = Modifier.size(25.dp),
                                contentAlignment = Alignment.Center,
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.pressure),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(22.dp)
                                        .padding(end = 8.dp),
                                )
                            }
                            Text(
                                text = "PRESSURE",
                                fontSize = 12.sp,
                                fontFamily = ubuntuBold,
                                color = Color.Gray,
                            )
                        }

                        Text(
                            text = "${current.pressureMb.roundToInt()} hPa",
                            fontSize = 25.sp,
                            fontFamily = ubuntuBold,
                            color = Color.White,
                            modifier = Modifier.padding(bottom = 4.dp),
                        )
                    }
                }
            }

            // HUMIDITY
            Card(
                modifier = Modifier
                    .size(191.8.dp)
                    .padding(end = 20.dp, top = 20.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(colorResource(id = R.color.brownik))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 8.dp),
                        ) {
                            Box(
                                modifier = Modifier.size(25.dp),
                                contentAlignment = Alignment.Center,
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.humidity),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(25.dp)
                                        .padding(end = 8.dp),
                                )
                            }
                            Text(
                                text = "HUMIDITY",
                                fontSize = 12.sp,
                                fontFamily = ubuntuBold,
                                color = Color.Gray,
                            )
                        }

                        Text(
                            text = "${current.humidity}%",
                            fontSize = 25.sp,
                            fontFamily = ubuntuBold,
                            color = Color.White,
                            modifier = Modifier.padding(bottom = 4.dp),
                        )
                    }

                    Text(
                        text = "Dew point: ${current.dewpointC.roundToInt()}°C",
                        fontSize = 12.sp,
                        color = Color.White,
                        fontFamily = ubuntuBold,
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(start = 8.dp, bottom = 20.dp),
                    )
                }
            }
        }
    }
}