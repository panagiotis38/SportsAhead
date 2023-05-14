package com.example.network.base

import com.google.gson.annotations.SerializedName

sealed class NetworkResponse<T> {

    data class Success<T>(var data: T) : NetworkResponse<T>()

    data class Error<T>(var error: NetworkError) : NetworkResponse<T>()
}

sealed class NetworkError {

    //TODO: Replace with servers network error model
    data class ServerError(
        @SerializedName("code")
        var code: String?,
        @SerializedName("message")
        var message: String? = null,
    ) : NetworkError()

    data class SystemError(
        @SerializedName("code")
        var code: String?,
        @SerializedName("message")
        var message: String? = null,
    ) : NetworkError()
}
