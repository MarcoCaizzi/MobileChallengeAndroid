package com.example.mobilechallengeandroid.data.local

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mobilechallengeandroid.data.model.Coord

@Entity(tableName = "cities")
data class CityEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val country: String,
    @Embedded(prefix = "coord_") val coord: Coord
)