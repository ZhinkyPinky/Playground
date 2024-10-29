package com.je.playground.designsystem.datetimepickers

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.je.playground.ui.theme.PlaygroundTheme
import com.je.playground.ui.theme.ThemePreviews
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit


@Composable
fun DateTimePicker(
    leadingIcon: @Composable() (() -> Unit)? = {
        Icon(imageVector = Icons.Filled.DateRange, contentDescription = "")
    },
    dateLabel: String,
    timeLabel: String,
    date: LocalDate?,
    time: LocalTime?,
    dateRequired: Boolean = false,
    onDateSelected: (LocalDate?) -> Unit,
    onTimeSelected: (LocalTime) -> Unit
) {

    val showDatePickerDialog = rememberSaveable { mutableStateOf(false) }
    val showTimePickerDialog = rememberSaveable { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(16.dp)
    ) {
        leadingIcon?.let { it() }

        Box(modifier = Modifier
            .weight(1f)
            .clickable { showDatePickerDialog.value = true }) {
            Text(
                text = date?.toString() ?: dateLabel,
                color = if (date != null) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        if ((dateRequired && (date != null)) || (!dateRequired)) {
            Box(
                modifier = Modifier.clickable { showTimePickerDialog.value = true }) {
                Text(
                    text = time?.toString() ?: timeLabel,
                    color = if (time != null) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }

    if (showDatePickerDialog.value) {
        DatePickerDialog(
            currentDate = date,
            onDateSelected = {
                onDateSelected(it)
                showDatePickerDialog.value = false
            },

            onDismiss = { showDatePickerDialog.value = false }
        )

    } else if (showTimePickerDialog.value) {
        TimePickerDialog(
            currentSelectedTime = time,
            onTimeSelected = {
                onTimeSelected(it)
                showTimePickerDialog.value = false
            },
            onDismissRequest = { showTimePickerDialog.value = false }
        )
    }
}


@ThemePreviews
@Composable
fun DateTimePickerEmptyPreview() {
    PlaygroundTheme {
        Surface {
            Column {
                HorizontalDivider(modifier = Modifier.padding(top = 16.dp))

                DateTimePicker(
                    dateLabel = "Select a date",
                    timeLabel = "Select a time",
                    date = null,
                    time = null,
                    onDateSelected = { _ -> },
                    onTimeSelected = { _ -> }
                )

                HorizontalDivider(modifier = Modifier.padding(bottom = 16.dp))

            }
        }
    }
}

@ThemePreviews
@Composable
fun DateTimePickerEmptyAndDateRequiredPreview() {
    PlaygroundTheme {
        Surface {
            Column {
                HorizontalDivider(modifier = Modifier.padding(top = 16.dp))

                DateTimePicker(
                    dateLabel = "Select a date",
                    timeLabel = "Select a time",
                    date = null,
                    time = null,
                    dateRequired = true,
                    onDateSelected = { _ -> },
                    onTimeSelected = { _ -> }
                )

                HorizontalDivider(modifier = Modifier.padding(bottom = 16.dp))

            }
        }
    }
}

@ThemePreviews
@Composable
fun DateTimePickerDateSelectedPreview() {
    PlaygroundTheme {
        Surface {
            Column {
                HorizontalDivider(modifier = Modifier.padding(top = 16.dp))

                DateTimePicker(
                    dateLabel = "Select a date",
                    timeLabel = "Select a time",
                    date = LocalDate.now(),
                    time = null,
                    onDateSelected = { _ -> },
                    onTimeSelected = { _ -> }
                )

                HorizontalDivider(modifier = Modifier.padding(bottom = 16.dp))

            }
        }
    }
}


@ThemePreviews
@Composable
fun DateTimePickerDateAndTimeSelectedPreview() {
    PlaygroundTheme {
        Surface {
            Column {
                HorizontalDivider(modifier = Modifier.padding(top = 16.dp))

                DateTimePicker(
                    dateLabel = "Select a date",
                    timeLabel = "Select a time",
                    date = LocalDate.now(),
                    time = LocalTime.now().truncatedTo(ChronoUnit.MINUTES),
                    onDateSelected = { _ -> },
                    onTimeSelected = { _ -> }
                )

                HorizontalDivider(modifier = Modifier.padding(bottom = 16.dp))

            }
        }
    }
}
