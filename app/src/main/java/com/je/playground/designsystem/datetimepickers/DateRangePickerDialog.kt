package com.je.playground.designsystem.datetimepickers

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import com.je.playground.ui.theme.PlaygroundTheme
import com.je.playground.ui.theme.ThemePreviews
import java.time.LocalDate
import java.time.ZoneId

@Composable
fun DateRangePicker(
    label: String, date: LocalDate?,
    onDateSelected: (Long?) -> Unit
) {
    val showDialog = rememberSaveable { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    if (showDialog.value) {
        DateRangePickerDialog(
            currentDate = date,
            onDateSelected = {
                onDateSelected(it)
                showDialog.value = false
            }, onDismiss = {
                showDialog.value = false
            })
    }

    Row {
        IconButton(
            onClick = {
                showDialog.value = true
                focusRequester.requestFocus()
            },
        ) { Icon(imageVector = Icons.Filled.DateRange, contentDescription = "") }
    }

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
            .padding(start = 12.dp, end = 12.dp, top = 6.dp, bottom = 6.dp)
            .focusRequester(focusRequester)
    )
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangePickerDialog(
    currentDate: LocalDate?,
    onDateSelected: (Long?) -> Unit, onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = currentDate?.plusDays(1)?.atStartOfDay(ZoneId.systemDefault())
            ?.toInstant()?.toEpochMilli()
    )

    androidx.compose.material3.DatePickerDialog(onDismissRequest = onDismiss, confirmButton = {
        TextButton(onClick = {
            onDateSelected(datePickerState.selectedDateMillis)
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
fun DateRangePickerDialogPreview() {
    PlaygroundTheme {
        DateRangePickerDialog(
            currentDate = LocalDate.now(),
            onDateSelected = { _ -> }, onDismiss = {})
    }
}
