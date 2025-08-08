package com.example.mobilechallengeandroid.domain.model

import com.example.mobilechallengeandroid.data.database.entities.CityEntity
import com.example.mobilechallengeandroid.data.model.CityModel


data class CityItem(
    val id: Long,
    val name: String,
    val country: String,
    val coordinates: CoordinatesItem
)

data class CoordinatesItem(
    val lat: Double,
    val lon: Double
)


fun CityModel.toDomain() = CityItem(
    id = id,
    name = name,
    country = country,
    coordinates = CoordinatesItem(
        lat = coordinates?.lat ?: 0.0,
        lon = coordinates?.lon ?: 0.0
    )
)

fun CityEntity.toDomain() = CityItem(
    id = id,
    name = name,
    country = country,
    coordinates = CoordinatesItem(
        lat = lat,
        lon = lon
    )
)
