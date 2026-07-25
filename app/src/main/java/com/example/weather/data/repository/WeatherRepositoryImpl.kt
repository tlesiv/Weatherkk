package com.example.weather.data.repository

import com.example.weather.data.remote.WeatherRemoteDataSource
import com.example.weather.data.remote.toDomain
import com.example.weather.domain.model.WeatherForecast
import com.example.weather.domain.repository.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val remoteDataSource: WeatherRemoteDataSource,
) : WeatherRepository {

    override suspend fun getForecast(city: String): WeatherForecast =
        remoteDataSource.fetchForecast(city).toDomain()
}