package com.example.mobilechallengeandroid.data.model

import androidx.room.Embedded
import com.google.gson.annotations.SerializedName

data class CityModel(
    @SerializedName("_id")
    val id: Long,
    @SerializedName("name")
    val name: String,
    @SerializedName("country")
    val country: String,
    @SerializedName("coord")
    @Embedded(prefix = "coordinates_")
    val coordinates: Coordinates?
) {
    data class Coordinates(
        @SerializedName("lat")
        val lat: Double,
        @SerializedName("lon")
        val lon: Double
    )
}