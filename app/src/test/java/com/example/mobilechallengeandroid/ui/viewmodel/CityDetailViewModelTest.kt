package com.example.mobilechallengeandroid.ui.viewmodel

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.mobilechallengeandroid.data.model.City
import com.example.mobilechallengeandroid.data.model.WeatherData
import com.example.mobilechallengeandroid.domain.CityRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.*
import org.mockito.kotlin.*
import kotlinx.coroutines.test.advanceTimeBy

@OptIn(ExperimentalCoroutinesApi::class)
class CityDetailViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: CityRepository
    private lateinit var context: Context
    private lateinit var viewModel: CityDetailViewModel

    private val city = City(1L, "Alabama", "US", City.Coord(0.0, 0.0))
    private val weather = WeatherData(
        description = "Sunny",
        temperature = 75.0,
        feelsLike = 74.0,
        humidity = 50,
        rainProbability = 10,
        temperatureUnit = "°C"
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mock()
        context = mock()
        whenever(context.getString(any())).thenReturn("dummy")
        viewModel = CityDetailViewModel(context, repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }


    /**
     * Verifies that calling loadCityDetails exposes the correct weather data and loading state.
     */
    @Test
    fun loadCityDetails_exposes_correct_weather_data_and_loading_state() = runTest {
        whenever(repository.getWeatherForCity(city)).thenReturn(weather)
        viewModel.loadCityDetails(city)
        advanceUntilIdle()
        val state = viewModel.state.value
        Assert.assertEquals(weather, state.weather)
        Assert.assertEquals("°C", state.temperatureUnit)
        Assert.assertFalse(state.isLoading)
    }

    /**
     * Verifies that isLoading is true during loading and false after loading.
     */
    @Test
    fun isLoading_is_true_during_loading_and_false_after() = runTest {
        val fakeRepository = object : CityRepository by repository {
            override suspend fun getWeatherForCity(city: City): WeatherData? {
                kotlinx.coroutines.delay(100)
                return weather
            }
        }
        val viewModel = CityDetailViewModel(context, fakeRepository)
        val deferred = async { viewModel.loadCityDetails(city) }
        advanceTimeBy(50)
        Assert.assertTrue(viewModel.state.value.isLoading)
        advanceUntilIdle()
        Assert.assertFalse(viewModel.state.value.isLoading)
        deferred.await()
    }

    /**
     * Verifies that if the repository returns null, state.weather is null.
     */
    @Test
    fun state_weather_is_null_if_repository_returns_null() = runTest {
        whenever(repository.getWeatherForCity(city)).thenReturn(null)
        viewModel.loadCityDetails(city)
        advanceUntilIdle()
        Assert.assertNull(viewModel.state.value.weather)
    }

    /**
     * Verifies that mapUrl is set correctly after loading city details.
     */
    @Test
    fun loadCityDetails_sets_mapUrl() = runTest {
        whenever(repository.getWeatherForCity(city)).thenReturn(weather)
        viewModel.loadCityDetails(city)
        advanceUntilIdle()
        val state = viewModel.state.value
        Assert.assertNotNull(state.mapUrl)
    }

    /**
     * Verifies that temperature and feelsLike are converted to Celsius.
     */
    @Test
    fun loadCityDetails_converts_temperature_to_celsius() = runTest {
        val weatherF = weather.copy(temperature = 86.0, feelsLike = 80.6)
        whenever(repository.getWeatherForCity(city)).thenReturn(weatherF)
        viewModel.loadCityDetails(city)
        advanceUntilIdle()
        val state = viewModel.state.value
        Assert.assertEquals(30.0, state.temperatureCelsius!!, 0.1)
        Assert.assertEquals(27.0, state.feelsLikeCelsius!!, 0.1)
    }

    /**
     * Verifies that isLoading is set to false if an error occurs while loading city details.
     */
    @Test
    fun loadCityDetails_sets_isLoading_false_on_error() = runTest {
        whenever(repository.getWeatherForCity(city)).thenThrow(RuntimeException("Error"))
        viewModel.loadCityDetails(city)
        advanceUntilIdle()
        val state = viewModel.state.value
        Assert.assertFalse(state.isLoading)
        Assert.assertNull(state.weather)
    }
}