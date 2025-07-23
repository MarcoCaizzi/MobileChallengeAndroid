package com.example.mobilechallengeandroid.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.mobilechallengeandroid.data.model.City
import com.example.mobilechallengeandroid.data.model.Coord
import com.example.mobilechallengeandroid.domain.CityRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.*
import org.junit.*
import org.mockito.kotlin.*

@OptIn(ExperimentalCoroutinesApi::class)
class CityListViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: CityRepository
    private lateinit var viewModel: CityListViewModel

    private val cities = listOf(
        City(1L, "Alabama", "US", Coord(0.0, 0.0)),
        City(2L, "Sydney", "AU", Coord(0.0, 0.0))
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mock()
        runBlocking {
            whenever(repository.searchCitiesByPrefix(any())).thenReturn(cities)
            whenever(repository.getFavoriteIds()).thenReturn(emptySet())
            whenever(repository.getCitiesByIds(any())).thenReturn(cities)
        }
        whenever(repository.getPagedCities(any())).thenReturn(flowOf())

        viewModel = CityListViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /**
     * Verifies that the ViewModel initializes with the correct state.
     */
    @Test
    fun onFilterChange_updates_filter_state() = runTest {
        viewModel.onFilterChange("Al")
        Assert.assertEquals("Al", viewModel.filter.value)
    }

    /**
     * Verifies that the ViewModel initializes with the correct state.
     */
    @Test
    fun onShowOnlyFavoritesChange_updates_state() = runTest {
        viewModel.onShowOnlyFavoritesChange(true)
        Assert.assertTrue(viewModel.showOnlyFavorites.value)
    }

    /**
     * Verifies that the ViewModel initializes with the correct state.
     */
    @Test
    fun onFavoriteClick_updates_favoriteIds() = runTest {
        whenever(repository.getFavoriteIds()).thenReturn(setOf(1L))
        viewModel.onFavoriteClick(1L)
        advanceUntilIdle()
        Assert.assertEquals(setOf(1L), viewModel.favoriteIds.value)
    }

    /**
     * Verifies that the ViewModel initializes with the correct state.
     */
    @Test
    fun selectCity_updates_selectedCityId() {
        viewModel.selectCity(2L)
        Assert.assertEquals(2L, viewModel.selectedCityId.value)
    }
}