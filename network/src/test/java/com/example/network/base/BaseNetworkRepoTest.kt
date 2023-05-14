package com.example.network.base

import com.example.network.utils.buildHttpException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okio.EOFException
import org.junit.Test
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException

@ExperimentalCoroutinesApi
internal class BaseNetworkRepoTest {

    private val testRepo = TestBaseRepo()

    @Test
    fun `when call is successful the return success response`() = runTest {
        val call = { "" }

        val output = testRepo.execute(call)

        assert(output is NetworkResponse.Success)
    }

    @Test
    fun `when call throws EOFException the return appropriate error response`() = runTest {
        val call = { throw EOFException() }

        val output = testRepo.execute(call)

        assert(output is NetworkResponse.Error)
        assert((output as NetworkResponse.Error).error is NetworkError.SystemError)
        assert((output.error as NetworkError.SystemError).code == BaseNetworkRepo.DECODING_ERROR)
    }

    @Test
    fun `when call throws HttpException with non parsable error body the return appropriate error response`() =
        runTest {
            val call = { throw buildHttpException<String>(400, "this is not a json body") }

            val output = testRepo.execute(call)

            assert(output is NetworkResponse.Error)
            assert((output as NetworkResponse.Error).error is NetworkError.SystemError)
            assert((output.error as NetworkError.SystemError).code == BaseNetworkRepo.PARSING_ERROR)
        }

    @Test
    fun `when call throws HttpException with parsable error body the return appropriate error response`() =
        runTest {
            val call = {
                throw buildHttpException<String>(
                    400,
                    "{code: \"400\", message: \"There is something going wrong\"}"
                )
            }

            val output = testRepo.execute(call)

            assert(output is NetworkResponse.Error)
            assert((output as NetworkResponse.Error).error is NetworkError.ServerError)
            assert((output.error as NetworkError.ServerError).code == "400")
            assert((output.error as NetworkError.ServerError).message == "There is something going wrong")
        }

    @Test
    fun `when call throws SSLHandshakeException the return appropriate error response`() = runTest {
        val call = { throw SSLHandshakeException("") }

        val output = testRepo.execute(call)

        assert(output is NetworkResponse.Error)
        assert((output as NetworkResponse.Error).error is NetworkError.SystemError)
        assert((output.error as NetworkError.SystemError).code == BaseNetworkRepo.SSL_ERROR)
    }

    @Test
    fun `when call throws UnknownHostException the return appropriate error response`() = runTest {
        val call = { throw UnknownHostException() }

        val output = testRepo.execute(call)

        assert(output is NetworkResponse.Error)
        assert((output as NetworkResponse.Error).error is NetworkError.SystemError)
        assert((output.error as NetworkError.SystemError).code == BaseNetworkRepo.UNKNOWN_HOST_ERROR)
    }

    @Test
    fun `when call throws any Exception the return appropriate error response`() = runTest {
        val call = { throw Exception() }

        val output = testRepo.execute(call)

        assert(output is NetworkResponse.Error)
        assert((output as NetworkResponse.Error).error is NetworkError.SystemError)
        assert((output.error as NetworkError.SystemError).code == BaseNetworkRepo.GENERAL_ERROR)
    }

}
