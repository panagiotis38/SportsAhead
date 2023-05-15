package com.example.data.datasource

import com.example.data.base.BaseDataSource
import com.example.data.base.BaseErrorTransformerImpl
import com.example.data.base.DataResponse
import com.example.data.mapper.UpcomingEventsDataMapper
import com.example.data.model.FavouriteModel
import com.example.data.model.UpcomingEventsModel
import com.example.network.base.BaseNetworkListResponse
import com.example.network.model.UpcomingEventsNetworkRequest
import com.example.network.model.UpcomingSport
import com.example.network.repo.UpcomingEventsNetworkRepo
import com.example.storage.datastore.DataStoreConstants
import com.example.storage.datastore.PrefsDataStore

class UpcomingEventsDatasource(
    private val eventsNetworkRepo: UpcomingEventsNetworkRepo,
    private val dataStore: PrefsDataStore,
    dataMapper: UpcomingEventsDataMapper,
) : BaseDataSource<UpcomingEventsNetworkRequest, BaseNetworkListResponse<UpcomingSport?>, UpcomingEventsModel?>(
    dataMapper,
    BaseErrorTransformerImpl()
) {

    suspend fun fetchUpcomingEvents(): DataResponse<UpcomingEventsModel?> {
        return execute(
            request = UpcomingEventsNetworkRequest(),
            provider = eventsNetworkRepo::getUpcomingEvents
        )
    }

    suspend fun persistFavourite(sportId: String, eventId: String) {
        val currentFavourites = dataStore.readObject(
            DataStoreConstants.KEY_USER_FAVORITES, FavouriteModel::class.java
        ) ?: FavouriteModel()

        currentFavourites.favouritesMap[sportId]?.add(eventId)
            ?: run {
                // if sport id is not present then add it
                currentFavourites.favouritesMap.put(
                    key = sportId,
                    value = mutableListOf(eventId)
                )
            }
        dataStore.writeObject(
            key = DataStoreConstants.KEY_USER_FAVORITES,
            value = currentFavourites
        )
    }

    suspend fun removeFavourite(sportId: String, eventId: String) {
        val currentFavorites = dataStore.readObject(
            DataStoreConstants.KEY_USER_FAVORITES, FavouriteModel::class.java
        ) ?: FavouriteModel()

        currentFavorites.favouritesMap[sportId]?.remove(eventId)

        dataStore.writeObject(
            key = DataStoreConstants.KEY_USER_FAVORITES,
            value = currentFavorites
        )
    }

}