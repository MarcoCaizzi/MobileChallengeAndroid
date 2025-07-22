package com.example.mobilechallengeandroid.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilechallengeandroid.data.model.City
import com.example.mobilechallengeandroid.domain.CityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.ExperimentalCoroutinesApi

@HiltViewModel
class CityListViewModel @Inject constructor(
    private val repository: CityRepository
) : ViewModel() {

    private val _filter = MutableStateFlow("")
    val filter: StateFlow<String> = _filter

    private val _favoriteIds = MutableStateFlow<Set<Long>>(emptySet())
    val favoriteIds: StateFlow<Set<Long>> = _favoriteIds

    private val _showOnlyFavorites = MutableStateFlow(false)
    val showOnlyFavorites: StateFlow<Boolean> = _showOnlyFavorites

    private val _selectedCityId = MutableStateFlow<Long?>(null)
    val selectedCityId: StateFlow<Long?> = _selectedCityId

    @OptIn(ExperimentalCoroutinesApi::class)
    val pagedCities: Flow<PagingData<City>> = combine(
        filter,
        favoriteIds,
        showOnlyFavorites
    ) { filter, favoriteIds, showOnlyFavorites ->
        Triple(filter, favoriteIds, showOnlyFavorites)
    }.flatMapLatest { (filter, favoriteIds, showOnlyFavorites) ->
        if (showOnlyFavorites) {
            flow {
                val cities = repository.getCitiesByIds(favoriteIds.toList())
                    .filter { it.name.startsWith(filter, ignoreCase = true) }
                    .sortedBy { it.name }
                emit(PagingData.from(cities))
            }
        } else {
            repository.getPagedCities(filter.trim())
        }
    }.cachedIn(viewModelScope)

    init {
        viewModelScope.launch {
            if (repository.searchCitiesByPrefix("").isEmpty()) {
                repository.downloadAndFetchCities()
            }
            _favoriteIds.value = repository.getFavoriteIds()
        }
    }

    fun onFilterChange(newFilter: String) {
        _filter.value = newFilter
    }

    fun onShowOnlyFavoritesChange(show: Boolean) {
        _showOnlyFavorites.value = show
    }

    fun onFavoriteClick(cityId: Long) {
        viewModelScope.launch {
            repository.toggleFavorite(cityId)
            _favoriteIds.value = repository.getFavoriteIds()
        }
    }

    fun selectCity(cityId: Long) {
        _selectedCityId.value = cityId
    }

    suspend fun getCityById(cityId: Long): City? {
        return repository.getCityById(cityId)
    }
}