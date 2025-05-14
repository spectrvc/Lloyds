package com.art.lloyds.ui.custom

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.art.domain.store.ApiResult
import com.art.lloyds.ui.custom.CustomSizes.Companion.COMMON_PADDING

@Composable
fun <T> CustomResultContainer(
    indicatorVisibility: Boolean = false,
    apiResult: ApiResult<T>,
    content: @Composable () -> Unit,
) {
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        CustomIndicator(indicatorVisibility)
        when (apiResult) {
            is ApiResult.Success -> content()
            is ApiResult.HttpError -> ErrorText("Http Error code: " + apiResult.code)
            is ApiResult.Error -> {
                apiResult.exception.message?.let {
                    ErrorText(it)
                }
            }
        }
    }
}

@Composable
private fun ErrorText(errorText: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = COMMON_PADDING),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = errorText)
    }
}

