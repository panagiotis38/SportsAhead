package com.example.network.api

import com.example.network.base.BaseNetworkListResponse
import com.example.network.model.UpcomingEventsNetworkResponse
import com.example.network.model.UpcomingSport
import retrofit2.http.GET

interface ApiService {

    @GET("api/sports")
    suspend fun getUpcomingEvents(): UpcomingEventsNetworkResponse

}