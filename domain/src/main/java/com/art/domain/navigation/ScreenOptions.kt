package com.art.domain.navigation

sealed class ScreenOptions {

    data object EmptyOptions : ScreenOptions()

    data class DetailOptions(
        val productId: Int = 0,
    ) : ScreenOptions()

}


