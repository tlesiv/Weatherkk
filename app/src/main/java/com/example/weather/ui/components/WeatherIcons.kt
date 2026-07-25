package com.example.weather.ui.components

import com.example.weather.R

fun weatherIconFor(condition: String): Int = when (condition.trim()) {
    "Sunny", "Clear" -> R.drawable.sun
    "Cloudy", "Overcast" -> R.drawable.white_cloud
    "Partly Cloudy" -> R.drawable.partly_cloud
    "Rainy", "Light drizzle", "Patchy rain nearby",
    "Light freezing rain" -> R.drawable.white_cloud_rain_br
    "Thunder" -> R.drawable.white_cloud_thunder
    "Snow", "Light snow showers", "Moderate or heavy snow showers", "Blizzard",
    "Patchy light snow", "Light snow", "Heavy snow",
    "Patchy heavy snow", "Moderate snow" -> R.drawable.snowflake
    else -> R.drawable.white_cloud
}