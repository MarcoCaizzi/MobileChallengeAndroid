package com.example.mobilechallegeandroid.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilechallegeandroid.data.City
import com.example.mobilechallegeandroid.data.CityRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CityListViewModel(
    private val repository: CityRepository
) : ViewModel() {

    private val _filter = MutableStateFlow("")
    val filter: StateFlow<String> = _filter

    private val _cities = MutableStateFlow<List<City>>(emptyList())
    val cities: StateFlow<List<City>> = _cities

    init {
        viewModelScope.launch {
            val allCities = repository.fetchCities()
            _cities.value = allCities
        }
    }

    fun onFilterChange(newFilter: String) {
        _filter.value = newFilter
        _cities.value = _cities.value.filter {
            it.name.lowercase().startsWith(newFilter.lowercase())
        }
    }

    fun onFavoriteClick(cityId: Long) {
        viewModelScope.launch {
            repository.toggleFavorite(cityId)
            val allCities = repository.fetchCities()
            _cities.value = allCities.filter {
                it.name.lowercase().startsWith(_filter.value.lowercase())
            }
        }
    }
}