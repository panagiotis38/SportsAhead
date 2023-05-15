package com.example.data.base

import com.example.data.base.DataErrorCodes.GENERIC_ERROR
import com.example.network.base.BaseNetworkListResponse
import com.example.network.base.NetworkError
import com.example.network.base.NetworkResponse


interface BaseErrorTransformer<T : BaseNetworkListResponse<*>, K : BaseDataResponse?> {
    fun transformApiError(apiResponse: NetworkResponse.Error<T>): DataResponse.Error<K>
}

class BaseErrorTransformerImpl<T : BaseNetworkListResponse<*>, K : BaseDataResponse?> :
    BaseErrorTransformer<T, K> {

    override fun transformApiError(apiResponse: NetworkResponse.Error<T>): DataResponse.Error<K> {
        return when (val errorResponse = apiResponse.error) {
            is NetworkError.ServerError -> {
                DataResponse.Error(
                    code = errorResponse.code ?: GENERIC_ERROR,
                    message = errorResponse.message
                )
            }
            is NetworkError.SystemError -> {
                DataResponse.Error(
                    code = GENERIC_ERROR
                )
            }
        }
    }
}
