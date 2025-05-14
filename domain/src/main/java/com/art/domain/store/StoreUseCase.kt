package com.art.domain.store

class StoreUseCase(
    private val storeRepository: StoreRepository,
) {

    suspend fun getProductList(): ApiResult<List<ProductDto>> =
        storeRepository.getProductList()

    suspend fun getProduct(productId: Int): ApiResult<ProductDto> =
        storeRepository.getProduct(productId)

}

