package com.art.lloyds.ui.screens.main

import com.art.domain.navigation.NavigationDto
import com.art.lloyds.ui.main.baseViewModel.UiEffect
import com.art.lloyds.ui.main.baseViewModel.UiEvent
import com.art.lloyds.ui.main.baseViewModel.UiState
import com.art.lloyds.ui.main.scaffold.DrawerItemData

data class MainState (
    val drawerItemList: List<DrawerItemData> = listOf()
) : UiState

sealed class MainEffect : UiEffect{
    data class ControllerNavigate(val navigationDto: NavigationDto) : MainEffect()
}

sealed class MainEvent : UiEvent {
    data class Navigate(val drawerItemData: DrawerItemData) : MainEvent()
}
