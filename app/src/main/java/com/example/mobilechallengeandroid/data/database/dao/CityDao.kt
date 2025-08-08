package com.example.mobilechallengeandroid.data.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mobilechallengeandroid.data.database.entities.CityEntity

@Dao
interface CityDao {

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertAll(cities: List<CityEntity>)

    @Query("SELECT * FROM cities WHERE name LIKE :prefix || '%'")
    suspend fun searchByName(prefix: String): List<CityEntity>

    @Query("SELECT * FROM cities WHERE name LIKE :prefix || '%' ORDER BY name ASC")
    fun getCitiesPagingSource(prefix: String): PagingSource<Int, CityEntity>

    @Query("SELECT * FROM cities WHERE id IN (:ids)")
    suspend fun getCitiesByIds(ids: List<Long>): List<CityEntity>

    @Query("SELECT * FROM cities WHERE id = :id LIMIT 1")
    suspend fun getCityById(id: Long): CityEntity?

    @Query("DELETE FROM cities")
    suspend fun clearAll()

    @Query("SELECT COUNT(*) FROM cities")
    suspend fun countCities(): Int

    @Query("SELECT * FROM cities WHERE name LIKE :prefix || '%' AND id IN (:ids) ORDER BY name ASC")
    fun getFavoriteCitiesPagingSource(prefix: String, ids: List<Long>): PagingSource<Int, CityEntity>
}