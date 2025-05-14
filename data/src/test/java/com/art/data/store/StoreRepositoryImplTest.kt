package com.art.data.store

import com.art.domain.store.ApiResult
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock
import retrofit2.HttpException
import retrofit2.Response

class StoreRepositoryImplTest {

    private lateinit var repository: StoreRepositoryImpl
    private val storeApiService = mock<StoreApiService>()

    @AfterEach
    fun afterEach() {
        Mockito.reset(storeApiService)
    }

    private fun createRepository() {
        repository = StoreRepositoryImpl(
            storeApiService = storeApiService
        )
    }

    private val productId = 123
    private val productItem = ProductItem(
        id = 123,
        title = "155",
        price = 6.7,
        description = "324",
        category = "23",
        image = "34"
    )
    private val productDto = productItem.toDto()
    private val productItemList = listOf(productItem)
    private val productDtoList = productItemList.map { it.toDto() }

    @Test
    fun `getProduct should return ApiResult Success if no error`() = runTest {
        val apiResult = ApiResult.Success(productDto)
        Mockito.`when`(storeApiService.getProduct(productId)).thenReturn(productItem)
        createRepository()
        assertEquals(apiResult, repository.getProduct(productId))
        Mockito.verify(storeApiService, Mockito.times(1))
            .getProduct(productId)
    }

    @Test
    fun `getProduct should return ApiResult Error if error`() = runTest {
        createRepository()
        assertEquals(true, repository.getProduct(productId) is ApiResult.Error)
    }

    @Test
    fun `getProductList should return ApiResult Success if no error`() = runTest {
        val apiResult = ApiResult.Success(productDtoList)
        Mockito.`when`(storeApiService.getProductList()).thenReturn(productItemList)
        createRepository()
        assertEquals(apiResult, repository.getProductList())
        Mockito.verify(storeApiService, Mockito.times(1))
            .getProductList()
    }

    @Test
    fun `getProductList should return ApiResult Error and code=0 if other then httpException`() =
        runTest {
            val exception = RuntimeException()
            Mockito.`when`(storeApiService.getProductList()).thenThrow(exception)
            createRepository()
            assertEquals(true, repository.getProductList() is ApiResult.Error)
        }

    @Test
    fun `getProductList should return ApiResult Error if error`() = runTest {
        val httpException = HttpException(
            Response.error<Any>(
                500,
                "msg".toResponseBody("plain/text".toMediaTypeOrNull())
            )
        )
        Mockito.`when`(storeApiService.getProductList()).thenThrow(httpException)
        createRepository()
        assertEquals(true, repository.getProductList() is ApiResult.HttpError)
    }
}