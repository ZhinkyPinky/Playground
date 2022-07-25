package com.je.playground.ui.tasks.components.shared

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun MainContentComponent(
    content : List<@Composable () -> Unit>
) {
    content.forEach { it() }
}