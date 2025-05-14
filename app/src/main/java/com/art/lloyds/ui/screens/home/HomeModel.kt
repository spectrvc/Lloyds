package com.art.lloyds.ui.screens.home

import com.art.domain.store.ApiResult
import com.art.domain.store.ProductDto
import com.art.lloyds.ui.main.baseViewModel.UiEffect
import com.art.lloyds.ui.main.baseViewModel.UiEvent
import com.art.lloyds.ui.main.baseViewModel.UiState

data class HomeState(
    val apiResult: ApiResult<List<ProductDto>> = ApiResult.Success(listOf()),
    val indicatorVisibility: Boolean = false
) : UiState

sealed class HomeEffect : UiEffect

sealed class HomeEvent : UiEvent {
    data object OnClickGetProductList : HomeEvent()
    data class OnClickListItem(val productId: Int) : HomeEvent()
}
