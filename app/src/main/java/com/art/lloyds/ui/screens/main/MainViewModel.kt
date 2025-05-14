package com.art.lloyds.ui.screens.main

import androidx.lifecycle.viewModelScope
import com.art.domain.common.DispatcherProvider
import com.art.domain.navigation.NavigationUseCase
import com.art.domain.navigation.ScreenOptions
import com.art.domain.navigation.ScreensEnum
import com.art.lloyds.R
import com.art.lloyds.ui.main.baseViewModel.BaseViewModel
import com.art.lloyds.ui.main.scaffold.DrawerItemData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val navigationUseCase: NavigationUseCase,
) : BaseViewModel<MainState, MainEffect, MainEvent>() {

    init {
        initViewModel(dispatcherProvider)
        receiveData()
    }

    override fun createInitialState(): MainState {
        return MainState()
    }

    override fun handleEvent(event: MainEvent) {
        when (event) {
            is MainEvent.Navigate -> navigate(event.drawerItemData)
        }
    }

    private fun receiveData() {
        receiveDrawerItemList()
        receiveNavigation()
    }

    private fun receiveDrawerItemList() {
        val drawerItemList = listOf(
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
        setState { copy(drawerItemList = drawerItemList) }
    }

    private fun receiveNavigation() {
        viewModelScope.launch(dispatcherProvider.main) {
            navigationUseCase.navigationSideEffect.collect { navigationDto ->
                setEffect(MainEffect.ControllerNavigate(navigationDto))
            }
        }
    }

    private fun navigate(drawerItemData: DrawerItemData) {
        viewModelScope.launch(dispatcherProvider.main) {
            navigationUseCase.navigate(
                screen = drawerItemData.id,
                options = ScreenOptions.EmptyOptions
            )
        }
    }

}


