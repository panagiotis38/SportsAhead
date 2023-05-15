package com.example.data.base

import com.example.data.base.DataErrorCodes.ERROR_MESSAGE_EMPTY_DATA
import com.example.data.base.DataErrorCodes.MAPPING_ERROR
import com.example.network.base.BaseNetworkListResponse
import com.example.network.base.BaseNetworkRequest
import com.example.network.base.NetworkResponse
import java.util.logging.Level
import java.util.logging.Logger

open class BaseDataSource<Y : BaseNetworkRequest, T : BaseNetworkListResponse<*>, K : BaseDataResponse?>(
    private val mapper: BaseDataMapper<T, K>,
    private val errorTransformer: BaseErrorTransformer<T, K>
) {

    suspend fun execute(request: Y, provider: suspend (Y) -> NetworkResponse<T>): DataResponse<K> {

        val networkResponse = provider.invoke(request)

        return toDataResponse(networkResponse)
    }

    private suspend fun toDataResponse(networkResponse: NetworkResponse<T>): DataResponse<K> {
        return when (networkResponse) {
            is NetworkResponse.Success -> {
                mapper.mapToData(networkResponse.data)?.let {
                    Logger.getGlobal().log(Level.INFO, "Success with ${networkResponse.data}")
                    DataResponse.Success(it)
                } ?: kotlin.run {
                    Logger.getGlobal().log(Level.INFO, "Error with empty data")
                    DataResponse.Error(code = MAPPING_ERROR, message = ERROR_MESSAGE_EMPTY_DATA)
                }
            }
            is NetworkResponse.Error -> {
                errorTransformer.transformApiError(networkResponse)
            }
        }
    }

}