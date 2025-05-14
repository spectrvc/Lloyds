package com.art.domain.navigation

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock

class NavigationUseCaseTest {

    private lateinit var useCase: NavigationUseCase
    private val navigationRepository = mock<NavigationRepository>()

    @AfterEach
    fun afterEach() {
        Mockito.reset(navigationRepository)
    }

    private fun createUseCase() {
        useCase = NavigationUseCase(
            navigationRepository = navigationRepository,
        )
    }

    private var screen = ScreensEnum.ABOUT
    private var options = ScreenOptions.EmptyOptions

    @Test
    fun `navigate should call navigate from repository`() = runTest {
        createUseCase()
        useCase.navigate(screen, options)
        Mockito.verify(navigationRepository, Mockito.times(1))
            .navigate(screen, options)
    }

    @Test
    fun `popBack should call popBack from repository`() = runTest {
        createUseCase()
        useCase.popBack()
        Mockito.verify(navigationRepository, Mockito.times(1))
            .popBack()
    }

    @Test
    fun `init should get navigationEffect from repository`() = runTest {
        createUseCase()
        Mockito.verify(navigationRepository, Mockito.times(1))
            .navigationEffect
    }



}