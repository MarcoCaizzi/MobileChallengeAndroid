package com.example.mobilechallegeandroid.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader
import java.io.File
import java.io.FileInputStream
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CityRepositoryImpl(private val context: Context) : CityRepository {
    private val favorites = mutableSetOf<Long>()
    private var cities: List<City> = emptyList()

    companion object {
        const val CITIES_JSON_URL =
            "https://gist.githubusercontent.com/hernan-uala/dce8843a8edbe0b0018b32e137bc2b3a/raw/0996accf70cb0ca0e16f9a99e0ee185fafca7af1/cities.json"
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

    override suspend fun downloadAndFetchCities(jsonUrl: String): List<City> = withContext(Dispatchers.IO) {
        val fileName = "cities.json"
        val file = File(context.filesDir, fileName)

        if (!file.exists()) {
            val client = OkHttpClient()
            val request = Request.Builder().url(jsonUrl).build()
            val response = client.newCall(request).execute()
            val body = response.body.string()
            file.writeText(body)
        }

        val reader = InputStreamReader(FileInputStream(file))
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
        cities
    }

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