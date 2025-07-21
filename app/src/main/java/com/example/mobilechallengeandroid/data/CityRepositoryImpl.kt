package com.example.mobilechallengeandroid.data

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
import androidx.core.content.edit
import com.example.mobilechallengeandroid.R

class CityRepositoryImpl(private val context: Context) : CityRepository {
    private val PREFS_NAME = "city_prefs"
    private val FAVORITES_KEY = "favorite_ids"

    private val prefs get() = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private var cities: List<City> = emptyList()

    companion object {
        const val CITIES_JSON_URL =
            "https://gist.githubusercontent.com/hernan-uala/dce8843a8edbe0b0018b32e137bc2b3a/raw/0996accf70cb0ca0e16f9a99e0ee185fafca7af1/cities.json"
    }

    override suspend fun getFavoriteIds(): Set<Long> {
        return prefs.getStringSet(FAVORITES_KEY, emptySet())
            ?.mapNotNull { it.toLongOrNull() }
            ?.toSet() ?: emptySet()
    }

    private fun saveFavoriteIds(ids: Set<Long>) {
        prefs.edit { putStringSet(FAVORITES_KEY, ids.map { it.toString() }.toSet()) }
    }

    override suspend fun toggleFavorite(cityId: Long) {
        val ids = getFavoriteIds().toMutableSet()
        if (ids.contains(cityId)) ids.remove(cityId) else ids.add(cityId)
        saveFavoriteIds(ids)
    }

    override suspend fun downloadAndFetchCities(jsonUrl: String): List<City> =
        withContext(Dispatchers.IO) {
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
                )
            }.sortedWith(compareBy({ it.name.lowercase() }, { it.country.lowercase() }))
            cities
        }

    override suspend fun getWeatherForCity(city: City): WeatherData? = withContext(Dispatchers.IO) {
        val apiKey = context.getString(R.string.weather_api_key)
        val url =
            "https://weather.googleapis.com/v1/currentConditions:lookup?key=$apiKey&location.latitude=${city.coord.lat}&location.longitude=${city.coord.lon}&unitsSystem=IMPERIAL"
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()
        val response = try {
            client.newCall(request).execute()
        } catch (_: Exception) {
            null
        }
        if (response != null && response.isSuccessful) {
            val body = response.body.string()
            body.let {
                val json = org.json.JSONObject(it)
                WeatherData(
                    description = json.optJSONObject("weatherCondition")
                        ?.optJSONObject("description")?.optString("text"),
                    temperature = json.optJSONObject("temperature")?.optDouble("degrees"),
                    temperatureUnit = json.optJSONObject("temperature")?.optString("unit"),
                    feelsLike = json.optJSONObject("feelsLikeTemperature")?.optDouble("degrees"),
                    humidity = json.optInt("relativeHumidity"),
                    rainProbability = json.optJSONObject("precipitation")
                        ?.optJSONObject("probability")?.optInt("percent")
                )
            }
        } else {
            null
        }
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