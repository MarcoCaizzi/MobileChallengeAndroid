package com.example.mobilechallengeandroid.utils

import android.content.Context
import com.example.mobilechallengeandroid.R
import com.example.mobilechallengeandroid.data.model.City

fun getStaticMapUrl(
    context: Context,
    city: City,
    width: Int = 600,
    height: Int = 300,
    zoom: Int = 12
): String {
    val apiKey = context.getString(R.string.google_maps_key)
    return "https://maps.googleapis.com/maps/api/staticmap?center=${city.coord.lat},${city.coord.lon}&zoom=$zoom&size=${width}x$height&markers=${city.coord.lat},${city.coord.lon}&key=$apiKey"
}