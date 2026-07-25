package com.example.weather.data.remote

import com.example.weather.BuildConfig
import com.example.weather.data.remote.dto.ForecastResponse
import javax.inject.Inject

class RetrofitWeatherRemoteDataSource @Inject constructor(
    private val api: WeatherApi,
) : WeatherRemoteDataSource {

    override suspend fun fetchForecast(city: String): ForecastResponse =
        api.getForecast(apiKey = BuildConfig.WEATHER_API_KEY, city = city)
}