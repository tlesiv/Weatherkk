package com.example.weather.domain.model

data class WeatherForecast(
    val city: String,
    val current: CurrentWeather,
    val days: List<DayForecast>,
)

data class CurrentWeather(
    val time: String,
    val tempC: Double,
    val feelsLikeC: Double,
    val condition: String,
    val windKph: Double,
    val gustKph: Double,
    val windDir: String,
    val pressureMb: Double,
    val humidity: Int,
    val dewpointC: Double,
    val visKm: Double,
    val uv: Double,
)

data class DayForecast(
    val date: String,
    val maxTempC: Double,
    val minTempC: Double,
    val condition: String,
    val totalSnowCm: Double,
    val totalPrecipMm: Double,
    val hours: List<HourForecast>,
)

data class HourForecast(
    val time: String,
    val tempC: Double,
    val condition: String,
)