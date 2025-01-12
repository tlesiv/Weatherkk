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

    //val precip_mm: String//опади в мм

    // val pressure_mb: String,//тиск


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




