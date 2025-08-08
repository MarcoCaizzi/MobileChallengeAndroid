package com.example.mobilechallengeandroid.ui.cityList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.mobilechallengeandroid.domain.model.CityItem
import com.example.mobilechallengeandroid.domain.usecase.FavoriteUseCase
import com.example.mobilechallengeandroid.domain.usecase.GetCitiesUseCase
import com.example.mobilechallengeandroid.domain.usecase.GetPagedCitiesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CityListViewModel @Inject constructor(
    private val getPagedCitiesUseCase: GetPagedCitiesUseCase,
    private val getCitiesUseCase: GetCitiesUseCase,
    private val favoriteUseCase: FavoriteUseCase
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
    val pagedCities: Flow<PagingData<CityItem>> = combine(
        filter,
        favoriteIds,
        showOnlyFavorites
    ) { filter, favoriteIds, showOnlyFavorites ->
        Triple(filter, favoriteIds, showOnlyFavorites)
    }.flatMapLatest { (filter, favoriteIds, showOnlyFavorites) ->
        getPagedCitiesUseCase(filter, favoriteIds, showOnlyFavorites)
    }.cachedIn(viewModelScope)

    init {
        viewModelScope.launch {
            getCitiesUseCase.getOrLoadPagedCities("").collect()
            _favoriteIds.value = favoriteUseCase.getFavoriteIds()
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
            favoriteUseCase.toggleFavorite(cityId)
            _favoriteIds.value = favoriteUseCase.getFavoriteIds()
        }
    }

    fun selectCity(cityId: Long) {
        _selectedCityId.value = cityId
    }

    suspend fun getCityById(cityId: Long): CityItem? {
        return getCitiesUseCase.getCityById(cityId)
    }
}
