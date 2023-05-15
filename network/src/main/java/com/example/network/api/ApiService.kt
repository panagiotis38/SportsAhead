package com.example.network.api

import com.example.network.model.UpcomingEventsNetworkResponse
import retrofit2.http.GET

interface ApiService {

    @GET("api/sports")
    suspend fun getUpcomingEvents(): UpcomingEventsNetworkResponse

}