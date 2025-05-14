package com.art.lloyds.ui.custom

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.art.lloyds.ui.custom.CustomSizes.Companion.COMMON_PADDING

@Composable
fun CustomIndicator(
    visibility: Boolean = false,
) {
    if (visibility) {
        val modifier = Modifier.fillMaxSize()
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier.background(Color.Transparent)
        ) {
            CircularProgressIndicator(
                modifier = Modifier.padding(COMMON_PADDING),
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}



