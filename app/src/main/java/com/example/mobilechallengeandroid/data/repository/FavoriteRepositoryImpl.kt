package com.example.mobilechallengeandroid.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.mobilechallengeandroid.domain.repository.FavoriteRepository
import javax.inject.Inject
import androidx.core.content.edit

class FavoriteRepositoryImpl @Inject constructor(
    private val context: Context,
) : FavoriteRepository {
    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences("favorites", Context.MODE_PRIVATE)
    }

    override suspend fun saveFavorite(cityId: Long) {
        val current = getFavoriteIds().toMutableSet()
        if (current.contains(cityId)) {
            current.remove(cityId)
        } else {
            current.add(cityId)
        }
        prefs.edit { putStringSet("favorite_ids", current.map { it.toString() }.toSet()) }
    }

    override suspend fun getFavoriteIds(): Set<Long> {
        return prefs.getStringSet("favorite_ids", emptySet())
            ?.mapNotNull { it.toLongOrNull() }
            ?.toSet() ?: emptySet()
    }
}
