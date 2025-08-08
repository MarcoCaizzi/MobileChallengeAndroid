package com.example.mobilechallengeandroid.domain.usecase

import com.example.mobilechallengeandroid.domain.repository.FavoriteRepository
import javax.inject.Inject

class FavoriteUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) {
    suspend fun toggleFavorite(cityId: Long) {
        favoriteRepository.saveFavorite(cityId)
    }

    suspend fun getFavoriteIds(): Set<Long> {
        return favoriteRepository.getFavoriteIds()
    }
}

