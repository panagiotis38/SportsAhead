package com.example.sportsahead.ui.dashboard.transformer

import com.example.data.model.EventModel
import com.example.data.model.SportModel
import com.example.data.model.UpcomingEventsModel
import com.example.sportsahead.ui.dashboard.DashboardViewModel
import com.example.sportsahead.ui.model.dashboard.EventUiModel
import com.example.sportsahead.ui.model.dashboard.SportUiModel
import com.example.sportsahead.ui.model.dashboard.UpcomingEventsUiModel
import com.example.sportsahead.utils.formatDate
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class UpcomingEventsUiTransformerImpl @Inject constructor() : UpcomingEventsUiTransformer {

    private val simpleDateFormat = SimpleDateFormat(
        EVENTS_START_TIME_PATTERN,
        Locale.getDefault()
    ).also {
        it.timeZone = TimeZone.getTimeZone(TIMEZONE_UTC)
    }

    override fun transformToUiModel(dataModel: UpcomingEventsModel): UpcomingEventsUiModel {
        return UpcomingEventsUiModel(
            upcomingEvents = extractSports(dataSports = dataModel.upcomingEvents)
        ).also {
            // Expand first section by default
            it.upcomingEvents.firstOrNull()?.isExpanded?.value = true
        }
    }

    override fun formatDate(millis: Long?) = millis?.formatDate(simpleDateFormat).orEmpty()


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
                formattedTimeUntilStart.value = formatDate(millisUntilStart)
            }
        }
    }

    companion object {
        private const val EVENTS_START_TIME_PATTERN = "HH:mm:ss"
        private const val TIMEZONE_UTC = "UTC"
    }
}