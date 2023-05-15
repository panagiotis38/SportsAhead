package com.example.sportsahead.ui.dashboard.transformer

import com.example.data.model.UpcomingEventsModel
import com.example.sportsahead.ui.model.dashboard.UpcomingEventsUiModel

interface UpcomingEventsUiTransformer {

    fun transformToUiModel(dataModel: UpcomingEventsModel): UpcomingEventsUiModel

    fun formatDate(millis: Long?): String

}