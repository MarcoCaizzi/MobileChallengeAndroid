package com.example.mobilechallengeandroid.data.model

data class City(
    val id: Long,
    val name: String,
    val country: String,
    val coord: Coord
) {
    data class Coord(
        val lat: Double,
        val lon: Double
    )
}