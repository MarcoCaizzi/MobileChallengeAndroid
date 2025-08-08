package com.example.mobilechallengeandroid.domain.usecase

import androidx.paging.PagingData
import com.example.mobilechallengeandroid.domain.model.CityItem
import com.example.mobilechallengeandroid.domain.repository.CityListRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPagedCitiesUseCase @Inject constructor(
    private val repository: CityListRepository
) {
    suspend operator fun invoke(
        filter: String,
        favoriteIds: Set<Long>,
        showOnlyFavorites: Boolean
    ): Flow<PagingData<CityItem>> {
        return if (showOnlyFavorites && favoriteIds.isNotEmpty()) {
            repository.getPagedFavoriteCitiesFromDatabase(filter, favoriteIds)
        } else {
            repository.getPagedCitiesFromDatabase(filter)
        }
    }
}

