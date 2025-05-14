package com.art.lloyds.ui.screens.main

import com.art.domain.common.DispatcherProvider
import com.art.domain.navigation.NavigationUseCase
import com.art.domain.navigation.ScreenOptions
import com.art.domain.navigation.ScreensEnum
import com.art.lloyds.R
import com.art.lloyds.ui.main.scaffold.DrawerItemData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock


class MainViewModelTest {

    private lateinit var viewModel: MainViewModel
    private val navigationUseCase = mock<NavigationUseCase>()

    //Random test data
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
    private val drawerItemData = DrawerItemData(
        id = ScreensEnum.ABOUT,
        resId = 2590,
        imageId = 2955
    )

    @AfterEach
    fun afterEach() {
        Mockito.reset(navigationUseCase)
    }

    private fun createViewModel(testScheduler: TestCoroutineScheduler) {
        val testDispatcher = StandardTestDispatcher(testScheduler)
        val dispatcherProvider = DispatcherProvider(
            io = testDispatcher,
            main = testDispatcher,
            default = testDispatcher
        )
        viewModel = MainViewModel(
            dispatcherProvider = dispatcherProvider,
            navigationUseCase = navigationUseCase,
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `init should set drawerItemList to state`() = runTest {
        Mockito.`when`(navigationUseCase.navigationSideEffect).thenReturn(MutableSharedFlow())
        createViewModel(testScheduler)
        advanceUntilIdle()
        assertEquals(drawerItemList, viewModel.uiState.value.drawerItemList)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `setEvent Navigate should call navigate`() = runTest {
        createViewModel(testScheduler)
        Mockito.`when`(navigationUseCase.navigationSideEffect).thenReturn(MutableSharedFlow())

        viewModel.setEvent(MainEvent.Navigate(drawerItemData))
        advanceUntilIdle()

        Mockito.verify(navigationUseCase, Mockito.times(1))
            .navigate(screen = drawerItemData.id, options = ScreenOptions.EmptyOptions)
    }

}