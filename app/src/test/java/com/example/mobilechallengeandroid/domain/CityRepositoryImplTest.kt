package com.example.mobilechallengeandroid.domain

import android.content.Context
import com.example.mobilechallengeandroid.data.local.CityDao
import com.example.mobilechallengeandroid.data.remote.file.CityFileApi
import com.example.mobilechallengeandroid.data.remote.weather.WeatherApi
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import org.junit.Assert.*
import org.junit.Before
import org.junit.After
import org.junit.Test
import org.mockito.kotlin.*
import java.io.File

class CityRepositoryImplTest {
    private lateinit var repository: CityRepositoryImpl
    private lateinit var context: Context
    private lateinit var cityDao: CityDao
    private lateinit var weatherApi: WeatherApi
    private lateinit var cityFileApi: CityFileApi

    @Before
    fun setup() {
        context = mock()
        cityDao = mock()
        weatherApi = mock()
        cityFileApi = mock()
        whenever(context.filesDir).thenReturn(File("build/tmp"))
        repository = CityRepositoryImpl(context, cityDao, weatherApi, cityFileApi)
    }

    @After
    fun cleanup() {
        val file = File(context.filesDir, "cities.json")
        if (file.exists()) file.delete()
    }

    /**
     * Verifies that when a network error occurs during file download,
     * the method returns an empty list.
     */
    @Test
    fun `downloadAndFetchCities returns empty list on network error`() = runBlocking {
        whenever(cityFileApi.downloadFile(any())).thenThrow(RuntimeException("Network error"))
        val result = repository.downloadAndFetchCities()
        assertTrue(result.isEmpty())
    }

    /**
     * Verifies that when the file already exists and contains valid city data,
     * the method reads and returns the list of cities.
     */
    @Test
    fun `downloadAndFetchCities reads existing file and returns cities`():Unit = runBlocking {
        // Prepare a valid JSON file
        val fileDir = context.filesDir
        val file = File(fileDir, "cities.json")
        val json = """[{"_id":1,"name":"TestCity","country":"TC","coord":{"lon":1.0,"lat":2.0}}]"""
        file.writeText(json)

        whenever(cityDao.insertAll(any())).thenReturn(Unit)

        val result = repository.downloadAndFetchCities()
        assertEquals(1, result.size)
        assertEquals("TestCity", result[0].name)

        file.delete()
    }

    /**
     * Verifies that if the file exists but is corrupted or invalid,
     * the method returns an empty list.
     */
    @Test
    fun `downloadAndFetchCities returns empty list if file is corrupted`():Unit = runBlocking {
        val fileDir = context.filesDir
        val file = File(fileDir, "cities.json")
        file.writeText("corrupted content")

        val result = repository.downloadAndFetchCities()
        assertTrue(result.isEmpty())

        file.delete()
    }

    /**
     * Verifies that when the file does not exist, it is downloaded and parsed,
     * and the cities are inserted into the database and returned.
     */
    @Test
    fun `downloadAndFetchCities downloads, parses and returns cities`():Unit = runBlocking {
        val fileDir = context.filesDir
        val file = File(fileDir, "cities.json")
        if (file.exists()) file.delete()

        val json = "[{\"_id\":1,\"name\":\"Alabama\",\"country\":\"US\",\"coord\":{\"lon\":0.0,\"lat\":0.0}}]"
        val responseBody = mock<ResponseBody> {
            on { bytes() } doReturn json.toByteArray()
        }
        whenever(cityFileApi.downloadFile(any())).thenReturn(responseBody)
        whenever(cityDao.insertAll(any())).thenReturn(Unit)

        val result = repository.downloadAndFetchCities()
        assertEquals(1, result.size)
        assertEquals("Alabama", result[0].name)

        file.delete()
    }
}