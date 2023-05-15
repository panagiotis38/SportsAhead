package com.example.data.mapper

import com.example.data.model.FavouriteModel
import com.example.network.model.UpcomingEvent
import com.example.network.model.UpcomingEventsNetworkResponse
import com.example.network.model.UpcomingSport
import com.example.storage.datastore.DataStoreConstants
import com.example.storage.datastore.PrefsDataStore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
internal class UpcomingEventsDataMapperTest {

    @Mock
    private lateinit var mockDataStore: PrefsDataStore

    private lateinit var mapper: UpcomingEventsDataMapper


    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        mapper = UpcomingEventsDataMapper(dataStore = mockDataStore)
    }

    @Test
    fun `given network response with empty list when mapToData is called then return data model with empty list`() =
        runTest {
            Mockito.`when`(
                mockDataStore.readObject(
                    DataStoreConstants.KEY_USER_FAVORITES,
                    FavouriteModel::class.java
                )
            ).thenReturn(
                FavouriteModel(favouritesMap = mutableMapOf())
            )
            val networkResponse = UpcomingEventsNetworkResponse()

            val dataModel = mapper.mapToData(response = networkResponse)

            Assert.assertTrue(dataModel.upcomingEvents.isEmpty())
        }

    @Test
    fun `given network response with sport list of null values when mapToData is called then return default values`() =
        runTest {
            Mockito.`when`(
                mockDataStore.readObject(
                    DataStoreConstants.KEY_USER_FAVORITES,
                    FavouriteModel::class.java
                )
            ).thenReturn(
                FavouriteModel(favouritesMap = mutableMapOf())
            )
            val networkResponse = UpcomingEventsNetworkResponse().apply {
                add(null)
                add(null)
            }

            val dataModel = mapper.mapToData(response = networkResponse)

            with(dataModel.upcomingEvents) {
                Assert.assertTrue(size == 2)
                Assert.assertTrue(first().id.isEmpty())
                Assert.assertTrue(first().name.isEmpty())
                Assert.assertTrue(first().events.isEmpty())
            }
        }

    @Test
    fun `given network response with null event list when mapToData is called then return data model with empty list`() =
        runTest {
            Mockito.`when`(
                mockDataStore.readObject(
                    DataStoreConstants.KEY_USER_FAVORITES,
                    FavouriteModel::class.java
                )
            ).thenReturn(
                FavouriteModel(favouritesMap = mutableMapOf())
            )
            val networkResponse = UpcomingEventsNetworkResponse().apply {
                add(
                    UpcomingSport(
                        id = null,
                        name = null,
                        upcomingEvents = null
                    )
                )
            }

            val dataModel = mapper.mapToData(response = networkResponse)

            Assert.assertTrue(dataModel.upcomingEvents.first().events.isEmpty())
        }

    @Test
    fun `given network response with empty event list when mapToData is called then return data model with empty list`() =
        runTest {
            Mockito.`when`(
                mockDataStore.readObject(
                    DataStoreConstants.KEY_USER_FAVORITES,
                    FavouriteModel::class.java
                )
            ).thenReturn(
                FavouriteModel(favouritesMap = mutableMapOf())
            )
            val networkResponse = UpcomingEventsNetworkResponse().apply {
                add(
                    UpcomingSport(
                        id = null,
                        name = null,
                        upcomingEvents = listOf()
                    )
                )
            }

            val dataModel = mapper.mapToData(response = networkResponse)

            Assert.assertTrue(dataModel.upcomingEvents.first().events.isEmpty())
        }

    @Test
    fun `given network response with event list of null values when mapToData is called then return default values`() =
        runTest {
            Mockito.`when`(
                mockDataStore.readObject(
                    DataStoreConstants.KEY_USER_FAVORITES,
                    FavouriteModel::class.java
                )
            ).thenReturn(
                FavouriteModel(favouritesMap = mutableMapOf())
            )
            val networkResponse = UpcomingEventsNetworkResponse().apply {
                add(
                    UpcomingSport(
                        id = null,
                        name = null,
                        upcomingEvents = listOf(null, null, null)
                    )
                )
            }

            val dataModel = mapper.mapToData(response = networkResponse)

            with(dataModel.upcomingEvents.first().events) {
                Assert.assertTrue(size == 3)
                Assert.assertTrue(first().id.isEmpty())
                Assert.assertTrue(first().firstContestant.isEmpty())
                Assert.assertTrue(first().secondContestant.isEmpty())
                Assert.assertTrue(first().millisUntilStart == null)
                Assert.assertFalse(first().isFavourite)
            }
        }

    @Test
    fun `when mapToData is called then return map values correctly`() = runTest {
        Mockito.`when`(
            mockDataStore.readObject(
                DataStoreConstants.KEY_USER_FAVORITES,
                FavouriteModel::class.java
            )
        ).thenReturn(
            FavouriteModel(favouritesMap = mutableMapOf())
        )
        val networkResponse = UpcomingEventsNetworkResponse().apply {
            add(sportModel(1000L))
            add(sportModel(2000L))
            add(sportModel(3000L))
        }

        val dataModel = mapper.mapToData(response = networkResponse)

        with(dataModel.upcomingEvents) {
            Assert.assertTrue(size == 3)
            Assert.assertTrue(first().id == "1000")
            Assert.assertTrue(first().name == "Sport 1000")
            Assert.assertTrue(first().events.size == 2)
            Assert.assertTrue(first().events.first().id == "1001")
            Assert.assertTrue(first().events.first().firstContestant == "Contestant 1 of 1001")
            Assert.assertTrue(first().events.first().secondContestant == "Contestant 2 of 1001")
            Assert.assertTrue(first().events.first().millisUntilStart == 10000L)
        }
    }

    @Test
    fun `given empty favourite list when mapToData is called then return data model with no favourites`() =
        runTest {
            Mockito.`when`(
                mockDataStore.readObject(
                    DataStoreConstants.KEY_USER_FAVORITES,
                    FavouriteModel::class.java
                )
            ).thenReturn(
                FavouriteModel(favouritesMap = mutableMapOf())
            )
            val networkResponse = UpcomingEventsNetworkResponse().apply {
                add(sportModel(1000L))
                add(sportModel(2000L))
                add(sportModel(3000L))
            }

            val dataModel = mapper.mapToData(response = networkResponse)

            with(dataModel.upcomingEvents.first().events) {
                Assert.assertFalse(first().isFavourite)
                Assert.assertFalse(get(1).isFavourite)
            }
        }

    @Test
    fun `given favourite list when mapToData is called then map favourites correctly`() = runTest {
        Mockito.`when`(
            mockDataStore.readObject(
                DataStoreConstants.KEY_USER_FAVORITES,
                FavouriteModel::class.java
            )
        ).thenReturn(
            FavouriteModel(
                favouritesMap = mutableMapOf(
                    "1000" to mutableListOf("1001", "1002"),
                    "2000" to mutableListOf("2001")
                )
            )
        )
        val networkResponse = UpcomingEventsNetworkResponse().apply {
            add(sportModel(1000L))
            add(sportModel(2000L))
            add(sportModel(3000L))
        }

        val dataModel = mapper.mapToData(response = networkResponse)

        with(dataModel.upcomingEvents) {
            Assert.assertTrue(first().events.first().isFavourite)
            Assert.assertTrue(first().events[1].isFavourite)
            Assert.assertTrue(get(1).events.first().isFavourite)
            Assert.assertFalse(get(1).events[1].isFavourite)
            Assert.assertFalse(get(2).events.first().isFavourite)
            Assert.assertFalse(get(2).events[1].isFavourite)
        }
    }

    private fun sportModel(id: Long) = UpcomingSport(
        id = id.toString(),
        name = "Sport $id",
        upcomingEvents = listOf(
            eventModel(id = id + 1L),
            eventModel(id = id + 2L)
        )
    )

    private fun eventModel(id: Long) = UpcomingEvent(
        id = id.toString(),
        sportId = null,
        name = "Contestant 1 of $id-Contestant 2 of $id",
        description = null,
        timeUntilStart = 10000L
    )

}