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
    val feelslike_c: String
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

}




// val wind_kph: String,
    //val gust_kph: String,//пориви вітру
   // val vis_km: String,
   // val wind_dir: String,//напрямок вітру
   // val pressure_mb: String,//тиск
   // val precip_mm: String,//опади в мм

