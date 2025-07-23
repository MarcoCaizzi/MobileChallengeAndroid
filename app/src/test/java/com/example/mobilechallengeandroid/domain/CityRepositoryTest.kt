package com.example.mobilechallengeandroid.domain

import android.content.Context
import androidx.paging.PagingData
import com.example.mobilechallengeandroid.data.local.CityDao
import com.example.mobilechallengeandroid.data.model.City
import com.example.mobilechallengeandroid.data.model.Coord
import com.example.mobilechallengeandroid.data.model.WeatherData
import com.example.mobilechallengeandroid.data.remote.file.CityFileApi
import com.example.mobilechallengeandroid.data.remote.weather.*
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*

/**
 * Unit tests for [CityRepository] implementations.
 * Tests both favorite logic (with [FakeCityRepository]) and weather fetching (with [CityRepositoryImpl] and mocks).
 */
class CityRepositoryTest {
    private lateinit var repository: CityRepository
    private lateinit var weatherApi: WeatherApi

    /**
     * Sets up a fake repository and a mock WeatherApi before each test.
     */
    @Before
    fun setup() {
        repository = FakeCityRepository()
        weatherApi = mock()
    }

    /**
     * Verifies that [toggleFavorite] adds and removes a city from favorites.
     */
    @Test
    fun `toggleFavorite adds and removes favorites`() = runBlocking {
        val cityId = 1234L
        assertFalse(repository.getFavoriteIds().contains(cityId))
        repository.toggleFavorite(cityId)
        assertTrue(repository.getFavoriteIds().contains(cityId))
        repository.toggleFavorite(cityId)
        assertFalse(repository.getFavoriteIds().contains(cityId))
    }

    /**
     * Verifies that [getFavoriteIds] returns all current favorites.
     */
    @Test
    fun `getFavoriteIds returns all current favorites`() = runBlocking {
        val ids = listOf(1L, 2L, 3L)
        ids.forEach { repository.toggleFavorite(it) }
        assertEquals(ids.toSet(), repository.getFavoriteIds())
    }

    /**
     * Verifies that toggling the same city multiple times works as expected.
     */
    @Test
    fun `toggleFavorite toggling same city multiple times`() = runBlocking {
        val cityId = 42L
        repository.toggleFavorite(cityId) // add
        repository.toggleFavorite(cityId) // remove
        repository.toggleFavorite(cityId) // add again
        assertTrue(repository.getFavoriteIds().contains(cityId))
        repository.toggleFavorite(cityId) // remove again
        assertFalse(repository.getFavoriteIds().contains(cityId))
    }

    /**
     * Verifies that removing one favorite does not affect others.
     */
    @Test
    fun `toggleFavorite removes only the specified city`() = runBlocking {
        val ids = listOf(10L, 20L, 30L)
        ids.forEach { repository.toggleFavorite(it) }
        repository.toggleFavorite(20L) // remove 20L
        assertEquals(setOf(10L, 30L), repository.getFavoriteIds())
    }

    /**
     * Verifies that [getWeatherForCity] returns weather data on success.
     */
    @Test
    fun `getWeatherForCity returns weather data on success`() = runBlocking {
        val context = mock<Context> {
            on { getString(any()) } doReturn "fake_api_key"
        }
        val cityDao = mock<CityDao>()
        val cityFileApi = mock<CityFileApi>()
        val cityRepository = CityRepositoryImpl(context, cityDao, weatherApi, cityFileApi)

        val city = City(1L, "TestCity", "TC", Coord(1.0, 2.0))
        val weatherDto = WeatherDto(
            weatherCondition = WeatherConditionDto(DescriptionDto("Sunny")),
            temperature = TemperatureDto(75.0, "F"),
            feelsLikeTemperature = TemperatureDto(74.0, "F"),
            relativeHumidity = 50,
            precipitation = PrecipitationDto(ProbabilityDto(10))
        )
        whenever(weatherApi.getWeather(any(), any(), any(), any())).thenReturn(weatherDto)

        val result = cityRepository.getWeatherForCity(city)
        assertNotNull(result)
        assertEquals("Sunny", result?.description)
        assertEquals(75.0, result?.temperature ?: 0.0, 0.01)
        assertEquals(50, result?.humidity)
        assertEquals(10, result?.rainProbability)
    }

    /**
     * Verifies that [getWeatherForCity] returns null if an exception occurs.
     */
    @Test
    fun `getWeatherForCity returns null on exception`() = runBlocking {
        val context = mock<Context> {
            on { getString(any()) } doReturn "fake_api_key"
        }
        val cityDao = mock<CityDao>()
        val cityFileApi = mock<CityFileApi>()
        val cityRepository = CityRepositoryImpl(context, cityDao, weatherApi, cityFileApi)

        val city = City(1L, "TestCity", "TC", Coord(1.0, 2.0))
        whenever(weatherApi.getWeather(any(), any(), any(), any())).thenThrow(RuntimeException("API error"))

        val result = cityRepository.getWeatherForCity(city)
        assertNull(result)
    }

    /**
     * Verifies that [searchCitiesByPrefix] returns cities matching the prefix (case-insensitive).
     */
    @Test
    fun `searchCitiesByPrefix returns matching cities`() = runBlocking {
        repository = FakeCityRepository()
        val result = repository.searchCitiesByPrefix("Al")
        assertFalse(result.isEmpty())
        val names = result.map { it.name }
        assertTrue(names.contains("Alabama"))
    }

    /**
     * Verifies that [searchCitiesByPrefix] returns an empty list if no city matches the prefix.
     */
    @Test
    fun `searchCitiesByPrefix returns empty list if no match`() = runBlocking {
        repository = FakeCityRepository()
        val result = repository.searchCitiesByPrefix("Xy")
        assertTrue(result.isEmpty())
    }

    /**
     * Verifies that [searchCitiesByPrefix] returns all cities if prefix is empty.
     */
    @Test
    fun `searchCitiesByPrefix returns all cities if prefix is empty`() = runBlocking {
        repository = FakeCityRepository()
        val result = repository.searchCitiesByPrefix("")
        assertEquals(5, result.size)
    }

    /**
     * Verifies that [searchCitiesByPrefix] returns cities starting with the given prefix,
     */
    @Test
    fun `searchCitiesByPrefix with prefix A returns all except Sydney`() = runBlocking {
        repository = FakeCityRepository()
        val result = repository.searchCitiesByPrefix("A")
        val names = result.map { it.name }
        assertEquals(setOf("Alabama", "Albuquerque", "Anaheim", "Arizona"), names.toSet())
    }

    @Test
    fun `searchCitiesByPrefix with prefix s returns only Sydney`() = runBlocking {
        repository = FakeCityRepository()
        val result = repository.searchCitiesByPrefix("s")
        assertEquals(1, result.size)
        assertEquals("Sydney", result[0].name)
    }

    @Test
    fun `searchCitiesByPrefix with prefix Al returns Alabama and Albuquerque`() = runBlocking {
        repository = FakeCityRepository()
        val result = repository.searchCitiesByPrefix("Al")
        val names = result.map { it.name }
        assertEquals(setOf("Alabama", "Albuquerque"), names.toSet())
    }

    @Test
    fun `searchCitiesByPrefix with prefix Alb returns only Albuquerque`() = runBlocking {
        repository = FakeCityRepository()
        val result = repository.searchCitiesByPrefix("Alb")
        assertEquals(1, result.size)
        assertEquals("Albuquerque", result[0].name)
    }
}

/**
 * Simple fake implementation of [CityRepository] for unit testing favorite logic.
 */
class FakeCityRepository : CityRepository {
    private val favorites = mutableSetOf<Long>()
    private val cities = listOf(
        City(1L, "Alabama", "US", Coord(0.0, 0.0)),
        City(2L, "Albuquerque", "US", Coord(0.0, 0.0)),
        City(3L, "Anaheim", "US", Coord(0.0, 0.0)),
        City(4L, "Arizona", "US", Coord(0.0, 0.0)),
        City(5L, "Sydney", "AU", Coord(0.0, 0.0))
    )

    override suspend fun toggleFavorite(cityId: Long) {
        if (!favorites.add(cityId)) favorites.remove(cityId)
    }
    override suspend fun getFavoriteIds(): Set<Long> = favorites.toSet()
    override suspend fun downloadAndFetchCities(): List<City> = emptyList()
    override suspend fun getWeatherForCity(city: City): WeatherData? = null
    override fun getPagedCities(prefix: String) = flowOf(PagingData.empty<City>())
    override suspend fun getCitiesByIds(ids: List<Long>): List<City> = emptyList()
    override suspend fun getCityById(id: Long): City? = null

    override suspend fun searchCitiesByPrefix(prefix: String): List<City> {
        return cities.filter { it.name.startsWith(prefix, ignoreCase = true) }
    }
}