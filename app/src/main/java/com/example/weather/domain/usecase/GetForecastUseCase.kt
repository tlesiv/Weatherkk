package com.example.weather.domain.usecase

import com.example.weather.domain.model.WeatherForecast
import com.example.weather.domain.repository.WeatherRepository

class GetForecastUseCase(
    private val repository: WeatherRepository,
) {
    suspend operator fun invoke(city: String): Result<WeatherForecast> = runCatching {
        val query = city.trim()
        require(query.isNotEmpty()) { "City name must not be blank" }
        repository.getForecast(query)
    }
}