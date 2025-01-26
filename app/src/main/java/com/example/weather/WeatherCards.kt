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

                        val descriptionOfFillingTemp = when {
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
                            text = descriptionOfFillingTemp,
                            fontSize = 12.sp,
                            color = Color.White,
                            fontFamily = ubuntuBold,
                            modifier = Modifier
                                .padding(start = 8.dp, bottom = 20.dp)
                                .align(Alignment.BottomStart)
                        )
                    }
                }

                // ІНДЕКС УФ
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
                                            .size(25.dp)//або 25
                                            .padding(end = 8.dp)
                                    )
                                }

                                Text(
                                    text = "ІНДЕКС УФ",
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
                                indexUV == null -> "Дані про індекс недоступні"
                                indexUV > 10 -> "Екстремальний"
                                indexUV in 8..10 -> "Дуже високий"
                                indexUV in 6..7 -> "Високий"
                                indexUV in 3..5 -> "Помірний"
                                else -> "Низький"
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
                            indexUV == null -> "Дані про ультрафіолетовий індекс наразі недоступні."
                            indexUV > 10 -> "Неодмінно захищайтеся від сонця, максимальний захист обов'язковий!"
                            indexUV in 8..10 -> "Уникайте перебування на сонці, застосовуйте максимальний захист."
                            indexUV in 6..7 -> "Уникайте тривалого перебування на сонці."
                            indexUV in 3..5 -> "Рекомендується використовувати базовий захист від сонця."
                            else -> "Перебування на сонці наразі безпечне."
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
                            ),//або просто padding(16.dp)

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
                                text = "ВІТЕР",
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
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp, bottom = 8.dp),
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

                        val windDirToUA = mapOf(
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
                        val windDirUA = windDirToUA[normalizedWindDir] ?: "Невідомо"

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Напрямок",
                                fontSize = 16.sp,
                                fontFamily = ubuntuBold,
                                color = Color.White
                            )
                            Text(
                                text = windDirUA,
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
                    "${item.formattedTotalSnowCm()} см"
                } else {
                    "${item.formattedTotalPrecipMm()} мм"
                }

                val snow = item.formattedTotalSnowCm().toIntOrNull() ?: 0
                val precip = item.formattedTotalPrecipMm().toIntOrNull() ?: 0


                val desc = when {
                    snow > 0 && precip > 0 -> "Сьогодні сніг з дощем."
                    snow > 0 -> "Сьогодні сніжно."
                    precip > 0 -> "Сьогодні дощить."//мб щось інше
                    snow == 0 && precip == 0 -> "Сьогодні без опадів."
                    else -> ""
                }




                //SNOW,PRECIP
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
                                    text = "ОПАДИ",
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
                                .padding(8.dp),//
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

                        val descriptionOfVisibility = when {
                            vis == null -> "Дані про видимість недоступні."
                            vis > 15 -> "Абсолютно ясно."
                            vis in 10..15 -> "Ясно."
                            vis in 5..9 -> "Легка імла зараз зменшує видимість."
                            else -> "Видимість дуже низька."
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

                //ТИСК (PRESSURE)
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
                                    text = "ТИСК",
                                    fontSize = 12.sp,
                                    fontFamily = ubuntuBold,
                                    color = Color.Gray
                                )
                            }

                            Text(
                                text = item.formattedPressureMb() + " гПа",
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

                // HUMIDITY(ВОЛОГІСТЬ)
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
                                .padding(8.dp),//
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
                                    text = "ВОЛОГІСТЬ",
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
                            text = "Точка роси: ${item.dewpoint_c.toFloat().toInt()}°С",
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