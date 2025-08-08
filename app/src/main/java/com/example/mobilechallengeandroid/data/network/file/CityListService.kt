package com.example.mobilechallengeandroid.data.network.file
import com.example.mobilechallengeandroid.data.model.CityModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CityListService @Inject constructor(
    private val api: CityListApiClient
) {
    suspend fun getAllCities(): List<CityModel> {
        return withContext(Dispatchers.IO) {
            val response = api.getAllCities()
            android.util.Log.d("CityListService", "API response code: ${response.code()} and message: ${response.message()}")
            if (response.isSuccessful) {
                val cities = response.body() ?: emptyList()
                android.util.Log.d("CityListService", "Received ${cities.size} cities from API.")
                cities
            } else {
                android.util.Log.e("CityListService", "API error: ${response.errorBody()?.string()}" )
                emptyList()
            }
        }
    }
}
