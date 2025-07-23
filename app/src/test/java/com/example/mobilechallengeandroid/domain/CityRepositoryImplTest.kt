package com.example.mobilechallengeandroid.domain

import android.content.Context
import android.content.SharedPreferences
import com.example.mobilechallengeandroid.data.local.CityDao
import com.example.mobilechallengeandroid.data.remote.file.CityFileApi
import com.example.mobilechallengeandroid.data.remote.weather.WeatherApi
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*
import java.io.File
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.testing.asSnapshot
import com.example.mobilechallengeandroid.data.local.CityEntity
import com.example.mobilechallengeandroid.data.model.City
import com.example.mobilechallengeandroid.data.model.Coord


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

        var favoriteIds: Set<String> = emptySet()

        val editor = mock<SharedPreferences.Editor> {
            on { putStringSet(any(), any()) } doAnswer { invocation ->
                favoriteIds = invocation.getArgument<Set<String>>(1)
                it
            }
            on { apply() } doAnswer { }
            on { commit() } doReturn true
        }

        val sharedPrefs = mock<SharedPreferences> {
            on { getStringSet(any(), any()) } doAnswer { invocation ->
                favoriteIds.ifEmpty { invocation.getArgument(1) }
            }
            on { edit() } doReturn editor
        }

        whenever(context.filesDir).thenReturn(File("build/tmp"))
        whenever(context.getSharedPreferences(any(), any())).thenReturn(sharedPrefs)

        repository = CityRepositoryImpl(context, cityDao, weatherApi, cityFileApi)
    }

    /**
     * Verifies that when a network error occurs during file download,
     * the method returns an empty list.
     */
    @Test
    fun downloadAndFetchCities_returns_empty_list_on_network_error() = runBlocking {
        whenever(cityFileApi.downloadFile(any())).thenThrow(RuntimeException("Network error"))
        val result = repository.downloadAndFetchCities()
        assertTrue(result.isEmpty())
    }

    /**
     * Verifies that when the file already exists and contains valid city data,
     * the method reads and returns the list of cities.
     */
    @Test
    fun downloadAndFetchCities_reads_existing_and_returns_cities(): Unit = runBlocking {
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
    fun downloadAndFetchCities_returns_empty_list_if_file_is_corrupted(): Unit = runBlocking {
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
    fun downloadAndFetchCities_downloads_parses_and_returns_cities(): Unit = runBlocking {
        val fileDir = context.filesDir
        val file = File(fileDir, "cities.json")
        if (file.exists()) file.delete()

        val json =
            "[{\"_id\":1,\"name\":\"Alabama\",\"country\":\"US\",\"coord\":{\"lon\":0.0,\"lat\":0.0}}]"
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

    /**
     * Ensures that downloadAndFetchCities returns an empty list when the file download fails.
     */
    @Test
    fun downloadAndFetchCities_returns_empty_list_when_downloadFile_returns_null() = runBlocking {
        whenever(cityFileApi.downloadFile(any())).thenReturn(null)
        val result = repository.downloadAndFetchCities()
        assertTrue(result.isEmpty())
    }

    /**
     * Ensures that getWeatherForCity returns weather data for a given city.
     */
    @Test
    fun getPagedCities_emits_paged_city_list() = runBlocking {
        val prefix = "A"
        val cityEntities = listOf(
            CityEntity(1L, "Alabama", "US", Coord(0.0, 0.0)),
            CityEntity(2L, "Albuquerque", "US", Coord(0.0, 0.0))
        )
        val pagingSource = object : PagingSource<Int, CityEntity>() {
            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CityEntity> {
                return LoadResult.Page(
                    data = cityEntities,
                    prevKey = null,
                    nextKey = null
                )
            }

            override fun getRefreshKey(state: PagingState<Int, CityEntity>): Int? = null
        }
        whenever(cityDao.getCitiesPagingSource(prefix)).thenReturn(pagingSource)

        val flow = repository.getPagedCities(prefix)
        val snapshot: List<City> = flow.asSnapshot()
        assertEquals(2, snapshot.size)
        assertEquals("Alabama", snapshot[0].name)
        assertEquals("Albuquerque", snapshot[1].name)
    }

    /**
     * Ensures that toggleFavorite updates the favorite status of a city.
     */
    @Test
    fun getCitiesByIds_returns_correct_cities() = runBlocking {
        val ids = listOf(1L, 2L)
        val entities = listOf(
            CityEntity(1L, "Alabama", "US", Coord(0.0, 0.0)),
            CityEntity(2L, "Albuquerque", "US", Coord(0.0, 0.0))
        )
        whenever(cityDao.getCitiesByIds(ids)).thenReturn(entities)

        val result = repository.getCitiesByIds(ids)
        assertEquals(2, result.size)
        assertEquals("Alabama", result[0].name)
        assertEquals("Albuquerque", result[1].name)
    }

    /**
     * Ensures that getCityById returns the correct city entity.
     */
    @Test
    fun getCityById_returns_correct_city() = runBlocking {
        val entity = CityEntity(1L, "Alabama", "US", Coord(0.0, 0.0))
        whenever(cityDao.getCityById(1L)).thenReturn(entity)

        val result = repository.getCityById(1L)
        assertNotNull(result)
        assertEquals("Alabama", result?.name)
    }

    /**
     * Ensures that getCityById returns null if the city is not found.
     */
    @Test
    fun getCityById_returns_null_if_not_found() = runBlocking {
        whenever(cityDao.getCityById(99L)).thenReturn(null)

        val result = repository.getCityById(99L)
        assertNull(result)
    }

    /**
     * Ensures that toggleFavorite adds a city to favorites if not already present,
     * and removes it if it is already a favorite.
     */
    @Test
    fun saveFavoriteIds_persists_favorites_correctly() = runBlocking {
        val cityId1 = 1L
        val cityId2 = 2L
        repository.toggleFavorite(cityId1)
        repository.toggleFavorite(cityId2)
        val favorites = repository.getFavoriteIds()
        assertTrue(favorites.containsAll(listOf(cityId1, cityId2)))
    }

    /**
     * Ensures that getFavoriteIds returns the correct set of favorite city IDs.
     */
    @Test
    fun isValidJsonArray_returns_true_for_valid_json_array() {
        val file = File(context.filesDir, "valid.json")
        file.writeText("""[{"_id":1,"name":"A","country":"B","coord":{"lon":0.0,"lat":0.0}}]""")
        val method =
            CityRepositoryImpl::class.java.getDeclaredMethod("isValidJsonArray", File::class.java)
        method.isAccessible = true
        val result = method.invoke(repository, file) as Boolean
        assertTrue(result)
        file.delete()
    }

    /**
     * Ensures that isValidJsonArray returns false for an empty file.
     */
    @Test
    fun isValidJsonArray_returns_false_for_empty_file() {
        val file = File(context.filesDir, "empty.json")
        file.writeText("")
        val method =
            CityRepositoryImpl::class.java.getDeclaredMethod("isValidJsonArray", File::class.java)
        method.isAccessible = true
        val result = method.invoke(repository, file) as Boolean
        assertFalse(result)
        file.delete()
    }

    /**
     * Ensures that isValidJsonArray returns false for a file that is not a valid JSON array.
     */
    @Test
    fun isValidJsonArray_returns_false_for_corrupted_file() {
        val file = File(context.filesDir, "corrupt.json")
        file.writeText("not a json")
        val method =
            CityRepositoryImpl::class.java.getDeclaredMethod("isValidJsonArray", File::class.java)
        method.isAccessible = true
        val result = method.invoke(repository, file) as Boolean
        assertFalse(result)
        file.delete()
    }

    /**
     * Ensures that isValidJsonArray returns false for a JSON object instead of an array.
     */
    @Test
    fun isValidJsonArray_returns_false_for_json_object_instead_of_array() {
        val file = File(context.filesDir, "object.json")
        file.writeText("""{"_id":1,"name":"A"}""")
        val method =
            CityRepositoryImpl::class.java.getDeclaredMethod("isValidJsonArray", File::class.java)
        method.isAccessible = true
        val result = method.invoke(repository, file) as Boolean
        assertFalse(result)
        file.delete()
    }
}