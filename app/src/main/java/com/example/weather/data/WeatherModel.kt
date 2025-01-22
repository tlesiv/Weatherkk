package com.example.weather.data

data class WeatherModel(
    val city: String,
    val time: String,
    val currentTemp: String,
    val condition: String,
    val icon: String,
    val maxTemp: String,
    val minTemp: String,
    val hours: String,
    val feelslike_c: String,
    val vis_km: String,
    val wind_kph: String,
    val gust_kph: String,//пориви вітру
    val wind_dir: String,//напрямок вітру
    val pressure_mb: String,//тиск
    val is_day: String,
    val totalsnow_cm: String,
    val totalprecip_mm: String,//опади в мм
    val uv: String,//індекс УФ
    val humidity: String,//вологість
    val dewpoint_c: String,//точка роси




) {
    fun formattedFeelingTemp(): String {
        return try {
            if (feelslike_c.isNotEmpty()) {
                "%.0f".format(feelslike_c.toFloat())
            } else {
                "N/A"
            }
        } catch (e: NumberFormatException) {
            "N/A"
        }
    }


    fun formattedCurrentTemp(): String {
        return try {
            if (currentTemp.isNotEmpty()) {
                "%.0f".format(currentTemp.toFloat())
            } else {
                "N/A"
            }
        } catch (e: NumberFormatException) {
            "N/A"
        }
    }

    fun formattedVisibilityKm(): String {
        return try {
            if (vis_km.isNotEmpty()) {
                "%.0f".format(vis_km.toFloat())
            } else {
                "N/A"
            }
        } catch (e: NumberFormatException) {
            "N/A"
        }
    }

    fun formattedPressureMb(): String {
        return try {
            if (pressure_mb.isNotEmpty()) {
                "%.0f".format(pressure_mb.toFloat())
            } else {
                "N/A"
            }
        } catch (e: NumberFormatException) {
            "N/A"
        }
    }

    fun formattedUV(): String {
        return try {
            if (uv.isNotEmpty()) {
                "%.0f".format(uv.toFloat())
            } else {
                "N/A"
            }
        } catch (e: NumberFormatException) {
            "N/A"
        }
    }

    fun formattedDewpointC(): String {
        return try {
            if (dewpoint_c.isNotEmpty()) {
                "%.0f".format(dewpoint_c.toFloat())
            } else {
                "N/A"
            }
        } catch (e: NumberFormatException) {
            "N/A"
        }
    }

    fun formattedTotalSnowCm(): String {
        return try {
            if (totalsnow_cm.isNotEmpty()) {
                "%.0f".format(totalsnow_cm.toFloat())
            } else {
                "N/A"
            }
        } catch (e: NumberFormatException) {
            "N/A"
        }
    }

    fun formattedTotalPrecipMm(): String {
        return try {
            if (totalprecip_mm.isNotEmpty()) {
                "%.0f".format(totalprecip_mm.toFloat())
            } else {
                "N/A"
            }
        } catch (e: NumberFormatException) {
            "N/A"
        }
    }

    fun formattedWindKM(): Float? {//зробити ліпше округлення через бібліотеку import kotlin.math.round
        return try {
            if (wind_kph.isNotEmpty()) {
                wind_kph.toFloat()
            } else {
                null
            }
        } catch (e: NumberFormatException) {
            null
        }
    }

    val formattedWindKMToMc = formattedWindKM()?.let { windKph ->
        "%.0f".format(windKph / 3.6)
    } ?: "N/A"


    fun formattedGustKM(): Float? {//зробити ліпше округлення через бібліотеку import kotlin.math.round
        return try {
            if (gust_kph.isNotEmpty()) {
                gust_kph.toFloat()
            } else {
                null
            }
        } catch (e: NumberFormatException) {
            null
        }
    }

    val formattedGustKMToMc = formattedGustKM()?.let { gustKph ->
        "%.0f".format(gustKph / 3.6)
    } ?: "N/A"








}




