
package com.example.mobilechallengeandroid.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilechallengeandroid.data.City
import com.example.mobilechallengeandroid.data.CityRepository
import com.example.mobilechallengeandroid.data.WeatherData
import com.example.mobilechallengeandroid.utils.fahrenheitToCelsius
import com.example.mobilechallengeandroid.utils.getStaticMapUrl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.content.Context

class CityDetailViewModel(private val repository: CityRepository) : ViewModel() {
    private val _weather = MutableStateFlow<WeatherData?>(null)
    val weather: StateFlow<WeatherData?> = _weather

    private val _temperatureCelsius = MutableStateFlow<Double?>(null)
    val temperatureCelsius: StateFlow<Double?> = _temperatureCelsius

    private val _feelsLikeCelsius = MutableStateFlow<Double?>(null)
    val feelsLikeCelsius: StateFlow<Double?> = _feelsLikeCelsius

    private val _temperatureUnit = MutableStateFlow("°C")
    val temperatureUnit: StateFlow<String> = _temperatureUnit

    private val _mapUrl = MutableStateFlow<String?>(null)
    val mapUrl: StateFlow<String?> = _mapUrl

    fun loadMapUrl(context: Context, city: City) {
        _mapUrl.value = getStaticMapUrl(context, city)
    }

    fun loadWeather(city: City) {
        viewModelScope.launch {
            _weather.value = null
            _temperatureCelsius.value = null
            _feelsLikeCelsius.value = null
            _temperatureUnit.value = "°C"

            val data = repository.getWeatherForCity(city)
            _weather.value = data
            _temperatureCelsius.value = data?.temperature?.let { fahrenheitToCelsius(it) }
            _feelsLikeCelsius.value = data?.feelsLike?.let { fahrenheitToCelsius(it) }
        }
    }
}