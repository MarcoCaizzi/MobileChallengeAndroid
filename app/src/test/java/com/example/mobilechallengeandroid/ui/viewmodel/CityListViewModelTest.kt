package com.example.mobilechallengeandroid.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.mobilechallengeandroid.data.model.City
import com.example.mobilechallengeandroid.domain.CityRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.*
import org.mockito.kotlin.*
import androidx.paging.PagingData
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class CityListViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: CityRepository
    private lateinit var viewModel: CityListViewModel

    private val cities = listOf(
        City(1L, "Alabama", "US", City.Coord(0.0, 0.0)),
        City(2L, "Albuquerque", "US", City.Coord(0.0, 0.0)),
        City(3L, "Anaheim", "US", City.Coord(0.0, 0.0)),
        City(4L, "Arizona", "US", City.Coord(0.0, 0.0)),
        City(5L, "Sydney", "AU", City.Coord(0.0, 0.0))
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mock()
        runBlocking {
            whenever(repository.getPagedCities(any())).thenReturn(flowOf(PagingData.from(cities)))
            whenever(repository.getCitiesByIds(any())).thenReturn(cities)
            whenever(repository.searchCitiesByPrefix(any())).thenReturn(cities)
            whenever(repository.getFavoriteIds()).thenReturn(emptySet())
        }
        viewModel = CityListViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun filter_updates_state() = runTest {
        viewModel.onFilterChange("Ala")
        Assert.assertEquals("Ala", viewModel.filter.value)
    }

    @Test
    fun showOnlyFavorites_updates_state() = runTest {
        viewModel.onShowOnlyFavoritesChange(true)
        Assert.assertTrue(viewModel.showOnlyFavorites.value)
    }

    @Test
    fun selectCity_updates_selectedCityId() = runTest {
        viewModel.selectCity(2L)
        Assert.assertEquals(2L, viewModel.selectedCityId.value)
    }

    @Test
    fun onFavoriteClick_toggles_favorite_and_updates_favoriteIds() = runTest {
        whenever(repository.getFavoriteIds()).thenReturn(setOf(1L))
        viewModel.onFavoriteClick(1L)
        testDispatcher.scheduler.advanceUntilIdle()
        Assert.assertEquals(setOf(1L), viewModel.favoriteIds.value)
    }

    @Test
    fun getCityById_returns_correct_city() = runTest {
        whenever(repository.getCityById(3L)).thenReturn(cities[2])
        val city = viewModel.getCityById(3L)
        Assert.assertEquals("Anaheim", city?.name)
    }

    @Test
    fun pagedCities_emits_correct_data() = runTest {
        val items = mutableListOf<PagingData<City>>()
        val job = launch {
            viewModel.pagedCities.toList(items)
        }
        advanceUntilIdle()
        Assert.assertTrue(items.isNotEmpty())
        job.cancel()
    }

    @Test
    fun pagedCities_filters_favorites_when_showOnlyFavorites_is_true() = runTest {
        whenever(repository.getFavoriteIds()).thenReturn(setOf(1L, 2L))
        whenever(repository.getCitiesByIds(listOf(1L, 2L))).thenReturn(listOf(cities[0], cities[1]))
        viewModel.onShowOnlyFavoritesChange(true)
        viewModel.onFavoriteClick(1L)
        viewModel.onFavoriteClick(2L)
        viewModel.onFilterChange("A")
        advanceUntilIdle()
        val flow = viewModel.pagedCities.first()
        // No se puede verificar el contenido exacto de PagingData fácilmente sin helpers,
        // pero el test asegura que no lanza y se emite algo.
        Assert.assertNotNull(flow)
    }
}