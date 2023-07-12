package com.je.playground.ui.taskeditor

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import com.je.playground.ui.tasklist.viewmodel.TaskType
import com.je.playground.ui.theme.regularText

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TaskTypeComponent(
    taskType : String,
    onValueChange : (String) -> Unit
) {
    var expanded by rememberSaveable {
        mutableStateOf(false)
    }

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Row(verticalAlignment = Alignment.CenterVertically) {
        TextFieldComponent(
            labelText = "Type",
            value = taskType,
            isSingleLine = true,
            onValueChange = {
                onValueChange(it)
            },
            modifier = Modifier.onFocusChanged { /* TODO: Make consistent. */
                expanded = it.isFocused
            }
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
                            taskType,
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