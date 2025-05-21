package com.art.lloyds.ui.screens.main

import app.cash.turbine.test
import com.art.domain.common.DispatcherProvider
import com.art.domain.navigation.NavigationDto
import com.art.domain.navigation.NavigationEnum
import com.art.domain.navigation.NavigationUseCase
import com.art.domain.navigation.ScreenOptions
import com.art.domain.navigation.ScreensEnum
import com.art.lloyds.R
import com.art.lloyds.ui.main.baseViewModel.UiEffect
import com.art.lloyds.ui.main.scaffold.DrawerItemData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
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
import org.mockito.kotlin.whenever

class MainViewModelTest {

    private val navigationUseCase = mock<NavigationUseCase>()

    @AfterEach
    fun afterEach() {
        Mockito.reset(navigationUseCase)
    }

    /////////////////////////////////////////// TEST DATA

    private val drawerItemData = DrawerItemData(
        id = ScreensEnum.ABOUT,
        resId = 2590,
        imageId = 2955
    )
    private val drawerItemList = listOf(
        DrawerItemData(
            id = ScreensEnum.HOME,
            resId = R.string.menu_home,
            imageId = R.drawable.home
        ),
        DrawerItemData(
            id = ScreensEnum.ABOUT,
            resId = R.string.menu_about,
            imageId = R.drawable.about
        )
    )
    private val navigationDto = NavigationDto(NavigationEnum.POP_BACK)
    val sharedFlow = MutableSharedFlow<NavigationDto>()

    /////////////////////////////////////////// BUILDER

    private inner class ViewModelBuilder {

        fun build(testDispatcher: TestDispatcher): MainViewModel {
            val dispatcherProvider = DispatcherProvider(
                io = testDispatcher,
                main = testDispatcher,
                default = testDispatcher
            )
            return MainViewModel(
                dispatcherProvider = dispatcherProvider,
                navigationUseCase = navigationUseCase,
            )
        }

        fun withNavigationSideEffect(): ViewModelBuilder {
            whenever(navigationUseCase.navigationSideEffect).doReturn(sharedFlow)
            return this
        }

    }

    /////////////////////////////////////////// CASES

    internal inner class Cases(private val viewModel: MainViewModel) {

        fun userNavigate(): Cases {
            viewModel.setEvent(MainEvent.Navigate(drawerItemData))
            return this
        }

        suspend fun navigationSideEffectWasReceived(): Cases {
            sharedFlow.emit(navigationDto)
            return this
        }

    }

    /////////////////////////////////////////// VERIFIER

    private fun assertThat(viewModel: MainViewModel): ViewModelVerifier {
        return ViewModelVerifier(viewModel)
    }

    private inner class ViewModelVerifier(private val viewModel: MainViewModel) {

        suspend fun navigateWasCalled(): ViewModelVerifier {
            Mockito.verify(navigationUseCase, Mockito.times(1))
                .navigate(screen = drawerItemData.id, options = ScreenOptions.EmptyOptions)
            return this
        }

        fun drawerItemListWasGot(): ViewModelVerifier {
            assertEquals(drawerItemList, viewModel.uiState.value.drawerItemList)
            return this
        }

        fun uiEffectWasEmitted(uiEffect: UiEffect): ViewModelVerifier {
            assertEquals(MainEffect.ControllerNavigate(navigationDto), uiEffect)
            return this
        }
    }

    /////////////////////////////////////////// TESTS

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `(init) should cause (drawerItemListWasGot)`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        val viewModel = ViewModelBuilder()
            .withNavigationSideEffect()
            .build(testDispatcher)
        assertThat(viewModel)
            .drawerItemListWasGot()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `setEvent(Navigate) should cause (navigateWasCalled)`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        val viewModel = ViewModelBuilder()
            .withNavigationSideEffect()
            .build(testDispatcher)
        Cases(viewModel)
            .userNavigate()
        assertThat(viewModel)
            .navigateWasCalled()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `(navigationSideEffectWasReceived) should cause (uiEffectWasEmitted)`() = runTest {
        val testDispatcher = StandardTestDispatcher(testScheduler)
        val viewModel = ViewModelBuilder()
            .withNavigationSideEffect()
            .build(testDispatcher)
        val job = launch(testDispatcher) {
            viewModel.uiEffect.test {
                Cases(viewModel)
                    .navigationSideEffectWasReceived()
                assertThat(viewModel)
                    .uiEffectWasEmitted(awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }
        advanceUntilIdle()
        job.cancel()
    }

}
