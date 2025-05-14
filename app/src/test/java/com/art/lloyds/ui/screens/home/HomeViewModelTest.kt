package com.art.lloyds.ui.screens.home

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
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock

class HomeViewModelTest {

    private lateinit var viewModel: HomeViewModel
    private val navigationUseCase = mock<NavigationUseCase>()
    private val storeUseCase = mock<StoreUseCase>()

    //Random test data
    private val apiResult = ApiResult.Success(
        listOf(
            ProductDto(
                id = 2194,
                title = "125",
                category = "325",
                price = 2.3,
                image = "451"
            )
        )
    )
    private val productId = 154

    @AfterEach
    fun afterEach() {
        Mockito.reset(navigationUseCase)
        Mockito.reset(storeUseCase)
    }

    private fun createViewModel(testScheduler: TestCoroutineScheduler) {
        val testDispatcher = StandardTestDispatcher(testScheduler)
        val dispatcherProvider = DispatcherProvider(
                io = testDispatcher,
                main = testDispatcher,
                default = testDispatcher
            )
        viewModel = HomeViewModel(
            dispatcherProvider = dispatcherProvider,
            navigationUseCase = navigationUseCase,
            storeUseCase = storeUseCase
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `setEvent OnClickGetProductList should call getProductList`() = runTest {
        createViewModel(testScheduler)
        Mockito.`when`(storeUseCase.getProductList()).thenReturn(apiResult)

        viewModel.setEvent(HomeEvent.OnClickGetProductList)
        advanceUntilIdle()

        Mockito.verify(storeUseCase, Mockito.times(1))
            .getProductList()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `setEvent OnClickGetProductList should set apiResult to state`() = runTest {
        createViewModel(testScheduler)
        Mockito.`when`(storeUseCase.getProductList()).thenReturn(apiResult)

        assertEquals(HomeState().apiResult, viewModel.uiState.value.apiResult)

        viewModel.setEvent(HomeEvent.OnClickGetProductList)
        advanceUntilIdle()

        assertEquals(apiResult, viewModel.uiState.value.apiResult)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `setEvent OnClickGetProductList should set indicatorVisibility true then false`() =
        runTest {
            createViewModel(testScheduler)
            Mockito.`when`(storeUseCase.getProductList()).thenReturn(apiResult)

            val flowList = mutableListOf<Boolean>()
            val job = launch(UnconfinedTestDispatcher(testScheduler)) {
                viewModel.uiState.map { it.indicatorVisibility }
                    .distinctUntilChanged()
                    .toList(flowList)
            }

            viewModel.setEvent(HomeEvent.OnClickGetProductList)
            advanceUntilIdle()

            assertEquals(3, flowList.size)
            if (flowList.size == 3) {
                assertEquals(false, flowList[0])    //initial value
                assertEquals(true, flowList[1])     //showIndicator
                assertEquals(false, flowList[2])    //hideIndicator
            }
            job.cancel()
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `setEvent OnClickListItem should navigate to DETAIL`() = runTest {
        createViewModel(testScheduler)

        viewModel.setEvent(HomeEvent.OnClickListItem(productId))
        advanceUntilIdle()

        Mockito.verify(navigationUseCase, Mockito.times(1))
            .navigate(
                screen = ScreensEnum.DETAIL,
                options = ScreenOptions.DetailOptions(productId)
            )
    }

}
