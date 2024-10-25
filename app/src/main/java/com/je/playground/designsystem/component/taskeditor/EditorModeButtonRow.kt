package com.je.playground.designsystem.component.taskeditor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.je.playground.designsystem.theme.ThemePreviews
import com.je.playground.designsystem.theme.PlaygroundTheme

@Composable
fun EditorModeButtonRow(
    isGroup : Boolean,
    onClick : () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .height(IntrinsicSize.Max)
            .fillMaxWidth()
    ) {
        Button(
            onClick = onClick,
            enabled = isGroup,
            shape = MaterialTheme.shapes.small.copy(CornerSize(0)),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isGroup) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            ),
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        ) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = "Single",
                tint = if (isGroup) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.onSecondaryContainer,
            )
            Text(
                text = "Single"
            )
        }

        Button(
            onClick = onClick,
            enabled = !isGroup,
            shape = MaterialTheme.shapes.small.copy(CornerSize(0)),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (!isGroup) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            ),
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        ) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = "Group",
                tint = if (isGroup) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.primaryContainer,
            )
            Text(text = "Group")
        }
    }
}

@ThemePreviews
@Composable
fun EditorModeButtonRowSinglePreview() {
    PlaygroundTheme {
        EditorModeButtonRow(isGroup = false) {

        }
    }
}

@ThemePreviews
@Composable
fun EditorModeButtonRowGroupPreview() {
    PlaygroundTheme {
        EditorModeButtonRow(isGroup = true) {

        }
    }
}