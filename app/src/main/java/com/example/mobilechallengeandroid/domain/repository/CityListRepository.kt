package com.example.mobilechallengeandroid.domain.repository

import androidx.paging.PagingData
import com.example.mobilechallengeandroid.data.database.entities.CityEntity
import com.example.mobilechallengeandroid.domain.model.CityItem
import kotlinx.coroutines.flow.Flow

interface CityListRepository {

    suspend fun getAllCitiesFromApi(): List<CityItem>
    suspend fun getPagedCitiesFromDatabase(prefix: String): Flow<PagingData<CityItem>>
    suspend fun getPagedFavoriteCitiesFromDatabase(prefix: String, ids: Set<Long>): Flow<PagingData<CityItem>>
    suspend fun insertCitiesInDatabase(cities: List<CityEntity>)
    suspend fun clearCities()
    suspend fun countCities(): Int
    suspend fun getCityById(cityId: Long): CityItem?
}