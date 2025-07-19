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

    private val _showOnlyFavorites = MutableStateFlow(false)
    val showOnlyFavorites: StateFlow<Boolean> = _showOnlyFavorites

    init {
        viewModelScope.launch {
            allCities = repository.downloadAndFetchCities(CityRepositoryImpl.CITIES_JSON_URL)
            cityTrie = CityTrie().apply {
                allCities.forEach { insert(it) }
            }
            _favoriteIds.value = repository.getFavoriteIds()
            updateCities()
        }
    }

    fun onShowOnlyFavoritesChange(show: Boolean) {
        _showOnlyFavorites.value = show
        updateCities()
    }

    private fun updateCities() {
        val prefix = _filter.value.trim().lowercase()
        val filtered = if (prefix.isEmpty()) {
            allCities
        } else {
            cityTrie?.search(prefix) ?: emptyList()
        }
        val result = if (_showOnlyFavorites.value) {
            filtered.filter { _favoriteIds.value.contains(it.id) }
        } else {
            filtered
        }
        _cities.value = result.sortedWith(compareBy({ it.name.lowercase() }, { it.country.lowercase() }))
    }

    fun onFilterChange(newFilter: String) {
        _filter.value = newFilter
        updateCities()
    }

    fun onFavoriteClick(cityId: Long) {
        viewModelScope.launch {
            repository.toggleFavorite(cityId)
            _favoriteIds.value = repository.getFavoriteIds()
            updateCities()
        }
    }
}