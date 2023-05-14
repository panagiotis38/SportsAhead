package com.example.network.model


import com.google.gson.annotations.SerializedName

data class UpcomingSport(
    @SerializedName("i")
    val id: String?,
    @SerializedName("d")
    val name: String?,
    @SerializedName("e")
    val upcomingEvents: List<UpcomingEvent?>?
)