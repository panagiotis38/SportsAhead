package com.example.network.model


import com.google.gson.annotations.SerializedName

data class UpcomingEvent(
    @SerializedName("i")
    val id: String?,
    @SerializedName("si")
    val sportId: String?,
    @SerializedName("d")
    val name: String?,
    @SerializedName("sh")
    val description: String?,
    @SerializedName("tt")
    val timeUntilStart: Int?
)