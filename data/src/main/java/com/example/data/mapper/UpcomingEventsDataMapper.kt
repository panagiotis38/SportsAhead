package com.example.data.mapper

import com.example.data.base.BaseDataMapper
import com.example.data.model.EventModel
import com.example.data.model.FavouriteModel
import com.example.data.model.SportModel
import com.example.data.model.UpcomingEventsModel
import com.example.network.base.BaseNetworkListResponse
import com.example.network.model.UpcomingEvent
import com.example.network.model.UpcomingSport
import com.example.storage.datastore.DataStoreConstants
import com.example.storage.datastore.PrefsDataStore

class UpcomingEventsDataMapper(
    private val dataStore: PrefsDataStore,
) : BaseDataMapper<BaseNetworkListResponse<UpcomingSport?>, UpcomingEventsModel?> {

    override suspend fun mapToData(response: BaseNetworkListResponse<UpcomingSport?>): UpcomingEventsModel {
        val userFavourites = getUserFavourites()
        return UpcomingEventsModel(
            upcomingEvents = extractSports(
                networkSports = response,
                favouritesMap = userFavourites.favouritesMap
            )
        )
    }

    private fun extractSports(networkSports: List<UpcomingSport?>, favouritesMap: Map<String, List<String>>): List<SportModel> {
        return networkSports.map {
            SportModel(
                id = it?.id.orEmpty(),
                name = it?.name.orEmpty(),
                events = extractEvents(
                    networkEvents = it?.upcomingEvents,
                    userFavourites = favouritesMap[it?.id.orEmpty()]
                )
            )
        }
    }

    private fun extractEvents(networkEvents: List<UpcomingEvent?>?, userFavourites: List<String>?): List<EventModel> {
        return networkEvents?.map {
            val contestantNames = it?.name?.split("-")
            EventModel(
                id = it?.id.orEmpty(),
                firstContestant = contestantNames?.firstOrNull().orEmpty(),
                secondContestant = contestantNames?.getOrNull(1).orEmpty(),
                millisUntilStart = it?.timeUntilStart?.toLong(),
                isFavourite = userFavourites?.contains(it?.id.orEmpty()) ?: false
            )
        }.orEmpty()
    }




    private suspend fun getUserFavourites(): FavouriteModel {
        return dataStore.readObject(DataStoreConstants.KEY_USER_FAVORITES, FavouriteModel::class.java)
            ?: FavouriteModel()
    }
}