package com.art.domain.navigation

class NavigationUseCase(private val navigationRepository: NavigationRepository) {

    val navigationSideEffect = navigationRepository.navigationEffect

    suspend fun navigate(screen: ScreensEnum, options: ScreenOptions) {
        navigationRepository.navigate(screen, options)
    }

    suspend fun popBack() {
        navigationRepository.popBack()
    }

}