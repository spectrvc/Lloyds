package com.art.domain.store

interface StoreRepository {

    suspend fun getProductList(): ApiResult<List<ProductDto>>

    suspend fun getProduct(productId: Int): ApiResult<ProductDto>

}