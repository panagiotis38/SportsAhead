package com.example.network.utils

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import retrofit2.Response

fun <T> buildHttpException(code: Int, message: String): HttpException {
    return HttpException(createErrorResponse<T>(code, message))
}

fun <T> createErrorResponse(code: Int, message: String): Response<T> {
    return Response.error(code, message.toResponseBody("text/plain".toMediaTypeOrNull()))
}