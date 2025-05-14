package com.art.data.navigation

import com.art.domain.navigation.NavigationDto
import com.art.domain.navigation.NavigationEnum
import com.art.domain.navigation.ScreenOptions
import com.art.domain.navigation.ScreensEnum
import com.google.gson.Gson
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class NavigationRepositoryImplTest {

    private lateinit var repository: NavigationRepositoryImpl

    private fun createRepository() {
        repository = NavigationRepositoryImpl()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `popBack should emit to navigationEffect`() = runTest {
        val navigationDto = NavigationDto(
            command = NavigationEnum.POP_BACK,
        )
        createRepository()

        val flowList = mutableListOf<NavigationDto>()
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            repository.navigationEffect.toList(flowList)
        }
        repository.popBack()
        advanceUntilIdle()

        assertEquals(1, flowList.size)
        if (flowList.size == 1) {
            assertEquals(navigationDto, flowList[0])
        }
        job.cancel()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `navigate (if EmptyOptions) should emit to navigationEffect without options`() = runTest {
        val screen = ScreensEnum.ABOUT
        val options = ScreenOptions.EmptyOptions
        val navigationDto = NavigationDto(
            command = NavigationEnum.NAVIGATE,
            route = screen.name,
        )

        createRepository()

        val flowList = mutableListOf<NavigationDto>()
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            repository.navigationEffect.toList(flowList)
        }
        repository.navigate(screen, options)
        advanceUntilIdle()

        assertEquals(1, flowList.size)
        if (flowList.size == 1) {
            assertEquals(navigationDto, flowList[0])
        }
        job.cancel()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `navigate (if not EmptyOptions) should emit to navigationEffect with options`() = runTest {
        val screen = ScreensEnum.ABOUT
        val options = ScreenOptions.DetailOptions(productId = 123)
        val navigationDto = NavigationDto(
            command = NavigationEnum.NAVIGATE,
            route = screen.name + "/" + Gson().toJson(options),
        )

        createRepository()

        val flowList = mutableListOf<NavigationDto>()
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            repository.navigationEffect.toList(flowList)
        }
        repository.navigate(screen, options)
        advanceUntilIdle()

        assertEquals(1, flowList.size)
        if (flowList.size == 1) {
            assertEquals(navigationDto, flowList[0])
        }
        job.cancel()
    }

}