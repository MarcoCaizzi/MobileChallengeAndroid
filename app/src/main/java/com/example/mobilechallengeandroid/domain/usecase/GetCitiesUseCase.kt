package com.example.mobilechallengeandroid.domain.usecase

import androidx.paging.PagingData
import com.example.mobilechallengeandroid.data.database.entities.toDatabase
import com.example.mobilechallengeandroid.domain.model.CityItem
import com.example.mobilechallengeandroid.domain.repository.CityListRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCitiesUseCase @Inject constructor(
    private val repository: CityListRepository
) {
    suspend fun getOrLoadPagedCities(prefix: String): Flow<PagingData<CityItem>> {
        val count = repository.countCities()
        if (count == 0) {
            val cities = repository.getAllCitiesFromApi()
            val entities = cities.map { it.toDatabase() }
            repository.clearCities()
            repository.insertCitiesInDatabase(entities)
        }
        return repository.getPagedCitiesFromDatabase(prefix)
    }

    suspend fun getCityById(cityId: Long): CityItem? {
        return repository.getCityById(cityId)
    }
}