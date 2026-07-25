package com.example.weather.ui.main

import com.example.weather.domain.model.WeatherForecast

data class WeatherUiState(
    val isLoading: Boolean = true,
    val forecast: WeatherForecast? = null,
    val errorMessage: String? = null,
)