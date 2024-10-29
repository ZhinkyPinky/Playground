package com.je.playground.designsystem.datetimepickers

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import com.je.playground.ui.theme.PlaygroundTheme
import com.je.playground.ui.theme.ThemePreviews
import java.time.LocalTime

@Composable
fun TimePicker(
    modifier: Modifier = Modifier,
    label: String,
    time: LocalTime?,
    onTimeSelected: (LocalTime) -> Unit
) {

    val showDialog = rememberSaveable { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    if (showDialog.value) {
        TimePickerDialog(
            currentSelectedTime = time,
            onTimeSelected = {
                onTimeSelected(it)
                showDialog.value = false
            }, onDismissRequest = {
                showDialog.value = false
            })
    }
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.then(modifier)) {
        IconButton(
            onClick = {
                showDialog.value = true
                focusRequester.requestFocus()
            },
        ) { Icon(imageVector = Icons.Filled.AccessTime, contentDescription = "") }

        OutlinedTextField(
            value = time?.toString() ?: "",
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
    }
}


@ThemePreviews
@Composable
fun TimePickerEmptyPreview() {
    PlaygroundTheme {
        Surface {
            TimePicker(
                label = "Time",
                time = null,
                onTimeSelected = {}
            )
        }
    }
}

@ThemePreviews
@Composable
fun TimePickerFilledPreview() {
    PlaygroundTheme {
        Surface {
            TimePicker(
                label = "Time",
                time = LocalTime.now(),
                onTimeSelected = {}
            )
        }
    }
}
