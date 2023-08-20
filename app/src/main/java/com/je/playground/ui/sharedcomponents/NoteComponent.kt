package com.je.playground.ui.sharedcomponents

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import com.je.playground.ui.theme.regularText

@Composable
fun NoteComponent(
    note : String,
    isExpanded : Boolean,
    modifier : Modifier = Modifier
) {
    Column {
        Text(
            text = note,
            style = regularText(MaterialTheme.colorScheme.onPrimary),
            overflow = TextOverflow.Ellipsis,
            maxLines = if (isExpanded) Int.MAX_VALUE else 1,
            modifier = modifier
        )
    }
}