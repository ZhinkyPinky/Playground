package com.je.playground.ui.sharedcomponents

import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color

@Composable
fun CheckboxComponent(
    isChecked : Boolean,
    onCheckedChange : (Boolean) -> Unit
) {
    Checkbox(
        checked = isChecked,
        onCheckedChange = onCheckedChange,
        colors = CheckboxDefaults.colors(
            checkedColor = Color.Transparent,
            checkmarkColor = MaterialTheme.colors.secondary,
            uncheckedColor = MaterialTheme.colors.secondary
        )
    )
}