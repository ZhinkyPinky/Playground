package com.je.playground.ui.sharedcomponents

import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun CheckboxComponent(
    isChecked : Boolean,
    modifier : Modifier = Modifier,
    onCheckedChange : (Boolean) -> Unit,
) {
    Checkbox(
        checked = isChecked,
        onCheckedChange = onCheckedChange,
        colors = CheckboxDefaults.colors(
            checkedColor = Color.Transparent,
            checkmarkColor = MaterialTheme.colorScheme.onPrimary,
            uncheckedColor = MaterialTheme.colorScheme.onPrimary
        ),
        modifier = Modifier.then(modifier)
    )
}