package com.example.data.model

data class SportModel(
    val id: String,
    val name: String,
    val events: List<EventModel>
)