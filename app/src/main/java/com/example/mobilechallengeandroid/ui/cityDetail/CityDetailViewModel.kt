package com.example.mobilechallengeandroid.ui.cityDetail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilechallengeandroid.utils.fahrenheitToCelsius
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.mobilechallengeandroid.domain.model.CityItem
import com.example.mobilechallengeandroid.domain.usecase.GetWeatherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.example.mobilechallengeandroid.ui.cityDetail.coordinator.CityDetailState

@HiltViewModel
class CityDetailViewModel @Inject constructor(
    private val getWeatherUseCase: GetWeatherUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(CityDetailState())
    val state: StateFlow<CityDetailState> = _state

    fun loadCityDetails(cityItem: CityItem, mapUrl: String? = null) {
        viewModelScope.launch {
            Log.d("CityDetailViewModel", "Loading weather for city: ${cityItem.name}, lat: ${cityItem.coordinates.lat}, lon: ${cityItem.coordinates.lon}")
            _state.value = _state.value.copy(isLoading = true)
            try {
                val weather = getWeatherUseCase(cityItem)
                Log.d("CityDetailViewModel", "Weather result: $weather")
                _state.value = _state.value.copy(
                    weather = weather,
                    temperatureCelsius = weather?.temperature?.let { fahrenheitToCelsius(it) },
                    feelsLikeCelsius = weather?.feelsLike?.let { fahrenheitToCelsius(it) },
                    mapUrl = mapUrl,
                    isLoading = false
                )
            } catch (e: Exception) {
                Log.e("CityDetailViewModel", "Error loading weather", e)
                _state.value = _state.value.copy(
                    weather = null,
                    isLoading = false
                )
            }
        }
    }
}