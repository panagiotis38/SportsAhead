package com.example.sportsahead.ui.dashboard.transformer

import com.example.data.model.EventModel
import com.example.data.model.SportModel
import com.example.data.model.UpcomingEventsModel
import com.example.sportsahead.ui.model.dashboard.EventUiModel
import com.example.sportsahead.ui.model.dashboard.SportUiModel
import com.example.sportsahead.ui.model.dashboard.UpcomingEventsUiModel
import javax.inject.Inject

class UpcomingEventsUiTransformerImpl @Inject constructor() : UpcomingEventsUiTransformer {

    override fun transformToUiModel(dataModel: UpcomingEventsModel): UpcomingEventsUiModel {
        return UpcomingEventsUiModel(
            upcomingEvents = extractSports(dataSports = dataModel.upcomingEvents)
        ).also {
            // Expand first section by default
            it.upcomingEvents.firstOrNull()?.isExpanded?.value = true
        }
    }

    private fun extractSports(dataSports: List<SportModel>): List<SportUiModel> {
        return dataSports.map {
            SportUiModel(
                id = it.id,
                name = it.name,
                events = extractEvents(dataEvents = it.events)
            )
        }
    }

    private fun extractEvents(dataEvents: List<EventModel>): List<EventUiModel> {
        return dataEvents.map {
            EventUiModel(
                id = it.id,
                firstContestant = it.firstContestant,
                secondContestant = it.secondContestant,
                millisUntilStart = it.millisUntilStart
            ).apply {
                isFavourite.value = it.isFavourite
            }
        }
    }
}