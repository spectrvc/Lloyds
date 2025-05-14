package com.art.lloyds.ui.screens.detail

import androidx.lifecycle.SavedStateHandle
import com.art.domain.common.DispatcherProvider
import com.art.domain.common.GlobalConst.Companion.NAVIGATION_OPTIONS
import com.art.domain.common.GlobalConst.Companion.NAVIGATION_OPTIONS_ERROR
import com.art.domain.navigation.NavigationUseCase
import com.art.domain.navigation.ScreenOptions
import com.art.domain.store.ApiResult
import com.art.domain.store.ProductDto
import com.art.domain.store.StoreUseCase
import com.google.gson.Gson
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
import org.mockito.kotlin.any
import org.mockito.kotlin.mock

class DetailViewModelTest {

    private lateinit var viewModel: DetailViewModel
    private val navigationUseCase = mock<NavigationUseCase>()
    private val storeUseCase = mock<StoreUseCase>()

    //Random test data
    private val apiResult = ApiResult.Success(
        ProductDto(
            id = 2194,
            title = "125",
            category = "325",
            price = 2.3,
            image = "451"
        )
    )
    private val productId = 154
    private val options = ScreenOptions.DetailOptions(productId = productId)
    private val savedStateHandle = SavedStateHandle().apply {
        set(NAVIGATION_OPTIONS, Gson().toJson(options))
    }

    @AfterEach
    fun afterEach() {
        Mockito.reset(navigationUseCase)
        Mockito.reset(storeUseCase)
    }

    private fun createViewModel(
        testScheduler: TestCoroutineScheduler,
        savedStateHandle: SavedStateHandle,
    ) {
        val testDispatcher = StandardTestDispatcher(testScheduler)
        val dispatcherProvider = DispatcherProvider(
            io = testDispatcher,
            main = testDispatcher,
            default = testDispatcher
        )
        viewModel = DetailViewModel(
            dispatcherProvider = dispatcherProvider,
            savedStateHandle = savedStateHandle,
            navigationUseCase = navigationUseCase,
            storeUseCase = storeUseCase,
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `init should set options to state`() = runTest {
        Mockito.`when`(storeUseCase.getProduct(any())).thenReturn(apiResult)
        createViewModel(
            testScheduler = testScheduler,
            savedStateHandle = savedStateHandle
        )
        advanceUntilIdle()
        assertEquals(options, viewModel.uiState.value.options)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `init should call getProduct`() = runTest {
        Mockito.`when`(storeUseCase.getProduct(any())).thenReturn(apiResult)
        createViewModel(
            testScheduler = testScheduler,
            savedStateHandle = savedStateHandle
        )
        advanceUntilIdle()
        Mockito.verify(storeUseCase, Mockito.times(1))
            .getProduct(productId)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `init should throw exceptions if savedStateHandle do not contain NAVIGATION_OPTIONS`() =
        runTest {
            var exception: Exception? = null
            try {
                createViewModel(
                    testScheduler = testScheduler,
                    savedStateHandle = SavedStateHandle()
                )
            } catch (e: Exception) {
                exception = e
            }
            advanceUntilIdle()
            assertEquals(NAVIGATION_OPTIONS_ERROR, exception?.message)

        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `init should set apiResult to state`() = runTest {
        Mockito.`when`(storeUseCase.getProduct(any())).thenReturn(apiResult)
        createViewModel(
            testScheduler = testScheduler,
            savedStateHandle = savedStateHandle
        )
        advanceUntilIdle()

        assertEquals(apiResult, viewModel.uiState.value.apiResult)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `init should set indicatorVisibility true then false`() =
        runTest {
            Mockito.`when`(storeUseCase.getProduct(any())).thenReturn(apiResult)
            createViewModel(
                testScheduler = testScheduler,
                savedStateHandle = savedStateHandle
            )

            val flowList = mutableListOf<Boolean>()
            val job = launch(UnconfinedTestDispatcher(testScheduler)) {
                viewModel.uiState.map { it.indicatorVisibility }
                    .distinctUntilChanged()
                    .toList(flowList)
            }
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
    fun `setEvent OnClickBack should navigate back`() = runTest {
        Mockito.`when`(storeUseCase.getProduct(any())).thenReturn(apiResult)
        createViewModel(
            testScheduler = testScheduler,
            savedStateHandle = savedStateHandle
        )

        viewModel.setEvent(DetailEvent.OnClickBack)
        advanceUntilIdle()

        Mockito.verify(navigationUseCase, Mockito.times(1))
            .popBack()
    }
}
