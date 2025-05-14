package com.art.lloyds.ui.main.scaffold

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DrawerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.art.lloyds.R
import com.art.lloyds.ui.custom.CustomSizes.Companion.COMMON_HEIGHT
import com.art.lloyds.ui.custom.CustomSizes.Companion.SMALL_PADDING
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun DrawerBody(
    scope: CoroutineScope,
    drawerState: DrawerState,
    drawerItemList: List<DrawerItemData>,
    modifier: Modifier = Modifier,
    onItemClick: (DrawerItemData) -> Unit,
) {
    Column(
        modifier = Modifier.background(MaterialTheme.colorScheme.surface)
    ) {
        Box(
            contentAlignment = Alignment.BottomStart,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = SMALL_PADDING)
                .background(MaterialTheme.colorScheme.primary)
                .height(COMMON_HEIGHT)
        ) {
            Text(
                text = stringResource(R.string.app_name),
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(SMALL_PADDING)
            )
        }
        LazyColumn(
            modifier = modifier.weight(1f)
        ) {
            items(drawerItemList) { item ->
                DrawerItem(
                    item,
                    modifier = modifier
                ) {
                    scope.launch {
                        drawerState.close()
                    }
                    onItemClick(item)
                }
            }
        }
    }
}


