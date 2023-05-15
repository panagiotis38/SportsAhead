package com.example.sportsahead.ui.dashboard.transformer

import com.example.data.model.EventModel
import com.example.data.model.SportModel
import com.example.data.model.UpcomingEventsModel
import org.junit.Assert
import org.junit.Test

internal class UpcomingEventsUiTransformerImplTest {

    private val transformer = UpcomingEventsUiTransformerImpl()

    @Test
    fun `when transformToUiModel is called then data are mapped correctly`() {
        val dataModel = dataModel()

        val uiModel = transformer.transformToUiModel(dataModel = dataModel)

        with (uiModel.upcomingEvents) {
            Assert.assertTrue(size == 2)
            Assert.assertTrue(first().events.size == 3)
            Assert.assertTrue(first().id == "1000")
            Assert.assertTrue(first().name == "Sport 1000")
            Assert.assertTrue(first().events.first().id == "1001")
            Assert.assertTrue(first().events.first().firstContestant == "First contestant of event 1001")
            Assert.assertTrue(first().events.first().secondContestant == "Second contestant of event 1001")
            Assert.assertTrue(first().events.first().millisUntilStart == (10000L + 1001L))
            Assert.assertTrue(first().events.first().isFavourite.value)
            Assert.assertFalse(first().events[1].isFavourite.value)
        }
    }

    @Test
    fun `when transformToUiModel is called then first event is auto expanded`() {
        val dataModel = dataModel()

        val uiModel = transformer.transformToUiModel(dataModel = dataModel)

        Assert.assertTrue(uiModel.upcomingEvents.first().isExpanded.value)
        Assert.assertFalse(uiModel.upcomingEvents[1].isExpanded.value)
    }

    private fun dataModel() = UpcomingEventsModel(
        upcomingEvents = listOf(
            sportModel(1000L),
            sportModel(2000L),
        ),
        latestEventStartTimeInMillis = 15000L
    )

    private fun sportModel(id: Long) = SportModel(
        id = id.toString(),
        name = "Sport $id",
        events = listOf(
            eventModel(id = id + 1L, favourite = true),
            eventModel(id = id + 2L, favourite = false),
            eventModel(id = id + 3L, favourite = false)
        )
    )

    private fun eventModel(id: Long, favourite: Boolean) = EventModel(
        id = id.toString(),
        firstContestant = "First contestant of event $id",
        secondContestant = "Second contestant of event $id",
        millisUntilStart = 10000L + id,
        isFavourite = favourite
    )

}