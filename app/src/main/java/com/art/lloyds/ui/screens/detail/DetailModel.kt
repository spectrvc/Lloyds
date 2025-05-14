package com.art.lloyds.ui.screens.detail

import com.art.domain.navigation.ScreenOptions
import com.art.domain.store.ApiResult
import com.art.domain.store.ProductDto
import com.art.lloyds.ui.main.baseViewModel.UiEffect
import com.art.lloyds.ui.main.baseViewModel.UiEvent
import com.art.lloyds.ui.main.baseViewModel.UiState

data class DetailState(
    val options: ScreenOptions.DetailOptions = ScreenOptions.DetailOptions(),
    val indicatorVisibility: Boolean = false,
    val apiResult: ApiResult<ProductDto> = ApiResult.Success(ProductDto()),
) : UiState

sealed class DetailEffect : UiEffect

sealed class DetailEvent : UiEvent {
    data object OnClickBack : DetailEvent()
}

