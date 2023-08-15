package com.je.playground.ui.tasklist.components.deprecated.shared

import androidx.compose.runtime.Composable

@Composable
fun MainContentComponent(
    content : List<@Composable () -> Unit>
) {
    content.forEach { it() }
}