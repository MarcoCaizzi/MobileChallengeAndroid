package com.example.mobilechallengeandroid.data.model
import com.google.gson.annotations.SerializedName
data class WeatherModel(
    @SerializedName("weatherCondition")
    val weatherCondition: WeatherCondition?,
    @SerializedName("temperature")
    val temperature: Temperature?,
    @SerializedName("feelsLikeTemperature")
    val feelsLikeTemperature: Temperature?,
    @SerializedName("relativeHumidity")
    val relativeHumidity: Int?,
    @SerializedName("precipitation")
    val precipitation: Precipitation?
)

data class WeatherCondition(
    @SerializedName("description")
    val description: Description?
)

data class Description(
    @SerializedName("text")
    val text: String?,
    @SerializedName("languageCode")
    val languageCode: String?
)

data class Temperature(
    @SerializedName("degrees")
    val degrees: Double?,
    @SerializedName("unit")
    val unit: String?
)

data class Precipitation(
    @SerializedName("probability")
    val probability: Probability?
)

data class Probability(
    @SerializedName("percent")
    val percent: Int?,
    @SerializedName("type")
    val type: String?
)