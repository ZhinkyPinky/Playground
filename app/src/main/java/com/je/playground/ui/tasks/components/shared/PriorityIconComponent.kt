package com.je.playground.ui.tasks.components.shared

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.je.playground.R

@Composable
fun PriorityIconComponent(
    priority : Int?,
    modifier : Modifier
) {
    Icon(
        imageVector = Icons.Filled.Circle,
        contentDescription = when (priority) {
            1 -> stringResource(R.string.high_priority)
            2 -> stringResource(R.string.medium_priority)
            3 -> stringResource(R.string.low_priority)
            else -> stringResource(R.string.no_priority_set)
        },
        tint = when (priority) {
            1 -> Color.Red
            2 -> Color(0xFFFFAB00)
            3 -> Color.Green
            else -> Color.Transparent
        },
        modifier = modifier
            .size(8.dp)

    )
}