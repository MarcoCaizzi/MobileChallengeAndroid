package com.example.mobilechallengeandroid.ui.cityList.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview(showBackground = true)
@Composable
fun FilterCityContent(
    filter: String = "",
    showOnlyFavorites: Boolean = false,
    onFilterChange: (String) -> Unit = {},
    onShowOnlyFavoritesChange: (Boolean) -> Unit = {}
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        OutlinedTextField(
            value = filter,
            onValueChange = { newFilter ->
                onFilterChange(newFilter)
            },
            label = { Text("filter") },
            modifier = Modifier.weight(1f),
            shape = MaterialTheme.shapes.large,
            leadingIcon = {
                Icon(Icons.Filled.Search, contentDescription = "filter_icon")
            }
        )
        Spacer(modifier = Modifier.width(8.dp))
        Checkbox(
            checked = showOnlyFavorites,
            onCheckedChange = { isChecked ->
                onShowOnlyFavoritesChange(isChecked)
            },
        )
        Text("Favorites only")
    }
}
