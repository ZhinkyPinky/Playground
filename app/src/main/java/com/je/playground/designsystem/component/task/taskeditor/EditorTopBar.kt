package com.je.playground.designsystem.component.task.taskeditor

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.je.playground.ui.theme.PlaygroundTheme
import com.je.playground.ui.theme.ThemePreviews

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorTopBar(
    text: String,
    showDivider: Boolean = false,
    onAction: () -> Unit,
    onNavigation: () -> Unit
) {
    Surface {
        Column {
            TopAppBar(
                title = { Text(text = text) },
                navigationIcon = {
                    IconButton(onClick = onNavigation) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Cancel",
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onAction) {
                        Icon(
                            imageVector = Icons.Filled.Save,
                            contentDescription = "Save",
                        )
                    }
                },
                modifier = Modifier.padding(
                    end = 16.dp
                )
            )

            if (showDivider) HorizontalDivider()
        }
    }
}

@ThemePreviews
@Composable
fun EditorTopBarPreview() {
    PlaygroundTheme {
        EditorTopBar(
            text = "Edit",
            onAction = {},
            onNavigation = {}
        )
    }
}