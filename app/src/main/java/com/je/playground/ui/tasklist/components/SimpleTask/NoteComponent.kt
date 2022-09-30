package com.je.playground.ui.tasklist.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.je.playground.ui.theme.regularText

@Composable
fun NoteComponent(note : String) {
    Column {
        Text(
            text = note,
            style = regularText(MaterialTheme.colors.secondary),
            modifier = Modifier.padding(
                start = 20.dp,
                end = 20.dp,
                top = 8.dp,
                bottom = 8.dp
            )
        )
    }
}