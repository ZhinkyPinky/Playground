package com.je.playground.designsystem.component.task

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun NoteComponent(
    note : String,
    isExpanded : Boolean,
    modifier : Modifier = Modifier
) {
        Text(
            text = note,
            overflow = TextOverflow.Ellipsis,
            maxLines = if (isExpanded) Int.MAX_VALUE else 1,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium,
            modifier = modifier
        )
}