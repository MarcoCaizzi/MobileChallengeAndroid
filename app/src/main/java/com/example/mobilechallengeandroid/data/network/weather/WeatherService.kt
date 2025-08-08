package com.example.mobilechallengeandroid.data.network.weather
import com.example.mobilechallengeandroid.data.model.WeatherModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WeatherService @Inject constructor(private val api: WeatherApiClient){
    suspend fun getWeatherByCity(
        apiKey: String,
        lat: Double,
        lon: Double,
        units: String = "IMPERIAL"
    ): WeatherModel? {
        return withContext(Dispatchers.IO) {
            val url = "https://weather.googleapis.com/v1/currentConditions:lookup?key=$apiKey&location.latitude=$lat&location.longitude=$lon&unitsSystem=$units"
            android.util.Log.d("WeatherService", "Request URL: $url")
            android.util.Log.d("WeatherService", "Requesting weather for lat=$lat, lon=$lon, apiKey=$apiKey")
            val response = api.getWeatherByCity(apiKey, lat, lon, units)
            android.util.Log.d("WeatherService", "API response code: ${response.code()} and message: ${response.message()}")
            if (response.isSuccessful) {
                val weather = response.body()
                android.util.Log.d("WeatherService", "Received weather: $weather")
                weather
            } else {
                android.util.Log.e("WeatherService", "API error: ${response.errorBody()?.string()}" )
                null
            }
        }
    }
}