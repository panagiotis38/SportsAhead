package com.example.data.base

import com.example.network.base.BaseNetworkListResponse

interface BaseDataMapper<T : BaseNetworkListResponse<*>, Y : BaseDataResponse?> {

    suspend fun mapToData(response: T): Y

}