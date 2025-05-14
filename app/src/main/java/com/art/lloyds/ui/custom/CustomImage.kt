package com.art.lloyds.ui.custom

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.Dp
import com.art.lloyds.ui.custom.CustomSizes.Companion.COMMON_HEIGHT

@Composable
fun CustomImage(
    modifier: Modifier = Modifier,
    imageId: Int,
    size: Dp = COMMON_HEIGHT,
    contentDescription: String = "",
) {
    Image(
        bitmap = ImageBitmap.imageResource(
            LocalContext.current.resources,
            imageId
        ),
        contentDescription = contentDescription,
        modifier = modifier.size(size)
    )
}
