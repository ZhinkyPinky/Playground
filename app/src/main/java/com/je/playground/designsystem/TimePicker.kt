package com.je.playground.designsystem

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.je.playground.R
import com.je.playground.ui.theme.PlaygroundTheme
import com.je.playground.ui.theme.ThemePreviews
import java.time.LocalTime

@Composable
fun TimePicker(
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
    Row(verticalAlignment = Alignment.CenterVertically) {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    currentSelectedTime: LocalTime?,
    onTimeSelected: (LocalTime) -> Unit,
    onDismissRequest: () -> Unit
) {
    val state = rememberTimePickerState(
        initialHour = currentSelectedTime?.hour ?: 0,
        initialMinute = currentSelectedTime?.minute ?: 0,
    )

    TimePickerDialog(
        onDismissRequest = {},
        confirmButton = {
            TextButton(
                onClick = { onTimeSelected(LocalTime.of(state.hour, state.minute)) }
            ) {
                Text(text = stringResource(R.string.ok))
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismissRequest
            ) {
                Text(text = stringResource(R.string.cancel))
            }
        },
    ) {
        androidx.compose.material3.TimePicker(
            state = state
        )
    }
}

@Composable
fun TimePickerDialog(
    title: String = "Select Time",
    onDismissRequest: () -> Unit,
    confirmButton: @Composable (() -> Unit),
    dismissButton: @Composable (() -> Unit)? = null,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    content: @Composable () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
    ) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = 6.dp,
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .height(IntrinsicSize.Min)
                .background(
                    shape = MaterialTheme.shapes.extraLarge, color = containerColor
                ),
            color = containerColor
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    text = title,
                    style = MaterialTheme.typography.labelMedium
                )
                content()
                Row(
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    dismissButton?.invoke()
                    confirmButton()
                }
            }
        }
    }
}

@ThemePreviews
@Composable
fun TimerPickerDialogPreview() {
    PlaygroundTheme {
        TimePickerDialog(
            currentSelectedTime = LocalTime.now(),
            onTimeSelected = {},
            onDismissRequest = {}
        )
    }
}