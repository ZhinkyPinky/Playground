package com.je.playground.ui.taskeditor

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.je.playground.ui.tasklist.viewmodel.TaskType
import com.je.playground.ui.theme.regularText

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TaskTypeComponent(
    onValueChange : (String) -> Unit
) {
    var textFieldValue by remember {
        mutableStateOf("")
    }

    var expanded by remember {
        mutableStateOf(false)
    }

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Row(verticalAlignment = Alignment.CenterVertically) {
        TextField(
            value = textFieldValue,
            label = { Text(text = "Type") },
            onValueChange = {
                textFieldValue = it
                onValueChange(it)
            },
            textStyle = regularText(MaterialTheme.colors.secondary),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                }
            ),
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                focusedLabelColor = MaterialTheme.colors.secondary,
                cursorColor = MaterialTheme.colors.secondaryVariant,
                trailingIconColor = MaterialTheme.colors.secondary,
                unfocusedIndicatorColor = Color.Transparent,
                backgroundColor = MaterialTheme.colors.background
            ),
            trailingIcon = {
                Icon(
                    imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = "Show task types",
                    modifier = Modifier.clickable { expanded = !expanded }
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged {
                    /* TODO: Make consistent. */
                    /*
                    if (it.isFocused) {
                        expanded = true
                        val text = textFieldState
                        textFieldState.value = textFieldState.value.copy(
                            selection = TextRange(
                                0,
                                text.length
                            )
                        )
                    } else {
                        expanded = false
                    }
                     */
                }
                .padding(
                    start = 4.dp,
                    end = 6.dp,
                )
                .height(54.dp)
        )
    }

    Divider(
        color = MaterialTheme.colors.primaryVariant,
    )

    if (expanded) {
        Column(
            modifier = Modifier.padding(top = 8.dp)
        ) {
            TaskType
                .values()
                .forEach {
                    if (it.name.contains(
                            textFieldValue,
                            true
                        )
                    )
                        Row {
                            Text(
                                text = it.name,
                                style = regularText(MaterialTheme.colors.secondary),
                                modifier = Modifier
                                    .clickable {
                                        expanded = false
                                        focusManager.clearFocus()
                                        keyboardController?.hide()
                                        textFieldValue = it.name
                                        onValueChange(it.name)
                                    }
                                    .padding(
                                        start = 16.dp,
                                        end = 16.dp,
                                        bottom = 8.dp
                                    )
                            )
                        }
                }

            Divider(color = MaterialTheme.colors.primaryVariant)
        }
    }
}