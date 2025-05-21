package com.art.lloyds.ui.screens.detail

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
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
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

class DetailViewModelTest {

    private val navigationUseCase = mock<NavigationUseCase>()
    private val storeUseCase = mock<StoreUseCase>()

    @AfterEach
    fun afterEach() {
        Mockito.reset(navigationUseCase)
        Mockito.reset(storeUseCase)
    }

    /////////////////////////////////////////// TEST DATA

    private val productId = 154
    private val options = ScreenOptions.DetailOptions(productId = productId)
    private val apiResult = ApiResult.Success(ProductDto(id = productId))
    private val savedStateHandle = SavedStateHandle().apply {
        set(NAVIGATION_OPTIONS, Gson().toJson(options))
    }

    /////////////////////////////////////////// BUILDER

    private inner class ViewModelBuilder {

        fun build(
            testDispatcher: TestDispatcher,
            savedStateHandle: SavedStateHandle,
        ): DetailViewModel {
            val dispatcherProvider = DispatcherProvider(
                io = testDispatcher,
                main = testDispatcher,
                default = testDispatcher
            )
            return DetailViewModel(
                dispatcherProvider = dispatcherProvider,
                savedStateHandle = savedStateHandle,
                navigationUseCase = navigationUseCase,
                storeUseCase = storeUseCase
            )
        }

        suspend fun withGetProductResult(): ViewModelBuilder {
            Mockito.`when`(storeUseCase.getProduct(any())).doReturn(apiResult)
            return this
        }

    }

    /////////////////////////////////////////// CASES

    internal inner class Cases(private val viewModel: DetailViewModel) {

        fun userOnClickBack(): Cases {
            viewModel.setEvent(DetailEvent.OnClickBack)
            return this
        }

    }

    /////////////////////////////////////////// VERIFIER

    private fun assertThat(viewModel: DetailViewModel): ViewModelVerifier {
        return ViewModelVerifier(viewModel)
    }

    private inner class ViewModelVerifier(private val viewModel: DetailViewModel) {

        suspend fun indicatorWasShownThenHidden(): ViewModelVerifier {
            viewModel.uiState.map { it.indicatorVisibility }
                .distinctUntilChanged()
                .test {
                    assertEquals(false, awaitItem())    //initial value
                    assertEquals(true, awaitItem())     //showIndicator
                    assertEquals(false, awaitItem())    //hideIndicator
                    expectNoEvents()
                }
            return this
        }

        suspend fun navigationPopBackWasCalled(): ViewModelVerifier {
            Mockito.verify(navigationUseCase, Mockito.times(1))
                .popBack()
            return this
        }

        suspend fun getProductWasCalled(): ViewModelVerifier {
            Mockito.verify(storeUseCase, Mockito.times(1))
                .getProduct(productId)
            return this
        }

        fun optionsWasGot(): ViewModelVerifier {
            assertEquals(options, viewModel.uiState.value.options)
            return this
        }

        fun apiResultWasGot(): ViewModelVerifier {
            assertEquals(apiResult, viewModel.uiState.value.apiResult)
            return this
        }

    }

    /////////////////////////////////////////// TESTS

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `(init) should cause (optionsWasGot)`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        val viewModel = ViewModelBuilder()
            .withGetProductResult()
            .build(testDispatcher, savedStateHandle)
        assertThat(viewModel)
            .optionsWasGot()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `(init) should cause (getProductWasCalled)`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        val viewModel = ViewModelBuilder()
            .withGetProductResult()
            .build(testDispatcher, savedStateHandle)
        assertThat(viewModel)
            .getProductWasCalled()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `(init) and (savedStateHandle do not contain NAVIGATION_OPTIONS) should cause (throw exceptions)`() =
        runTest {
            var exception: Exception? = null
            try {
                val testDispatcher = UnconfinedTestDispatcher(testScheduler)
                ViewModelBuilder().build(testDispatcher, SavedStateHandle())
            } catch (e: Exception) {
                exception = e
            }
            assertEquals(NAVIGATION_OPTIONS_ERROR, exception?.message)
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `(init) should cause (apiResultWasGot)`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        val viewModel = ViewModelBuilder()
            .withGetProductResult()
            .build(testDispatcher, savedStateHandle)
        assertThat(viewModel)
            .apiResultWasGot()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `(init) should cause (indicatorWasShownThenHidden)`() =
        runTest {
            val testDispatcher = StandardTestDispatcher(testScheduler)
            val viewModel = ViewModelBuilder()
                .withGetProductResult()
                .build(testDispatcher, savedStateHandle)
            val job = launch(testDispatcher) {
                assertThat(viewModel)
                    .indicatorWasShownThenHidden()
            }
            advanceUntilIdle()
            job.cancel()
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `setEvent(DetailEvent) should cause (navigationPopBackWasCalled)`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        val viewModel = ViewModelBuilder()
            .withGetProductResult()
            .build(testDispatcher, savedStateHandle)
        Cases(viewModel)
            .userOnClickBack()
        assertThat(viewModel)
            .navigationPopBackWasCalled()
    }
}
