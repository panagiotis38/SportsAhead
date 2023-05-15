package com.example.sportsahead.ui.model.dashboard

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class SportUiModel(
    val id: String = "",
    val name: String = "",
    val events: List<EventUiModel> = listOf(),
    var isExpanded: MutableState<Boolean> = mutableStateOf(false)
)