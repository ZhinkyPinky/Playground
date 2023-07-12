package com.je.playground.ui.taskeditor

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import com.je.playground.ui.theme.regularText

@OptIn(ExperimentalComposeUiApi::class)
@Composable fun TextFieldComponent(
    labelText : String,
    value : String,
    isSingleLine : Boolean,
    onValueChange : (String) -> Unit,
    modifier : Modifier? = Modifier
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    TextField(
        value = value,
        onValueChange = {
            onValueChange(it)
        },
        label = { Text(text = labelText) },
        textStyle = regularText(MaterialTheme.colors.secondary),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
                keyboardController?.hide()
            }
        ),
        colors = TextFieldDefaults.textFieldColors(
            focusedLabelColor = MaterialTheme.colors.secondary,
            cursorColor = MaterialTheme.colors.secondaryVariant,
            trailingIconColor = MaterialTheme.colors.secondary,
            unfocusedIndicatorColor = Color.Transparent,
            backgroundColor = MaterialTheme.colors.background
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
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(
                start = 4.dp,
                end = 6.dp,
            )
            .then(modifier ?: Modifier)
    )
}

