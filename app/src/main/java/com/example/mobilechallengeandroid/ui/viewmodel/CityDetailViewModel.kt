package com.example.mobilechallengeandroid.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilechallengeandroid.data.model.City
import com.example.mobilechallengeandroid.domain.CityRepository
import com.example.mobilechallengeandroid.data.model.WeatherData
import com.example.mobilechallengeandroid.utils.fahrenheitToCelsius
import com.example.mobilechallengeandroid.utils.getStaticMapUrl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.content.Context
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import dagger.hilt.android.qualifiers.ApplicationContext


data class CityDetailState(
    val weather: WeatherData? = null,
    val temperatureCelsius: Double? = null,
    val feelsLikeCelsius: Double? = null,
    val temperatureUnit: String = "°C",
    val mapUrl: String? = null,
    val isLoading: Boolean = false
)

@HiltViewModel
class CityDetailViewModel @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val repository: CityRepository
) : ViewModel() {
    private val _state = MutableStateFlow(CityDetailState())
    val state: StateFlow<CityDetailState> = _state

    fun loadCityDetails(city: City) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            val mapUrl = getStaticMapUrl(context,city)
            val weather = repository.getWeatherForCity(city)
            _state.value = _state.value.copy(
                weather = weather,
                temperatureCelsius = weather?.temperature?.let { fahrenheitToCelsius(it) },
                feelsLikeCelsius = weather?.feelsLike?.let { fahrenheitToCelsius(it) },
                mapUrl = mapUrl,
                isLoading = false
            )
        }
    }
}