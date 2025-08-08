package com.example.mobilechallengeandroid.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mobilechallengeandroid.domain.model.CityItem

@Entity(tableName = "cities")
data class CityEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "country")
    val country: String,
    @ColumnInfo(name = "lat")
    val lat: Double,
    @ColumnInfo(name = "lon")
    val lon: Double
)

fun CityItem.toDatabase() = CityEntity(
    id = id,
    name = name,
    country = country,
    lat = coordinates.lat,
    lon = coordinates.lon
)