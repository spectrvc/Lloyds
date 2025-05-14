package com.art.data.store

import com.art.domain.store.ApiResult
import com.art.domain.store.ProductDto
import com.art.domain.store.StoreRepository
import retrofit2.HttpException

class StoreRepositoryImpl(val storeApiService: StoreApiService) : StoreRepository {

    override suspend fun getProductList(): ApiResult<List<ProductDto>> {
        return apiResultWrapper {
            storeApiService.getProductList().map { it.toDto() }
        }
    }

    override suspend fun getProduct(productId: Int): ApiResult<ProductDto> {
        return apiResultWrapper {
            storeApiService.getProduct(productId).toDto()
        }
    }

    private suspend fun <T> apiResultWrapper(call: suspend () -> T): ApiResult<T> {
        return try {
            ApiResult.Success(call())
        } catch (e: HttpException) {
            ApiResult.HttpError(e.code())
        } catch (e: Exception) {
            ApiResult.Error(e)
        }
    }

}





