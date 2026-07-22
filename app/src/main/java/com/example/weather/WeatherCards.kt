package com.example.weather

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
import com.example.weather.data.WeatherModel

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
                                Box(
                                    modifier = Modifier
                                        .size(25.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.temperature),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(20.dp)
                                            .padding(end = 8.dp)
                                    )
                                }

                                Text(
                                    text = "FEELS LIKE",
                                    fontSize = 12.sp,
                                    fontFamily = ubuntuBold,
                                    color = Color.Gray
                                )
                            }

                            Text(
                                text = item.formattedFeelingTemp() + "°C",
                                fontSize = 25.sp,
                                fontFamily = ubuntuBold,
                                color = Color.White,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )

                            Text(
                                text = "Actual: ${item.currentTemp.toFloat().toInt()}°C",
                                fontSize = 16.sp,
                                fontFamily = ubuntuBold,
                                color = Color.LightGray,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                        }

                        val feelsLikeRounded = item.feelslike_c?.toFloatOrNull()?.toInt()
                        val currentTempRounded = item.currentTemp?.toFloatOrNull()?.toInt()

                        val descriptionOfFeelingTemp = when {
                            feelsLikeRounded == null || currentTempRounded == null ->
                                "Temperature data is currently unavailable."

                            feelsLikeRounded > currentTempRounded ->
                                "The weather feels warmer than it actually is."

                            feelsLikeRounded < currentTempRounded ->
                                "The wind makes it feel colder."

                            else ->
                                "Matches the actual air temperature."
                        }

                        Text(
                            text = descriptionOfFeelingTemp,
                            fontSize = 12.sp,
                            color = Color.White,
                            fontFamily = ubuntuBold,
                            modifier = Modifier
                                .padding(start = 8.dp, bottom = 20.dp)
                                .align(Alignment.BottomStart)
                        )
                    }
                }

                // UV INDEX
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

                                Box(
                                    modifier = Modifier
                                        .size(25.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.uv_sun2),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(25.dp)
                                            .padding(end = 8.dp)
                                    )
                                }

                                Text(
                                    text = "UV INDEX",
                                    fontSize = 12.sp,
                                    fontFamily = ubuntuBold,
                                    color = Color.Gray
                                )
                            }

                            Text(
                                text = item.formattedUV(),
                                fontSize = 25.sp,
                                fontFamily = ubuntuBold,
                                color = Color.White,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )


                            val indexUV = item.uv?.toFloatOrNull()?.toInt()

                            val mainDescriptionOfUV = when {
                                indexUV == null -> "Index data is unavailable"
                                indexUV > 10 -> "Extreme"
                                indexUV in 8..10 -> "Very high"
                                indexUV in 6..7 -> "High"
                                indexUV in 3..5 -> "Moderate"
                                else -> "Low"
                            }

                            Text(
                                text = mainDescriptionOfUV,
                                fontSize = 16.sp,
                                fontFamily = ubuntuBold,
                                color = Color.LightGray,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                        }
                        val indexUV = item.uv?.toFloatOrNull()?.toInt()

                        val descriptionOfUV = when {
                            indexUV == null -> "UV index data is currently unavailable."
                            indexUV > 10 -> "Be sure to protect yourself from the sun, maximum protection is a must!"
                            indexUV in 8..10 -> "Avoid sun exposure, use maximum protection."
                            indexUV in 6..7 -> "Avoid prolonged sun exposure."
                            indexUV in 3..5 -> "Basic sun protection is recommended."
                            else -> "Being in the sun is currently safe."
                        }

                        Text(
                            text = descriptionOfUV,
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
                            .padding(
                                start = 16.dp,
                                end = 16.dp,
                                top = 8.dp,
                                bottom = 8.dp
                            ),

                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 8.dp)
                        ) {


                            Image(
                                painter = painterResource(id = R.drawable.wind),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(25.dp)
                                    .padding(end = 8.dp)
                            )
                            Text(
                                text = "WIND",
                                fontSize = 12.sp,
                                fontFamily = ubuntuBold,
                                color = Color.Gray
                            )
                        }


                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Wind",
                                fontSize = 16.sp,
                                fontFamily = ubuntuBold,
                                color = Color.White
                            )
                            Text(
                                text = item.formattedWindKMToMc + " m/s",
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
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp, bottom = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Gusts",
                                fontSize = 16.sp,
                                fontFamily = ubuntuBold,
                                color = Color.White
                            )
                            Text(
                                text = item.formattedGustKMToMc + " m/s",
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

                        val windDirToEN = mapOf(
                            "N" to "North",
                            "NE" to "Northeast",
                            "E" to "East",
                            "SE" to "Southeast",
                            "S" to "South",
                            "SW" to "Southwest",
                            "W" to "West",
                            "NW" to "Northwest",
                            "NNE" to "North-Northeast",
                            "ENE" to "East-Northeast",
                            "ESE" to "East-Southeast",
                            "SSE" to "South-Southeast",
                            "SSW" to "South-Southwest",
                            "WSW" to "West-Southwest",
                            "WNW" to "West-Northwest",
                            "NNW" to "North-Northwest"
                        )
                        val normalizedWindDir = item.wind_dir.trim()
                        val windDirEN = windDirToEN[normalizedWindDir] ?: "Unknown"

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Direction",
                                fontSize = 16.sp,
                                fontFamily = ubuntuBold,
                                color = Color.White
                            )
                            Text(
                                text = windDirEN,
                                fontSize = 16.sp,
                                fontFamily = ubuntuBold,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                val snowConditions = setOf(
                    "Snow",
                    "Light snow showers",
                    "Moderate or heavy snow showers",
                    "Blizzard",
                    "Patchy light snow",
                    "Light snow",
                    "Heavy snow",
                    "Patchy heavy snow",
                    "Moderate snow"
                )
                val isSnow = snowConditions.contains(item.condition)

                val snowOrPrecip = if (isSnow) {
                    "${item.formattedTotalSnowCm()} cm"
                } else {
                    "${item.formattedTotalPrecipMm()} mm"
                }

                val snow = item.formattedTotalSnowCm().toIntOrNull() ?: 0
                val precip = item.formattedTotalPrecipMm().toIntOrNull() ?: 0


                val desc = when {
                    snow > 0 && precip > 0 -> "Snow mixed with rain today."
                    snow > 0 -> "Snow today."
                    precip > 0 -> "Rain today." //maybe something else
                    snow == 0 && precip == 0 -> "No precipitation today."
                    else -> ""
                }


                // SNOW, PRECIP
                Card(
                    modifier = Modifier
                        .size(191.8.dp)
                        .padding(start = 20.dp, top = 20.dp),
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
                                .padding(8.dp),
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(bottom = 8.dp)
                            ) {

                                Box(
                                    modifier = Modifier
                                        .size(25.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.water_drop),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(18.dp)
                                            .padding(end = 8.dp)
                                    )
                                }

                                Text(
                                    text = "PRECIPITATION",
                                    fontSize = 12.sp,
                                    fontFamily = ubuntuBold,
                                    color = Color.Gray
                                )
                            }

                            Text(
                                text = snowOrPrecip,
                                fontSize = 25.sp,
                                fontFamily = ubuntuBold,
                                color = Color.White,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )

                        }

                        Text(
                            text = desc,
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
                            .background(colorResource(id = R.color.brownik))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp), //
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(bottom = 8.dp)
                            ) {

                                Box(
                                    modifier = Modifier
                                        .size(25.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.visibility),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(28.dp)
                                            .padding(end = 8.dp)
                                    )
                                }

                                Text(
                                    text = "VISIBILITY",
                                    fontSize = 12.sp,
                                    fontFamily = ubuntuBold,
                                    color = Color.Gray
                                )
                            }

                            Text(
                                text = item.formattedVisibilityKm() + " km",
                                fontSize = 25.sp,
                                fontFamily = ubuntuBold,
                                color = Color.White,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                        }

                        val vis = item.vis_km?.toFloatOrNull()?.toInt()

                        val descriptionOfVisibility = when {
                            vis == null -> "Visibility data is unavailable."
                            vis > 15 -> "Absolutely clear."
                            vis in 10..15 -> "Clear."
                            vis in 5..9 -> "Light haze is currently reducing visibility."
                            else -> "Visibility is very low."
                        }

                        Text(
                            text = descriptionOfVisibility,
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


            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                // PRESSURE
                Card(
                    modifier = Modifier
                        .size(191.8.dp)
                        .padding(start = 20.dp, top = 20.dp),
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
                                .padding(8.dp),
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(bottom = 8.dp)
                            ) {

                                Box(
                                    modifier = Modifier
                                        .size(25.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.pressure),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(22.dp)
                                            .padding(end = 8.dp)
                                    )
                                }

                                Text(
                                    text = "PRESSURE",
                                    fontSize = 12.sp,
                                    fontFamily = ubuntuBold,
                                    color = Color.Gray
                                )
                            }

                            Text(
                                text = item.formattedPressureMb() + " hPa",
                                fontSize = 25.sp,
                                fontFamily = ubuntuBold,
                                color = Color.White,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )

                        }

                        Text(
                            text = "",
                            fontSize = 12.sp,
                            color = Color.White,
                            fontFamily = ubuntuBold,
                            modifier = Modifier
                                .padding(start = 8.dp, bottom = 20.dp)
                                .align(Alignment.BottomStart)
                        )
                    }
                }

                // HUMIDITY
                Card(
                    modifier = Modifier
                        .size(191.8.dp)
                        .padding(end = 20.dp, top = 20.dp),
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
                                .padding(8.dp),
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(bottom = 8.dp)
                            ) {

                                Box(
                                    modifier = Modifier
                                        .size(25.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.humidity),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(25.dp)
                                            .padding(end = 8.dp)
                                    )
                                }

                                Text(
                                    text = "HUMIDITY",
                                    fontSize = 12.sp,
                                    fontFamily = ubuntuBold,
                                    color = Color.Gray
                                )
                            }

                            Text(
                                text = item.humidity + "%",
                                fontSize = 25.sp,
                                fontFamily = ubuntuBold,
                                color = Color.White,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                        }



                        Text(
                            text = "Dew point: ${item.dewpoint_c.toFloat().toInt()}°C",
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

        }

    }
}