package com.je.playground.designsystem.datetimepickers

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import com.je.playground.feature.utility.toEpochMilliseconds
import com.je.playground.feature.utility.toLocalDate
import com.je.playground.ui.theme.PlaygroundTheme
import com.je.playground.ui.theme.ThemePreviews
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(
    currentDate: LocalDate?,
    onDateSelected: (LocalDate?) -> Unit, onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = currentDate?.toEpochMilliseconds()
    )

    androidx.compose.material3.DatePickerDialog(onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis?.toLocalDate())
            }) {
                Text(text = "OK")
            }
        }, dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(text = "Avbryt")
            }
        }) {
        androidx.compose.material3.DatePicker(state = datePickerState)
    }
}

@ThemePreviews
@Composable
fun DatePickerDialogPreview() {
    PlaygroundTheme() {
        DatePickerDialog(
            currentDate = LocalDate.now(),
            onDateSelected = { _ -> }, onDismiss = {})
    }
}
