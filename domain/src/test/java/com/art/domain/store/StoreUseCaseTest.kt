package com.art.domain.store

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock

class StoreUseCaseTest {

    private lateinit var useCase: StoreUseCase
    private val storeRepository = mock<StoreRepository>()

    @AfterEach
    fun afterEach() {
        Mockito.reset(storeRepository)
    }

    private fun createUseCase() {
        useCase = StoreUseCase(
            storeRepository = storeRepository,
        )
    }

    private var productId = 123

    @Test
    fun `getProductList should call getProductList from repository`() = runTest {
        createUseCase()
        useCase.getProductList()
        Mockito.verify(storeRepository, Mockito.times(1))
            .getProductList()
    }

    @Test
    fun `getProduct should call getProduct from repository`() = runTest {
        createUseCase()
        useCase.getProduct(productId)
        Mockito.verify(storeRepository, Mockito.times(1))
            .getProduct(productId)
    }

}