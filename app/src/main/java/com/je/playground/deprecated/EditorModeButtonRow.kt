package com.je.playground.deprecated

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.je.playground.ui.theme.ThemePreviews
import com.je.playground.ui.theme.PlaygroundTheme

@Composable
fun EditorModeButtonRow(
    isGroup: Boolean,
    onClick: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .height(IntrinsicSize.Max)
            .fillMaxWidth()
    ) {
        Button(
            onClick = onClick,
            enabled = isGroup,
            shape = MaterialTheme.shapes.small.copy(CornerSize(0)),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isGroup) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSurface,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            ),
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        ) { Text(text = "Single")}

        Button(
            onClick = onClick,
            enabled = !isGroup,
            shape = MaterialTheme.shapes.small.copy(CornerSize(0)),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (!isGroup) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSurface,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            ),
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        ) {
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