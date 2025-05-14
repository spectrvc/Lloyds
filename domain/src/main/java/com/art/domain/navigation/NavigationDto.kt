package com.art.domain.navigation

data class NavigationDto(
    val command: NavigationEnum,
    val route: String = "",
)
