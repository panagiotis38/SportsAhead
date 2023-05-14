package com.example.sportsahead.ui.model.dashboard

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class EventUiModel(
    val id: String = "",
    val firstContestant: String = "",
    val secondContestant: String = "",
    val millisUntilStart: Long? = null,
    var isFavourite: MutableState<Boolean> = mutableStateOf(false)
)
