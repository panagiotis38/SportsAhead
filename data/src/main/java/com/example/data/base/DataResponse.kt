package com.example.data.base

sealed class DataResponse<T> {

    data class Success<T>(var data: T) : DataResponse<T>()

    data class Error<T>(var code: String, var message: String? = null) : DataResponse<T>()

}