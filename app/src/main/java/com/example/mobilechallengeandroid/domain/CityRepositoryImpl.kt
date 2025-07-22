package com.example.mobilechallengeandroid.domain

import android.content.Context
import androidx.core.content.edit
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.mobilechallengeandroid.R
import com.example.mobilechallengeandroid.data.model.WeatherData
import com.example.mobilechallengeandroid.data.local.CityDao
import com.example.mobilechallengeandroid.data.local.CityEntity
import com.example.mobilechallengeandroid.data.model.City
import com.example.mobilechallengeandroid.data.model.Coord
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import javax.inject.Inject

class CityRepositoryImpl @Inject constructor(
    private val context: Context, private val cityDao: CityDao
) : CityRepository {
    private val PREFS_NAME = "city_prefs"
    private val FAVORITES_KEY = "favorite_ids"

    private val prefs get() = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)


    companion object {
        const val CITIES_JSON_URL =
            "https://gist.githubusercontent.com/hernan-uala/dce8843a8edbe0b0018b32e137bc2b3a/raw/0996accf70cb0ca0e16f9a99e0ee185fafca7af1/cities.json"
    }

    override suspend fun getFavoriteIds(): Set<Long> {
        return prefs.getStringSet(FAVORITES_KEY, emptySet())?.mapNotNull { it.toLongOrNull() }
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

            try {
                if (!file.exists()) {
                    val client = OkHttpClient()
                    val request = Request.Builder().url(jsonUrl).build()
                    val response = client.newCall(request).execute()

                    if (!response.isSuccessful) {
                        throw Exception("Error downloading cities: ${response.code}")
                    }

                    val body = response.body.string()
                    file.writeText(body)
                }

                val reader = InputStreamReader(FileInputStream(file))
                val type = object : TypeToken<List<CityJson>>() {}.type
                val cityJsonList: List<CityJson> = Gson().fromJson(reader, type)
                reader.close()

                val cityEntities = cityJsonList.map {
                    CityEntity(
                        id = it._id,
                        name = it.name,
                        country = it.country,
                        coord = Coord(it.coord.lon, it.coord.lat)
                    )
                }
                cityDao.insertAll(cityEntities)
                cityEntities.map { City(it.id, it.name, it.country, it.coord) }
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }

    override suspend fun searchCitiesByPrefix(prefix: String): List<City> =
        withContext(Dispatchers.IO) {
            cityDao.searchByName(prefix).map { City(it.id, it.name, it.country, it.coord) }
        }

    override suspend fun getCitiesByIds(ids: List<Long>): List<City> {
        return cityDao.getCitiesByIds(ids).map { City(it.id, it.name, it.country, it.coord) }
    }

    override suspend fun getCityById(id: Long): City? {
        return cityDao.getCityById(id)?.let { City(it.id, it.name, it.country, it.coord) }
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
                val json = JSONObject(it)
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

    override fun getPagedCities(prefix: String): Flow<PagingData<City>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20, enablePlaceholders = false
            ),
            pagingSourceFactory = { cityDao.getCitiesPagingSource(prefix) }
        ).flow.map { pagingData ->
            pagingData.map { entity ->
                City(entity.id, entity.name, entity.country, entity.coord)
            }
        }
    }


    private data class CityJson(
        val _id: Long, val name: String, val country: String, val coord: CoordJson
    )

    private data class CoordJson(
        val lon: Double, val lat: Double
    )
}