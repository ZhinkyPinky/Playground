package com.je.playground.ui.tasklist.components.shared

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun SubContentComponent(
    content : List<@Composable () -> Unit>
) {
    Column {
        content.forEach { it() }
    }

    Divider(
        color = MaterialTheme.colors.primaryVariant,
        thickness = 4.dp
    )
}