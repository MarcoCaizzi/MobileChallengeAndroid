package com.example.mobilechallengeandroid.data.model

data class Coord(
    val lon: Double,
    val lat: Double
)

data class City(
    val id: Long,
    val name: String,
    val country: String,
    val coord: Coord
)