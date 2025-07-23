package com.example.mobilechallengeandroid.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mobilechallengeandroid.data.local.CityDao
import com.example.mobilechallengeandroid.data.local.CityDatabase
import com.example.mobilechallengeandroid.data.local.CityEntity
import com.example.mobilechallengeandroid.data.model.City
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class CityDaoTest {

    private lateinit var db: CityDatabase
    private lateinit var dao: CityDao

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            CityDatabase::class.java
        ).build()
        dao = db.cityDao()
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun insertAndSearchByName_returnsCorrectCities() = runBlocking {
        val city = CityEntity(1L, "Alabama", "US", City.Coord(0.0, 0.0))
        dao.insertAll(listOf(city))
        val result = dao.searchByName("Ala")
        Assert.assertEquals(1, result.size)
        Assert.assertEquals("Alabama", result[0].name)
    }
}