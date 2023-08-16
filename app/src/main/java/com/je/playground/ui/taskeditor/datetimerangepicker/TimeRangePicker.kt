package com.je.playground.ui.taskeditor.datetimerangepicker

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.je.playground.ui.theme.regularText
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
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
    ) {
        Row {
            TextField(
                value = startTime?.toString() ?: "",
                onValueChange = { },
                label = { Text(text = "Start Time") },
                enabled = false,
                textStyle = regularText(MaterialTheme.colorScheme.onPrimary),
                colors = TextFieldDefaults.colors(
                    disabledLabelColor = MaterialTheme.colorScheme.onPrimary,
                    focusedLabelColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onPrimary,
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
                colors = TextFieldDefaults.colors(
                    disabledLabelColor = if (startTime != null) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondary,
                    focusedLabelColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onPrimary,
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

            if (startTime != null) {
                IconButton(onClick = {
                    onStartTimeValueChange(null)
                    onEndTimeValueChange(null)
                }) {
                    Icon(
                        imageVector = Icons.Filled.Clear,
                        contentDescription = "Clear date selection"
                    )
                }
            }
        }

        if (showStartTimeSelection) {
            AlertDialog(
                onDismissRequest = { showStartTimeSelection = !showStartTimeSelection },
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .wrapContentSize()
            ) {
                TimeSelectionComponent(
                    startTime = startTime,
                    endTime = endTime,
                    startTimeOrEndTime = true,
                    onSave = {
                        showStartTimeSelection = !showStartTimeSelection
                        onStartTimeValueChange(it)
                    },
                    onCancel = { showStartTimeSelection = !showStartTimeSelection }
                )
            }
        }
    }

    if (showEndTimeSelection) {
        AlertDialog(
            onDismissRequest = { showEndTimeSelection = !showEndTimeSelection },
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .wrapContentSize()
        ) {
            TimeSelectionComponent(
                startTime = startTime,
                endTime = endTime,
                startTimeOrEndTime = false,
                onSave = {
                    showEndTimeSelection = !showEndTimeSelection
                    onEndTimeValueChange(it)
                },
                onCancel = { showEndTimeSelection = !showEndTimeSelection }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable fun TimeSelectionComponent(
    startTime : LocalTime?,
    endTime : LocalTime?,
    startTimeOrEndTime : Boolean,
    onSave : (LocalTime) -> Unit,
    onCancel : () -> Unit
) {
    val state = rememberTimePickerState(
        initialHour = if (startTimeOrEndTime && startTime != null) {
            startTime.hour
        } else if (endTime != null) {
            endTime.hour
        } else {
            0
        },

        initialMinute = if (startTimeOrEndTime && startTime != null) {
            startTime.minute
        } else if (endTime != null) {
            endTime.minute
        } else {
            0
        },
    )

    Column {
        TimePicker(
            state = state,
            colors = TimePickerDefaults.colors(
                clockDialColor = MaterialTheme.colorScheme.secondary,
                clockDialSelectedContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                clockDialUnselectedContentColor = MaterialTheme.colorScheme.onSecondaryContainer,

                containerColor = MaterialTheme.colorScheme.primaryContainer,

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
                    if (startTimeOrEndTime) {
                        startTime?.let { onSave(it) }
                    } else {
                        endTime?.let { onSave(it) }
                    }
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



