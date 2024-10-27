package com.je.playground.designsystem.component.task.taskeditor

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.je.playground.ui.theme.ThemePreviews
import com.je.playground.ui.theme.PlaygroundTheme
import com.je.playground.feature.tasks.editor.TaskEditorEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorTopBar(
    text: String,
    onEvent: (TaskEditorEvent) -> Unit,
    onBackPress: () -> Unit
) {
    Surface (modifier = Modifier.padding(bottom = 1.dp)){
        TopAppBar(
            title = { Text(text = text) },
            navigationIcon = {
                IconButton(onClick = onBackPress) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                    )
                }
            },
            actions = {
                IconButton(onClick = { onEvent(TaskEditorEvent.Save) }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Save,
                        contentDescription = "Save",
                    )
                }
            },
            modifier = Modifier.padding(
                start = 16.dp,
                end = 16.dp
            )
        )
    }
}

@ThemePreviews
@Composable
fun EditorTopBarPreview() {
    PlaygroundTheme {
        EditorTopBar(
            text = "Edit",
            onEvent = { /*TODO*/ }) {

        }
    }
}