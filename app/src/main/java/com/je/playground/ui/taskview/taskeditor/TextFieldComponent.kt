package com.je.playground.ui.taskview.taskeditor

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp

@Composable fun TextFieldComponent(
    label : String,
    placeholder : String,
    value : String,
    isSingleLine : Boolean,
    onValueChange : (String) -> Unit,
    modifier : Modifier? = Modifier
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        value = value,
        onValueChange = {
            onValueChange(it)
        },
        label = { Text(text = label) },
        placeholder = { Text(text = placeholder) },
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
                keyboardController?.hide()
            }
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
            unfocusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
            disabledTextColor = MaterialTheme.colorScheme.onSecondaryContainer,
            errorTextColor = Color.Red,

            focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            disabledContainerColor = MaterialTheme.colorScheme.primaryContainer,
            errorContainerColor = MaterialTheme.colorScheme.primaryContainer,

            cursorColor = MaterialTheme.colorScheme.onPrimaryContainer,
            errorCursorColor = Color.Red,

            focusedBorderColor = MaterialTheme.colorScheme.onSecondaryContainer,
            unfocusedBorderColor = MaterialTheme.colorScheme.background,
            errorBorderColor = Color.Red,

            focusedLabelColor = MaterialTheme.colorScheme.onSecondaryContainer,
            unfocusedLabelColor = MaterialTheme.colorScheme.onSecondaryContainer,
            errorLabelColor = Color.Red,

            focusedPlaceholderColor = MaterialTheme.colorScheme.onSecondaryContainer,
            unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSecondaryContainer,
            disabledPlaceholderColor = MaterialTheme.colorScheme.onSecondaryContainer,

            focusedTrailingIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
            unfocusedTrailingIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
        ),
        singleLine = isSingleLine,
        trailingIcon = {
            if (value != "") {
                Icon(
                    imageVector = Icons.Filled.Clear,
                    contentDescription = "Clear text-field.",
                    modifier = Modifier
                        .size(16.dp)
                        .clickable {
                            onValueChange("")
                        }
                )
            }
        },
        shape = RectangleShape,
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .then(modifier ?: Modifier)
    )
}

