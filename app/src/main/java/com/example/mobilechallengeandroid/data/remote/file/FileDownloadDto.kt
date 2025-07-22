package com.example.mobilechallengeandroid.data.remote.file

data class CityJson(
    val _id: Long,
    val name: String,
    val country: String,
    val coord: CoordJson
)

data class CoordJson(
    val lon: Double,
    val lat: Double
)