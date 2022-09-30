package com.je.playground.ui.taskeditor

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import com.je.playground.ui.theme.regularText

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TitleTextFieldComponent(
    onValueChange : (String) -> Unit
) {
    var title by remember {
        mutableStateOf("")
    }

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Column {
        TextField(
            value = title,
            onValueChange = {
                title = it
                onValueChange(it)
            },
            label = { Text(text = "Title*") },
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
                unfocusedIndicatorColor = Color.Transparent,
                backgroundColor = MaterialTheme.colors.background
            ),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
        )
    }
}