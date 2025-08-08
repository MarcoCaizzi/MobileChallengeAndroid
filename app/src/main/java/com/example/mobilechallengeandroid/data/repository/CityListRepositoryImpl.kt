package com.example.mobilechallengeandroid.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.mobilechallengeandroid.data.database.dao.CityDao
import com.example.mobilechallengeandroid.data.database.entities.CityEntity
import com.example.mobilechallengeandroid.domain.repository.CityListRepository
import com.example.mobilechallengeandroid.data.model.CityModel
import com.example.mobilechallengeandroid.data.network.file.CityListService
import com.example.mobilechallengeandroid.domain.model.CityItem
import com.example.mobilechallengeandroid.domain.model.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CityListRepositoryImpl @Inject constructor(
    private val cityDao: CityDao,
    private val api: CityListService
) : CityListRepository {

    override suspend fun getAllCitiesFromApi(): List<CityItem> {
        android.util.Log.d("CityListRepositoryImpl", "Fetching cities from API...")
        val response: List<CityModel> = api.getAllCities()
        response.forEach {
                android.util.Log.w("CityListRepositoryImpl", "City with missing coordinates: id=${it.id}, name=${it.name}, country=${it.country}")
            }
        return response.map { it.toDomain() }
    }

    override suspend fun getPagedCitiesFromDatabase(prefix: String): Flow<PagingData<CityItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20, enablePlaceholders = false
            ),
            pagingSourceFactory = { cityDao.getCitiesPagingSource(prefix) }
        ).flow.map { pagingData ->
            pagingData.map { it.toDomain() }
        }
    }

    override suspend fun getPagedFavoriteCitiesFromDatabase(prefix: String, ids: Set<Long>): Flow<PagingData<CityItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20, enablePlaceholders = false
            ),
            pagingSourceFactory = { cityDao.getFavoriteCitiesPagingSource(prefix, ids.toList()) }
        ).flow.map { pagingData ->
            pagingData.map { it.toDomain() }
        }
    }

    override suspend fun insertCitiesInDatabase(cities: List<CityEntity>) {
        android.util.Log.d("CityListRepositoryImpl", "Saving ${cities.size} cities to database...")
        cityDao.insertAll(cities)
        android.util.Log.d("CityListRepositoryImpl", "Cities saved to database.")
    }

    override suspend fun clearCities() {
        cityDao.clearAll()
    }

    override suspend fun countCities(): Int {
        return cityDao.countCities()
    }

    override suspend fun getCityById(cityId: Long): CityItem? {
        return cityDao.getCityById(cityId)?.toDomain()
    }
}