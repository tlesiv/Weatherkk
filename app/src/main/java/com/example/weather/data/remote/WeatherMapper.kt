package com.example.weather.data.remote

import com.example.weather.data.remote.dto.CurrentDto
import com.example.weather.data.remote.dto.ForecastDayDto
import com.example.weather.data.remote.dto.ForecastResponse
import com.example.weather.data.remote.dto.HourDto
import com.example.weather.domain.model.CurrentWeather
import com.example.weather.domain.model.DayForecast
import com.example.weather.domain.model.HourForecast
import com.example.weather.domain.model.WeatherForecast

fun ForecastResponse.toDomain(): WeatherForecast = WeatherForecast(
    city = location.name,
    current = current.toDomain(),
    days = forecast.forecastDays.map { it.toDomain() },
)

private fun CurrentDto.toDomain() = CurrentWeather(
    time = lastUpdated,
    tempC = tempC,
    feelsLikeC = feelsLikeC,
    condition = condition.text,
    windKph = windKph,
    gustKph = gustKph,
    windDir = windDir,
    pressureMb = pressureMb,
    humidity = humidity,
    dewpointC = dewpointC,
    visKm = visKm,
    uv = uv,
)

private fun ForecastDayDto.toDomain() = DayForecast(
    date = date,
    maxTempC = day.maxTempC,
    minTempC = day.minTempC,
    condition = day.condition.text,
    totalSnowCm = day.totalSnowCm,
    totalPrecipMm = day.totalPrecipMm,
    hours = hour.map { it.toDomain() },
)

private fun HourDto.toDomain() = HourForecast(
    time = time,
    tempC = tempC,
    condition = condition.text,
)