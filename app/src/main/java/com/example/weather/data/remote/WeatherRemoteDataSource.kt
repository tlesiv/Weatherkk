package com.example.weather.data.remote

import com.example.weather.data.remote.dto.ForecastResponse

interface WeatherRemoteDataSource {
    suspend fun fetchForecast(city: String): ForecastResponse
}