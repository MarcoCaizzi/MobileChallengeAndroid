package com.example.mobilechallengeandroid.domain.usecase

import com.example.mobilechallengeandroid.domain.model.CityItem
import com.example.mobilechallengeandroid.domain.model.WeatherItem
import com.example.mobilechallengeandroid.domain.repository.WeatherRepository
import javax.inject.Inject

class GetWeatherUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
) {
    suspend operator fun invoke(cityItem: CityItem): WeatherItem? {
        return weatherRepository.getWeatherForCity(cityItem)
    }
}
