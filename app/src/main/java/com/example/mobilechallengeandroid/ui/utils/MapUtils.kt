package com.example.mobilechallengeandroid.ui.utils

import android.content.Context
import com.example.mobilechallengeandroid.R
import com.example.mobilechallengeandroid.domain.model.CityItem

fun getStaticMapUrl(
    context: Context,
    cityItem: CityItem,
    width: Int = 600,
    height: Int = 300,
    zoom: Int = 12
): String {
    val apiKey = context.getString(R.string.google_maps_key)
    return "https://maps.googleapis.com/maps/api/staticmap?center=${cityItem.coordinates.lat},${cityItem.coordinates.lon}&zoom=$zoom&size=${width}x$height&markers=${cityItem.coordinates.lat},${cityItem.coordinates.lon}&key=$apiKey"
}