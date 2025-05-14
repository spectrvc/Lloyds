package com.art.domain.navigation

import kotlinx.coroutines.flow.SharedFlow

interface NavigationRepository {

    val navigationEffect: SharedFlow<NavigationDto>

    suspend fun navigate(screen: ScreensEnum, options: ScreenOptions)

    suspend fun popBack()


}