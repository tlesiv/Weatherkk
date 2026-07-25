package com.example.weather.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ForecastResponse(
    val location: LocationDto,
    val current: CurrentDto,
    val forecast: ForecastDto,
)

@Serializable
data class LocationDto(
    val name: String,
)

@Serializable
data class ConditionDto(
    val text: String = "",
)

@Serializable
data class CurrentDto(
    @SerialName("last_updated") val lastUpdated: String = "",
    @SerialName("temp_c") val tempC: Double,
    @SerialName("feelslike_c") val feelsLikeC: Double = 0.0,
    val condition: ConditionDto = ConditionDto(),
    @SerialName("wind_kph") val windKph: Double = 0.0,
    @SerialName("gust_kph") val gustKph: Double = 0.0,
    @SerialName("wind_dir") val windDir: String = "",
    @SerialName("pressure_mb") val pressureMb: Double = 0.0,
    val humidity: Int = 0,
    @SerialName("dewpoint_c") val dewpointC: Double = 0.0,
    @SerialName("vis_km") val visKm: Double = 0.0,
    val uv: Double = 0.0,
)

@Serializable
data class ForecastDto(
    @SerialName("forecastday") val forecastDays: List<ForecastDayDto>,
)

@Serializable
data class ForecastDayDto(
    val date: String,
    val day: DayDto,
    val hour: List<HourDto> = emptyList(),
)

@Serializable
data class DayDto(
    @SerialName("maxtemp_c") val maxTempC: Double = 0.0,
    @SerialName("mintemp_c") val minTempC: Double = 0.0,
    val condition: ConditionDto = ConditionDto(),
    @SerialName("totalsnow_cm") val totalSnowCm: Double = 0.0,
    @SerialName("totalprecip_mm") val totalPrecipMm: Double = 0.0,
)

@Serializable
data class HourDto(
    val time: String,
    @SerialName("temp_c") val tempC: Double = 0.0,
    val condition: ConditionDto = ConditionDto(),
)