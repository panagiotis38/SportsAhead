package com.example.data.model

data class EventModel(
    val id: String,
    val firstContestant: String,
    val secondContestant: String,
    val millisUntilStart: Long?,
    val isFavourite: Boolean
)
