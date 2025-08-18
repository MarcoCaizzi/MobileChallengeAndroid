package com.example.mobilechallengeandroid.ui.component.cityDetail.map

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Preview(showBackground = true)
@Composable
fun PreviewCityDetailMapContent() {
    CityDetailMapContent(mapUrl = "https://maps.googleapis.com/maps/api/staticmap?center=-34.6037,-58.3816&zoom=10&size=600x300")
}