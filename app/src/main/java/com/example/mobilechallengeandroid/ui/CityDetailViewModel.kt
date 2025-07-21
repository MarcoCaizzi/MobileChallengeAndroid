package com.example.mobilechallengeandroid.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilechallengeandroid.data.City
import com.example.mobilechallengeandroid.data.CityRepository
import com.example.mobilechallengeandroid.data.WeatherData
import com.example.mobilechallengeandroid.utils.fahrenheitToCelsius
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class CityDetailViewModel(private val repository: CityRepository) : ViewModel() {
    private val _weather = MutableStateFlow<WeatherData?>(null)
    val weather: StateFlow<WeatherData?> = _weather

    val temperatureCelsius: StateFlow<Double?> = MutableStateFlow(null)
    val feelsLikeCelsius: StateFlow<Double?> = MutableStateFlow(null)
    val temperatureUnit: StateFlow<String> = MutableStateFlow("°C")

    fun loadWeather(city: City) {
        viewModelScope.launch {
            _weather.value = null
            (temperatureCelsius as MutableStateFlow).value = null
            (feelsLikeCelsius as MutableStateFlow).value = null
            (temperatureUnit as MutableStateFlow).value = "°C"

            val data = repository.getWeatherForCity(city)
            _weather.value = data
            temperatureCelsius.value = data?.temperature?.let { fahrenheitToCelsius(it) }
            feelsLikeCelsius.value = data?.feelsLike?.let { fahrenheitToCelsius(it) }
            temperatureUnit.value = "°C"
        }
    }
}