package com.art.lloyds.ui.main.scaffold

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.art.domain.navigation.ScreensEnum
import com.art.lloyds.ui.custom.CustomImage
import com.art.lloyds.ui.custom.CustomSizes.Companion.DRAWER_ICON_SIZE
import com.art.lloyds.ui.custom.CustomSizes.Companion.SMALL_PADDING

data class DrawerItemData(
    val id: ScreensEnum,
    val resId: Int,
    val imageId: Int,
)

@Composable
fun DrawerItem(
    drawerItemData: DrawerItemData,
    modifier: Modifier,
    onItemClick: (DrawerItemData) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick(drawerItemData) }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier.padding(SMALL_PADDING)
        ) {
            CustomImage(
                imageId = drawerItemData.imageId,
                size = DRAWER_ICON_SIZE,
            )
            Text(
                text = stringResource(id = drawerItemData.resId),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(horizontal = SMALL_PADDING)
            )
        }
    }
}