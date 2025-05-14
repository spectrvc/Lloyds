package com.art.lloyds.ui.custom

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.art.lloyds.ui.custom.CustomSizes.Companion.COMMON_ELEVATION
import com.art.lloyds.ui.custom.CustomSizes.Companion.COMMON_PADDING

@Composable
fun CustomCard(
    content: @Composable () -> Unit,
) {
    Card(
        modifier = Modifier.padding(all = COMMON_PADDING),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(
            defaultElevation = COMMON_ELEVATION
        ),
        colors = CardColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary,
            disabledContainerColor = MaterialTheme.colorScheme.secondary,
            disabledContentColor = MaterialTheme.colorScheme.onSecondary
        )
    ) {
        content()
    }
}



