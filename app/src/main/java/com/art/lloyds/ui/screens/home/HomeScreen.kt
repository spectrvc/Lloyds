package com.art.lloyds.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import coil.compose.AsyncImage
import com.art.domain.store.ApiResult
import com.art.domain.store.ProductDto
import com.art.lloyds.R
import com.art.lloyds.ui.custom.CustomTextButton
import com.art.lloyds.ui.custom.CustomCard
import com.art.lloyds.ui.custom.CustomResultContainer
import com.art.lloyds.ui.custom.CustomSizes.Companion.COMMON_PADDING

@Composable
fun HomeScreen(viewModel: HomeViewModel){
    val state by viewModel.uiState.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        verticalArrangement = Arrangement.Top
    ) {
        CustomTextButton(
            text = stringResource(id = R.string.button_get_product_list),
            onClick = { viewModel.setEvent(HomeEvent.OnClickGetProductList) }
        )
        CustomResultContainer(
            indicatorVisibility = state.indicatorVisibility,
            apiResult = state.apiResult,
            content = {
                ProductList(
                    viewModel = viewModel,
                    productList = (state.apiResult as ApiResult.Success).data
                )
            }

        )
    }
}

@Composable
private fun ProductList(
    viewModel: HomeViewModel,
    productList: List<ProductDto>
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(productList) { dto ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                CustomCard {
                    Product(
                        viewModel = viewModel,
                        productDto = dto,
                    )
                }
            }
        }
    }
}

@Composable
private fun Product(
    viewModel: HomeViewModel,
    productDto: ProductDto
) {
    Row(
        modifier = Modifier
            .padding(COMMON_PADDING)
            .clickable { viewModel.setEvent( HomeEvent.OnClickListItem(productDto.id)) },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top,
    ) {
        Column(
            modifier = Modifier
                .weight(2.0f)
                .padding(end = COMMON_PADDING),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text(text = productDto.title)
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary
            )
            Text(text = stringResource(id = R.string.product_price) + ": " + productDto.price)
        }
        AsyncImage(
            model = productDto.image,
            contentDescription = "",
            placeholder = painterResource(id = R.drawable.placeholder),
            modifier = Modifier.weight(1.0f),
            contentScale = ContentScale.Inside
        )
    }
}



