package com.art.data.navigation

import com.art.domain.navigation.NavigationDto
import com.art.domain.navigation.NavigationEnum
import com.art.domain.navigation.NavigationRepository
import com.art.domain.navigation.ScreenOptions
import com.art.domain.navigation.ScreensEnum
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class NavigationRepositoryImpl : NavigationRepository {
    private val _navigationEffect = MutableSharedFlow<NavigationDto>()
    override val navigationEffect = _navigationEffect.asSharedFlow()

    override suspend fun navigate(screen: ScreensEnum, options: ScreenOptions) {
        val route = if (options is ScreenOptions.EmptyOptions)
            screen.name
        else
            screen.name + "/" + Gson().toJson(options)
        val navigationDto = NavigationDto(
            command = NavigationEnum.NAVIGATE,
            route = route,
        )
        _navigationEffect.emit(navigationDto)
    }

    override suspend fun popBack() {
        val navigationDto = NavigationDto(
            command = NavigationEnum.POP_BACK,
        )
        _navigationEffect.emit(navigationDto)
    }

}