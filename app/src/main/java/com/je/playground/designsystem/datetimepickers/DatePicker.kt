package com.je.playground.designsystem.datetimepickers

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.je.playground.ui.theme.PlaygroundTheme
import com.je.playground.ui.theme.ThemePreviews
import java.time.LocalDate

@Composable
fun DatePicker(
    modifier: Modifier = Modifier,
    label: String,
    date: LocalDate?,
    onDateSelected: (LocalDate?) -> Unit
) {
    val showDialog = rememberSaveable { mutableStateOf(false) }
    //val focusRequester = remember { FocusRequester() }

    if (showDialog.value) {
        DatePickerDialog(
            currentDate = date,
            onDateSelected = {
                onDateSelected(it)
                showDialog.value = false
            }, onDismiss = {
                showDialog.value = false
            })
    }

    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.then(modifier)) {
        IconButton(
            onClick = {
                showDialog.value = true
                //focusRequester.requestFocus()
            },
        ) { Icon(imageVector = Icons.Filled.DateRange, contentDescription = "") }

        TextButton(onClick = { showDialog.value = true }) {
            Text(text = date?.toString() ?: label)
        }

        /*
        OutlinedTextField(
            value = date?.toString() ?: "",
            onValueChange = { },
            readOnly = true,
            label = { Text(text = label) },
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledBorderColor = MaterialTheme.colorScheme.outline,
                disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,

                //For Icons
                disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
        )

         */
    }
}

@ThemePreviews
@Composable
fun DatePickerEmptyPreview() {
    PlaygroundTheme {
        Surface(modifier = Modifier.fillMaxWidth()) {
            Column {
                HorizontalDivider(modifier = Modifier.padding(top = 16.dp))

                DatePicker(
                    label = "Start",
                    date = null,
                    onDateSelected = {}
                )

                HorizontalDivider(modifier = Modifier.padding(bottom = 16.dp))
            }
        }
    }
}

@ThemePreviews
@Composable
fun DatePickerFilledPreview() {
    PlaygroundTheme {
        Surface(modifier = Modifier.fillMaxWidth()) {
            Column {
                HorizontalDivider(modifier = Modifier.padding(top = 16.dp))

                DatePicker(
                    label = "Date",
                    date = LocalDate.now(),
                    onDateSelected = {}
                )

                HorizontalDivider(modifier = Modifier.padding(bottom = 16.dp))
            }
        }
    }
}
