package com.example.data.model

import com.example.data.base.BaseDataResponse

data class UpcomingEventsModel(
    val upcomingEvents: List<SportModel>,
    val latestEventStartTimeInMillis: Long
) : BaseDataResponse()