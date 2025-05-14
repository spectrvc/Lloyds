package com.art.lloyds.ui.screens.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.art.domain.common.DispatcherProvider
import com.art.domain.common.GlobalConst.Companion.NAVIGATION_OPTIONS
import com.art.domain.common.GlobalConst.Companion.NAVIGATION_OPTIONS_ERROR
import com.art.domain.navigation.NavigationUseCase
import com.art.domain.navigation.ScreenOptions
import com.art.domain.store.StoreUseCase
import com.art.lloyds.ui.main.baseViewModel.BaseViewModel
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    savedStateHandle: SavedStateHandle,
    private val navigationUseCase: NavigationUseCase,
    private val storeUseCase: StoreUseCase,
) : BaseViewModel<DetailState, DetailEffect, DetailEvent>() {

    init {
        initViewModel(dispatcherProvider)
        val optionsJson =
            savedStateHandle.get<String>(NAVIGATION_OPTIONS) ?: throw Exception(
                NAVIGATION_OPTIONS_ERROR
            )
        val options = Gson().fromJson(optionsJson, ScreenOptions.DetailOptions::class.java)
        setState { copy(options = options) }
        receiveData()
    }

    override fun createInitialState(): DetailState {
        return DetailState()
    }

    override fun handleEvent(event: DetailEvent) {
        when (event) {
            is DetailEvent.OnClickBack -> {
                viewModelScope.launch(dispatcherProvider.main) {
                    navigationUseCase.popBack()
                }
            }
        }
    }

    private fun receiveData() {
        viewModelScope.launch(dispatcherProvider.io) {
            showIndicator()
            val apiResult = storeUseCase.getProduct(uiState.value.options.productId)
            setState { copy(apiResult = apiResult) }
            hideIndicator()
        }
    }

    private fun showIndicator() {
        setState { copy(indicatorVisibility = true) }
    }

    private fun hideIndicator() {
        setState { copy(indicatorVisibility = false) }
    }
}

