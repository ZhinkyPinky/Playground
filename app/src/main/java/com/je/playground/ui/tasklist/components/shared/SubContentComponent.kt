package com.je.playground.ui.tasklist.components.shared

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SubContentComponent(
    content : List<@Composable () -> Unit>
) {
    Column(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
    ) {
        content.forEach { it() }
    }
    /*
    Divider(
        color = MaterialTheme.colors.primaryVariant,
        thickness = 4.dp
    )
     */
}