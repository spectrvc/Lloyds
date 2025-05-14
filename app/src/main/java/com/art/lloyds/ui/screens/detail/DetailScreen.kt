package com.art.lloyds.ui.screens.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import coil.compose.AsyncImage
import com.art.domain.store.ApiResult
import com.art.domain.store.ProductDto
import com.art.lloyds.R
import com.art.lloyds.ui.custom.CustomCard
import com.art.lloyds.ui.custom.CustomResultContainer
import com.art.lloyds.ui.custom.CustomTextButton
import com.art.lloyds.ui.custom.CustomSizes.Companion.COMMON_PADDING

@Composable
fun DetailScreen(viewModel: DetailViewModel) {
    val state by viewModel.uiState.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        verticalArrangement = Arrangement.Top
    ) {
        CustomTextButton(
            text = stringResource(id = R.string.button_back),
            onClick = { viewModel.setEvent(DetailEvent.OnClickBack) }
        )
        CustomResultContainer(
            indicatorVisibility = state.indicatorVisibility,
            apiResult = state.apiResult,
            content = {
                Product(
                    productDto = (state.apiResult as ApiResult.Success).data
                )
            }
        )
    }
}

@Composable
private fun Product(
    productDto: ProductDto,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        CustomCard {
            Column(
                modifier = Modifier
                    .padding(all = COMMON_PADDING)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = productDto.title,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                )
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.primary
                )
                ParameterRow(
                    resId = R.string.product_id,
                    text = productDto.id.toString()
                )
                ParameterRow(
                    resId = R.string.product_category,
                    text = productDto.category
                )
                ParameterRow(
                    resId = R.string.product_price,
                    text = productDto.price.toString()
                )
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.primary
                )
                AsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = COMMON_PADDING),
                    model = productDto.image,
                    contentDescription = "",
                    placeholder = painterResource(id = R.drawable.placeholder),
                    contentScale = ContentScale.Inside
                )
            }
        }
    }
}

@Composable
fun ParameterRow(
    resId: Int,
    text: String,
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier.weight(1.0f),
            text = stringResource(resId)
        )
        Text(
            modifier = Modifier.weight(2.0f),
            text = text
        )
    }
}