package com.art.data.store

import retrofit2.http.GET
import retrofit2.http.Path

interface StoreApiService {

    @GET("products")
    suspend fun getProductList(): List<ProductItem>

    @GET("/products/{id}")
    suspend fun getProduct(
        @Path("id") productId: Int,
    ): ProductItem

}
