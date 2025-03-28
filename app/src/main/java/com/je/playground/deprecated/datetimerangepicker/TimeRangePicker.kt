package com.je.playground.deprecated.datetimerangepicker

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.je.playground.ui.theme.ThemePreviews
import com.je.playground.ui.theme.PlaygroundTheme
import com.je.playground.ui.theme.regularText
import java.time.LocalTime

@Composable
fun TimeRangePicker(
    startTime : LocalTime?,
    endTime : LocalTime?,
    onStartTimeValueChange : (LocalTime?) -> Unit,
    onEndTimeValueChange : (LocalTime?) -> Unit
) {
    var showStartTimeSelection by rememberSaveable { mutableStateOf(false) }
    var showEndTimeSelection by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(
                top = 6.dp,
                bottom = 6.dp
            )
    ) {
        Row(
            modifier = Modifier.border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.background,
                shape = RectangleShape
            )
        ) {
            TextField(
                value = startTime?.toString() ?: "",
                onValueChange = { },
                label = { Text(text = "Start Time") },
                enabled = false,
                textStyle = regularText(MaterialTheme.colorScheme.onPrimary),
                colors = TextFieldDefaults.colors(
                    disabledLabelColor = MaterialTheme.colorScheme.onSecondary,
                    focusedLabelColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSecondary,
                    cursorColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer
                ),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clickable { showStartTimeSelection = !showStartTimeSelection }
            )

            TextField(
                value = endTime?.toString() ?: "",
                onValueChange = { },
                label = { Text(text = "End Time") },
                enabled = false,
                textStyle = regularText(MaterialTheme.colorScheme.onPrimary),
                trailingIcon = {
                    if (startTime != null) {
                        Icon(
                            imageVector = Icons.Filled.Clear,
                            contentDescription = "Clear text-field.",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier
                                .size(16.dp)
                                .clickable {
                                    onStartTimeValueChange(null)
                                    onEndTimeValueChange(null)
                                }
                        )
                    }
                },
                colors = TextFieldDefaults.colors(
                    disabledLabelColor = if (startTime != null) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.background,
                    focusedLabelColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSecondary,
                    cursorColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer
                ),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clickable {
                        if (startTime != null) {
                            showEndTimeSelection = !showEndTimeSelection
                        }
                    }
            )
        }

        if (showStartTimeSelection) {
            TimeSelectionDialog(
                title = "Select start time",
                startTime = startTime,
                endTime = endTime,
                onSave = {
                    showStartTimeSelection = !showStartTimeSelection
                    onStartTimeValueChange(it)
                },
                onCancel = { showStartTimeSelection = !showStartTimeSelection }
            )
        }
    }

    if (showEndTimeSelection) {
        TimeSelectionDialog(
            title = "Select end time",
            startTime = startTime,
            endTime = endTime,
            onSave = {
                showEndTimeSelection = !showEndTimeSelection
                onEndTimeValueChange(it)
            },
            onCancel = { showEndTimeSelection = !showEndTimeSelection }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeSelectionDialog(
    title : String,
    startTime : LocalTime?,
    endTime : LocalTime?,
    onSave : (LocalTime) -> Unit,
    onCancel : () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onCancel,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
            .wrapContentSize()
    ) {
        TimeSelectionComponent(
            title = title,
            startTime = startTime,
            endTime = endTime,
            startTimeOrEndTime = false,
            onSave = onSave,
            onCancel = onCancel
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable fun TimeSelectionComponent(
    title : String,
    startTime : LocalTime?,
    endTime : LocalTime?,
    startTimeOrEndTime : Boolean,
    onSave : (LocalTime) -> Unit,
    onCancel : () -> Unit
) {
    val state = rememberTimePickerState(
        initialHour = if (startTimeOrEndTime && startTime != null) {
            startTime.hour
        } else endTime?.hour ?: 0,

        initialMinute = if (startTimeOrEndTime && startTime != null) {
            startTime.minute
        } else endTime?.minute ?: 0,
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 12.dp,
                    top = 12.dp
                )
        )

        TimePicker(
            state = state,
            colors = TimePickerDefaults.colors(
                clockDialColor = MaterialTheme.colorScheme.secondaryContainer,
                clockDialSelectedContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                clockDialUnselectedContentColor = MaterialTheme.colorScheme.onSecondaryContainer,

                containerColor = MaterialTheme.colorScheme.primaryContainer,
                selectorColor = MaterialTheme.colorScheme.primaryContainer,

                periodSelectorBorderColor = MaterialTheme.colorScheme.onPrimaryContainer,
                periodSelectorSelectedContainerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                periodSelectorUnselectedContainerColor = MaterialTheme.colorScheme.onSecondaryContainer,
                periodSelectorSelectedContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                periodSelectorUnselectedContentColor = MaterialTheme.colorScheme.onSecondaryContainer,

                timeSelectorSelectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                timeSelectorUnselectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                timeSelectorSelectedContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                timeSelectorUnselectedContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            )
        )

        Row {
            TextButton(
                onClick = {
                    onSave(
                        LocalTime.of(
                            state.hour,
                            state.minute
                        )
                    )
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "Save")
            }

            TextButton(
                onClick = { onCancel() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "Cancel")
            }
        }
    }
}

@ThemePreviews
@Composable
fun TimeRangePickerPreview() {
    PlaygroundTheme {
        TimeRangePicker(
            startTime = LocalTime.of(
                LocalTime.now().hour,
                LocalTime.now().minute
            ),
            endTime = LocalTime.of(
                LocalTime
                    .now()
                    .plusHours(10).hour,
                LocalTime.now().minute
            ),
            onStartTimeValueChange = {},
            onEndTimeValueChange = {}
        )
    }
}



