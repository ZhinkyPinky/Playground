package com.je.playground.ui.sharedcomponents

import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.je.playground.R

/**
 * Component used to show the priority of a task represented as a colored dot.
 * 0 = High(Red), 1 = Medium(OrangeYellow), 2 = Low(Green)
 */
@Composable
fun PriorityIconComponent(
    priority : Int?,
    modifier : Modifier
) {
    Icon(
        imageVector = Icons.Filled.Circle,
        contentDescription = when (priority) {
            0 -> stringResource(R.string.high_priority)
            1 -> stringResource(R.string.medium_priority)
            2 -> stringResource(R.string.low_priority)
            else -> stringResource(R.string.no_priority_set)
        },
        tint = when (priority) {
            0 -> Color.Red
            1 -> Color(0xFFFFAB00)
            2 -> MaterialTheme.colors.onPrimary
            else -> Color.Transparent
        },
        modifier = modifier
            .size(8.dp)
    )
}