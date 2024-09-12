package com.je.playground.designsystem.component.taskeditor

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.je.playground.designsystem.ThemePreviews
import com.je.playground.designsystem.theme.PlaygroundTheme
import com.je.playground.feature.tasks.editor.TaskEditorEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorTopBar(
    text : String,
    onEvent : (TaskEditorEvent) -> Unit,
    onBackPress : () -> Unit
) {
    Column {
        TopAppBar(
            title = {
                Text(
                    text = text,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 22.sp,
                    maxLines = 1,
                    textAlign = TextAlign.Start,

                    modifier = Modifier
                        .padding(
                            start = 0.dp,
                            top = 4.dp,
                            end = 8.dp,
                            bottom = 4.dp
                        )
                        .weight(1f)
                )
            },
            navigationIcon = {
                IconButton(onClick = onBackPress) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.padding(end = 2.dp)
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            actions = {
                IconButton(
                    onClick = {
                        onEvent(TaskEditorEvent.SaveTask)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Save,
                        contentDescription = "Save",
                        tint = MaterialTheme.colorScheme.onPrimary,
                    )
                }
            })
        Divider(
            color = MaterialTheme.colorScheme.background
        )
    }
}

@ThemePreviews
@Composable
fun EditorTopBarPreview(){
    PlaygroundTheme {
        EditorTopBar(
            text = "Edit",
            onEvent = { /*TODO*/ }) {
            
        }
    }
}