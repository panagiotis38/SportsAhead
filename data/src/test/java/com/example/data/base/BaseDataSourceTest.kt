package com.example.data.base

import com.example.network.base.BaseNetworkListResponse
import com.example.network.base.BaseNetworkRequest
import com.example.network.base.NetworkError
import com.example.network.base.NetworkResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any

@ExperimentalCoroutinesApi
internal class BaseDataSourceTest {

    @Mock
    private lateinit var mapper: BaseDataMapper<BaseNetworkListResponse<*>, BaseDataResponse>

    @Mock
    private lateinit var errorTransformer: BaseErrorTransformer<BaseNetworkListResponse<*>, BaseDataResponse>

    private lateinit var baseDataSource: BaseDataSource<BaseNetworkRequest, BaseNetworkListResponse<*>, BaseDataResponse>

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        baseDataSource = BaseDataSource(
            mapper = mapper,
            errorTransformer = errorTransformer
        )
    }

    @Test
    fun `when network response is successful and data model is not null then return success data response`() =
        runTest {
            Mockito.`when`(mapper.mapToData(any())).thenReturn(BaseDataResponse())
            val provider: (BaseNetworkRequest) -> NetworkResponse<BaseNetworkListResponse<*>> = {
                NetworkResponse.Success(BaseNetworkListResponse<String>())
            }

            val output = baseDataSource.execute(
                request = BaseNetworkRequest(),
                provider = provider
            )

            assert(output is DataResponse.Success)
        }

    @Test
    fun `when network response is successful but data model is null then return appropriate error data response`() =
        runTest {
            Mockito.`when`(mapper.mapToData(any())).thenReturn(null)
            val provider: (BaseNetworkRequest) -> NetworkResponse<BaseNetworkListResponse<*>> = {
                NetworkResponse.Success(BaseNetworkListResponse<String>())
            }

            val output = baseDataSource.execute(
                request = BaseNetworkRequest(),
                provider = provider
            )

            assert(output is DataResponse.Error)
            assert((output as DataResponse.Error).code == DataErrorCodes.MAPPING_ERROR)
            assert(output.message == DataErrorCodes.ERROR_MESSAGE_EMPTY_DATA)
        }

    @Test
    fun `when network response is erroneous then invoke base error transformer`() = runTest {
        val networkResponse =
            NetworkResponse.Error<BaseNetworkListResponse<*>>(NetworkError.ServerError("400", ""))
        Mockito.`when`(errorTransformer.transformApiError(any())).thenReturn(
            DataResponse.Error(
                code = DataErrorCodes.GENERIC_ERROR
            )
        )
        val provider: (BaseNetworkRequest) -> NetworkResponse<BaseNetworkListResponse<*>> = {
            networkResponse
        }

        baseDataSource.execute(
            request = BaseNetworkRequest(),
            provider = provider
        )

        Mockito.verify(errorTransformer).transformApiError(networkResponse)
    }

}