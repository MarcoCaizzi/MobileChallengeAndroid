package com.example.mobilechallegeandroid.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader

class CityRepositoryImpl(private val context: Context) : CityRepository {
    private val favorites = mutableSetOf<Long>()
    private var cities: List<City> = emptyList()

    override suspend fun fetchCities(): List<City> {
        if (cities.isEmpty()) {
            val inputStream = context.assets.open("cities.json")
            val reader = InputStreamReader(inputStream)
            val type = object : TypeToken<List<CityJson>>() {}.type
            val cityJsonList: List<CityJson> = Gson().fromJson(reader, type)
            reader.close()
            cities = cityJsonList.map {
                City(
                    id = it._id,
                    name = it.name,
                    country = it.country,
                    coord = Coord(it.coord.lon, it.coord.lat),
                    isFavorite = favorites.contains(it._id)
                )
            }
        }
        return cities
    }

    override suspend fun getFavorites(): List<City> {
        return cities.filter { favorites.contains(it.id) }
    }

    override suspend fun toggleFavorite(cityId: Long) {
        if (favorites.contains(cityId)) {
            favorites.remove(cityId)
        } else {
            favorites.add(cityId)
        }
    }

    // Clase auxiliar para parsear el JSON
    private data class CityJson(
        val _id: Long,
        val name: String,
        val country: String,
        val coord: CoordJson
    )

    private data class CoordJson(
        val lon: Double,
        val lat: Double
    )
}