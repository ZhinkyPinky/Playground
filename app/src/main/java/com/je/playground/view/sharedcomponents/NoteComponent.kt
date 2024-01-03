package com.je.playground.view.sharedcomponents

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import com.je.playground.view.theme.regularText

@Composable
fun NoteComponent(
    note : String,
    isExpanded : Boolean,
    modifier : Modifier = Modifier
) {
    Column {
        Text(
            text = note,
            style = regularText(Color(0xFFCCCCCC)),
                //TODO: if(isExpanded) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondary
            overflow = TextOverflow.Ellipsis,
            maxLines = if (isExpanded) Int.MAX_VALUE else 1,
            modifier = modifier
        )
    }
}