package com.example.weather.domain.repository

import com.example.weather.domain.model.WeatherForecast

interface WeatherRepository {
    suspend fun getForecast(city: String): WeatherForecast
}