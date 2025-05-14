package com.art.lloyds.ui.screens.home

import androidx.lifecycle.viewModelScope
import com.art.domain.common.DispatcherProvider
import com.art.domain.navigation.NavigationUseCase
import com.art.domain.navigation.ScreenOptions
import com.art.domain.navigation.ScreensEnum
import com.art.domain.store.StoreUseCase
import com.art.lloyds.ui.main.baseViewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val navigationUseCase: NavigationUseCase,
    private val storeUseCase: StoreUseCase,
) : BaseViewModel<HomeState, HomeEffect, HomeEvent>() {

    init {
        initViewModel(dispatcherProvider)
    }

    override fun createInitialState(): HomeState {
        return HomeState()
    }

    override fun handleEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.OnClickGetProductList -> {
                onClickGetProductList()
            }

            is HomeEvent.OnClickListItem -> navigateToDialog(event.productId)
        }
    }

    private fun onClickGetProductList() {
        viewModelScope.launch(dispatcherProvider.io) {
            showIndicator()
            val apiResult = storeUseCase.getProductList()
            setState { copy(apiResult = apiResult) }
            hideIndicator()
        }
    }

    private fun navigateToDialog(id: Int) {
        viewModelScope.launch(dispatcherProvider.main) {
            navigationUseCase.navigate(
                screen = ScreensEnum.DETAIL,
                options = ScreenOptions.DetailOptions(productId = id)
            )
        }
    }

    private fun showIndicator() {
        setState { copy(indicatorVisibility = true) }
    }

    private fun hideIndicator() {
        setState { copy(indicatorVisibility = false) }
    }

}











