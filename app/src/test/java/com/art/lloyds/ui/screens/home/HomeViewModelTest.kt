package com.art.lloyds.ui.screens.home

import app.cash.turbine.test
import com.art.domain.common.DispatcherProvider
import com.art.domain.navigation.NavigationUseCase
import com.art.domain.navigation.ScreenOptions
import com.art.domain.navigation.ScreensEnum
import com.art.domain.store.ApiResult
import com.art.domain.store.ProductDto
import com.art.domain.store.StoreUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

class HomeViewModelTest {

    private val navigationUseCase = mock<NavigationUseCase>()
    private val storeUseCase = mock<StoreUseCase>()

    @AfterEach
    fun afterEach() {
        Mockito.reset(navigationUseCase)
        Mockito.reset(storeUseCase)
    }

    /////////////////////////////////////////// TEST DATA

    private val productId: Int = 154
    private val apiResult: ApiResult<List<ProductDto>> = ApiResult.Success(
        listOf(
            ProductDto(id = productId)
        )
    )

    /////////////////////////////////////////// BUILDER

    private inner class ViewModelBuilder {

        fun build(testDispatcher: TestDispatcher): HomeViewModel {
            val dispatcherProvider = DispatcherProvider(
                io = testDispatcher,
                main = testDispatcher,
                default = testDispatcher
            )
            return HomeViewModel(
                dispatcherProvider = dispatcherProvider,
                navigationUseCase = navigationUseCase,
                storeUseCase = storeUseCase,
            )
        }

        suspend fun withGetProductListResult(): ViewModelBuilder {
            Mockito.`when`(storeUseCase.getProductList()).doReturn(apiResult)
            return this
        }

    }

    /////////////////////////////////////////// CASES

    internal inner class Cases(private val viewModel: HomeViewModel) {

        fun userOnClickListItem(): Cases {
            viewModel.setEvent(HomeEvent.OnClickListItem(productId))
            return this
        }

        fun userOnClickGetProductList(): Cases {
            viewModel.setEvent(HomeEvent.OnClickGetProductList)
            return this
        }

    }

    /////////////////////////////////////////// VERIFIER

    private fun assertThat(viewModel: HomeViewModel): ViewModelVerifier {
        return ViewModelVerifier(viewModel)
    }

    private inner class ViewModelVerifier(private val viewModel: HomeViewModel) {

        suspend fun indicatorWasShownThenHidden(): ViewModelVerifier {
            viewModel.uiState.map { it.indicatorVisibility }
                .distinctUntilChanged()
                .test {
                    assertEquals(false, awaitItem())    //initial value
                    assertEquals(true, awaitItem())     //showIndicator
                    assertEquals(false, awaitItem())    //hideIndicator value
                    expectNoEvents()
                }
            return this
        }

        suspend fun navigationToDetailWasCalled(): ViewModelVerifier {
            Mockito.verify(navigationUseCase, Mockito.times(1))
                .navigate(
                    screen = ScreensEnum.DETAIL,
                    options = ScreenOptions.DetailOptions(productId)
                )
            return this
        }

        suspend fun getProductListWasCalled(): ViewModelVerifier {
            Mockito.verify(storeUseCase, Mockito.times(1))
                .getProductList()
            return this
        }

        fun apiResultWasGot(): ViewModelVerifier {
            assertEquals(apiResult, viewModel.uiState.value.apiResult)
            return this
        }

    }
///////////////////////////////////////////
    /////////////////////////////////////////// TESTS

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `setEvent(OnClickGetProductList) should cause (getProductListWasCalled)`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        val viewModel = ViewModelBuilder()
            .withGetProductListResult()
            .build(testDispatcher)
        Cases(viewModel)
            .userOnClickGetProductList()
        assertThat(viewModel)
            .getProductListWasCalled()

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `setEvent(OnClickGetProductList) should cause (apiResultWasGot)`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        val viewModel = ViewModelBuilder()
            .withGetProductListResult()
            .build(testDispatcher)
        Cases(viewModel)
            .userOnClickGetProductList()
        assertThat(viewModel)
            .apiResultWasGot()

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `setEvent(OnClickGetProductList) should cause (indicatorWasShownThenHidden)`() =
        runTest {
            val testDispatcher = StandardTestDispatcher(testScheduler)
            val viewModel = ViewModelBuilder()
                .withGetProductListResult()
                .build(testDispatcher)
            val job = launch(testDispatcher) {
                assertThat(viewModel)
                    .indicatorWasShownThenHidden()
            }
            Cases(viewModel)
                .userOnClickGetProductList()
            advanceUntilIdle()
            job.cancel()
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `setEvent(OnClickListItem) should cause (navigationToDetailWasCalled)`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        val viewModel = ViewModelBuilder()
            .build(testDispatcher)
        Cases(viewModel)
            .userOnClickListItem()
        assertThat(viewModel)
            .navigationToDetailWasCalled()
    }

}
