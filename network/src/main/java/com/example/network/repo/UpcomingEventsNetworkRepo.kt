package com.example.network.repo

import com.example.network.api.ApiService
import com.example.network.base.BaseNetworkListResponse
import com.example.network.base.BaseNetworkRepo
import com.example.network.base.NetworkResponse
import com.example.network.model.UpcomingEventsNetworkRequest
import com.example.network.model.UpcomingSport

class UpcomingEventsNetworkRepo(private val apiService: ApiService) : BaseNetworkRepo() {

    suspend fun getUpcomingEvents(request: UpcomingEventsNetworkRequest): NetworkResponse<BaseNetworkListResponse<UpcomingSport?>> {
        return execute {
            apiService.getUpcomingEvents()
        }
    }

}