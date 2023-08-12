package com.je.playground.ui.tasklist.components.simpletask

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.je.playground.ui.theme.regularText

@Composable
fun NoteComponent(note : String) {
    Column {
        Text(
            text = note,
            style = regularText(MaterialTheme.colorScheme.onPrimary),
            modifier = Modifier.padding(
                start = 12.dp,
                end = 20.dp,
                top = 8.dp,
                bottom = 8.dp
            )
        )
    }
}