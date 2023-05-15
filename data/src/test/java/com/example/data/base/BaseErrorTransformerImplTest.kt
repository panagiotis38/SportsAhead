package com.example.data.base

import com.example.data.base.DataErrorCodes.GENERIC_ERROR
import com.example.network.base.BaseNetworkListResponse
import com.example.network.base.NetworkError
import com.example.network.base.NetworkResponse
import org.junit.Test

internal class BaseErrorTransformerImplTest {

    private val baseErrorTransformer = BaseErrorTransformerImpl<BaseNetworkListResponse<*>, BaseDataResponse>()

    @Test
    fun `when error is server error with non null code then return appropriate data response`() {
        val error = NetworkResponse.Error<BaseNetworkListResponse<*>>(
            NetworkError.ServerError(
                code = "400",
                message = "There is something going wrong"
            )
        )

        val output = baseErrorTransformer.transformApiError(error)

        assert(output.code == "400")
        assert(output.message == "There is something going wrong")
    }

    @Test
    fun `when error is server error with null code then return appropriate data response`() {
        val error = NetworkResponse.Error<BaseNetworkListResponse<*>>(
            NetworkError.ServerError(
                code = null,
                message = "There is something going wrong"
            )
        )

        val output = baseErrorTransformer.transformApiError(error)

        assert(output.code == GENERIC_ERROR)
        assert(output.message == "There is something going wrong")
    }

    @Test
    fun `when error is system error then return appropriate data response`() {
        val error = NetworkResponse.Error<BaseNetworkListResponse<*>>(
            NetworkError.SystemError(
                code = "400",
                message = "There is something going wrong"
            )
        )

        val output = baseErrorTransformer.transformApiError(error)

        assert(output.code == GENERIC_ERROR)
    }
}