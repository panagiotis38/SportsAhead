package com.example.network.base

import com.google.gson.Gson
import retrofit2.HttpException
import retrofit2.Response
import java.io.EOFException
import java.net.UnknownHostException
import java.util.logging.Level
import java.util.logging.Logger
import javax.net.ssl.SSLHandshakeException

abstract class BaseNetworkRepo {

    private val logger = Logger.getGlobal()
    private fun getTag(): String = this.javaClass.simpleName

    suspend fun <T> execute(call: suspend () -> T): NetworkResponse<T> {
        return try {
            val response = call.invoke()
            NetworkResponse.Success(response)
        } catch (eofe: EOFException) {
            NetworkResponse.Error(
                NetworkError.SystemError(
                    code = DECODING_ERROR
                )
            )
        } catch (http: HttpException) {
            logger.log(Level.SEVERE, "${getTag()} -> Error with ${http.message}", http)
            NetworkResponse.Error(http.response()?.let { parseErrorResponse(it) }
                ?: NetworkError.SystemError(code = EMPTY_BODY_ERROR))
        } catch (sslException: SSLHandshakeException) {
            logger.log(
                Level.SEVERE,
                "${getTag()} -> Error with ${sslException.message}",
                sslException
            )
            NetworkResponse.Error(NetworkError.SystemError(code = SSL_ERROR))
        } catch (unknownHostException: UnknownHostException) {
            logger.log(
                Level.SEVERE,
                "${getTag()} -> Error with ${unknownHostException.message}",
                unknownHostException
            )
            NetworkResponse.Error(NetworkError.SystemError(code = UNKNOWN_HOST_ERROR))
        } catch (e: Exception) {
            logger.log(Level.SEVERE, "${getTag()} -> Error with ${e.message}", e)
            NetworkResponse.Error(NetworkError.SystemError(code = GENERAL_ERROR))
        }
    }

    private fun parseErrorResponse(response: Response<*>): NetworkError {
        response.errorBody()?.let {
            try {
                Gson().fromJson(it.string(), NetworkError.ServerError::class.java)
                    ?.let { error ->
                        return error
                    } ?: return NetworkError.SystemError(code = PARSING_ERROR)
            } catch (e: Exception) {
                return NetworkError.SystemError(code = PARSING_ERROR)
            }
        } ?: return NetworkError.SystemError(code = EMPTY_BODY_ERROR)
    }

    companion object {
        const val SSL_ERROR = "SSL_ERROR"
        const val GENERAL_ERROR = "GENERAL_ERROR"
        const val UNKNOWN_HOST_ERROR = "UNKNOWN_HOST_ERROR"
        const val PARSING_ERROR = "PARSING_ERROR"
        const val DECODING_ERROR = "DECODING_ERROR"
        const val EMPTY_BODY_ERROR = "EMPTY_BODY_ERROR"
    }
}