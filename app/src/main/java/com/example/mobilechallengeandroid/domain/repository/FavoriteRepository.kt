package com.example.mobilechallengeandroid.domain.repository

interface FavoriteRepository {
    suspend fun saveFavorite(cityId: Long)
    suspend fun getFavoriteIds(): Set<Long>
}