package com.je.playground.designsystem.component.common

import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.je.playground.ui.theme.ThemePreviews
import com.je.playground.ui.theme.PlaygroundTheme

@Composable
fun CheckboxComponent(
    isChecked : Boolean,
    modifier : Modifier = Modifier,
    onCheckedChange : (Boolean) -> Unit,
) {
    Checkbox( //TODO: Tri-state??
        checked = isChecked,
        onCheckedChange = onCheckedChange,
        colors = CheckboxDefaults.colors(
            checkedColor = Color.Transparent,
            checkmarkColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        modifier = Modifier.then(modifier)
    )
}

@ThemePreviews
@Composable
fun CheckboxPreview1(){
    PlaygroundTheme {
        CheckboxComponent(
            isChecked = false,
            onCheckedChange = {}
        )
    }
}

@ThemePreviews
@Composable
fun CheckboxPreview2(){
    PlaygroundTheme {
        CheckboxComponent(
            isChecked = true,
            onCheckedChange = {}
        )
    }
}
