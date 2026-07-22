package com.example.weather.data

data class WeatherModel(
    val city: String,
    val time: String,
    val currentTemp: String,
    val condition: String,
    val icon: String,
    val maxTemp: String,
    val minTemp: String,
    val hours: String,
    val feelslike_c: String,
    val vis_km: String,
    val wind_kph: String,
    val gust_kph: String,
    val wind_dir: String,
    val pressure_mb: String,
    val is_day: String,
    val totalsnow_cm: String,
    val totalprecip_mm: String,
    val uv: String,
    val humidity: String,
    val dewpoint_c: String,
) {

    private fun formatNumber(value: String): String = value.toFloatOrNull()?.let { "%.0f".format(it) } ?: "N/A"

    fun formattedFeelingTemp(): String = formatNumber(feelslike_c)

    fun formattedVisibilityKm(): String = formatNumber(vis_km)

    fun formattedPressureMb(): String = formatNumber(pressure_mb)

    fun formattedUV(): String = formatNumber(uv)

    fun formattedTotalSnowCm(): String = formatNumber(totalsnow_cm)

    fun formattedTotalPrecipMm(): String = formatNumber(totalprecip_mm)

    val formattedWindKMToMc: String = wind_kph.toFloatOrNull()
        ?.let { windKph -> "%.0f".format(windKph / 3.6) }
        ?: "N/A"

    val formattedGustKMToMc: String = gust_kph.toFloatOrNull()
        ?.let { gustKph -> "%.0f".format(gustKph / 3.6) }
        ?: "N/A"
}