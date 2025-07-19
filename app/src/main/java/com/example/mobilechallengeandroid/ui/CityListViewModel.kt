package com.example.mobilechallengeandroid.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilechallengeandroid.data.City
import com.example.mobilechallengeandroid.data.CityRepository
import com.example.mobilechallengeandroid.data.CityRepositoryImpl
import com.example.mobilechallengeandroid.data.CityTrie
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CityListViewModel(
    private val repository: CityRepository
) : ViewModel() {

    private val _filter = MutableStateFlow("")
    val filter: StateFlow<String> = _filter

    private val _cities = MutableStateFlow<List<City>>(emptyList())
    val cities: StateFlow<List<City>> = _cities
    private var allCities: List<City> = emptyList()
    private var cityTrie: CityTrie? = null
    private val _favoriteIds = MutableStateFlow<Set<Long>>(emptySet())
    val favoriteIds: StateFlow<Set<Long>> = _favoriteIds

    init {
        viewModelScope.launch {
            allCities = repository.downloadAndFetchCities(CityRepositoryImpl.CITIES_JSON_URL)
            cityTrie = CityTrie().apply {
                allCities.forEach { insert(it) }
            }
            _cities.value = allCities
            _favoriteIds.value = repository.getFavoriteIds()
        }
    }

    fun onFilterChange(newFilter: String) {
        _filter.value = newFilter
        val prefix = newFilter.trim().lowercase()
        val filtered = if (prefix.isEmpty()) {
            allCities
        } else {
            cityTrie?.search(prefix) ?: emptyList()
        }
        _cities.value =
            filtered.sortedWith(compareBy({ it.name.lowercase() }, { it.country.lowercase() }))
    }

    fun onFavoriteClick(cityId: Long) {
        viewModelScope.launch {
            repository.toggleFavorite(cityId)
            _favoriteIds.value = repository.getFavoriteIds()
        }
    }
}