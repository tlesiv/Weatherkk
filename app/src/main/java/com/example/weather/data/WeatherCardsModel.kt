package com.example.weather.data

data class WeatherCardsModel(
    val feelslike_c: String,
    val wind_kph: String,
    val gust_kph: String,//пориви вітру
    val vis_km: String,
    val wind_dir: String,//напрямок вітру
    val pressure_mb: String,//тиск
    val precip_mm: String,//опади в мм

)
