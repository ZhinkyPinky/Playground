package com.je.playground.ui.taskview.taskeditor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.je.playground.ui.taskview.taskeditor.datetimerangepicker.DateRangePicker
import com.je.playground.ui.taskview.taskeditor.datetimerangepicker.TimeRangePicker
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun TaskGroupEditorDialog(
    taskGroupTitle : String,
    updateTaskGroupTitle : (String) -> Unit,
    taskGroupNote : String,
    updateTaskGroupNote : (String) -> Unit,
    taskGroupPriority : Int,
    updateTaskGroupPriority : (Int) -> Unit,
    taskGroupStartDate : LocalDate?,
    updateTaskGroupStartDate : (LocalDate) -> Unit,
    taskGroupStartTime : LocalTime?,
    updateTaskGroupStartTime : (LocalTime) -> Unit,
    taskGroupEndDate : LocalDate?,
    updateTaskGroupEndDate : (LocalDate) -> Unit,
    taskGroupEndTime : LocalTime?,
    updateTaskGroupEndTime : (LocalTime) -> Unit,
    onDismissRequest : () -> Unit,
) {
    var title by rememberSaveable { mutableStateOf(taskGroupTitle) }
    var note by rememberSaveable { mutableStateOf(taskGroupNote) }
    var priority by rememberSaveable { mutableIntStateOf(taskGroupPriority) }
    var startDate by rememberSaveable { mutableStateOf(taskGroupStartDate) }
    var startTime by rememberSaveable { mutableStateOf(taskGroupStartTime) }
    var endDate by rememberSaveable { mutableStateOf(taskGroupEndDate) }
    var endTime by rememberSaveable { mutableStateOf(taskGroupEndTime) }

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(dismissOnClickOutside = false)
    ) {
        Surface(
            modifier =
            Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(
                    start = 12.dp,
                    end = 12.dp,
                    top = 12.dp
                )
        ) {
            Column {
                Text(
                    text = "New task group",
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Divider(
                    color = MaterialTheme.colorScheme.background,
                    modifier = Modifier.padding(
                        top = 12.dp,
                        bottom = 6.dp
                    )
                )

                TextFieldComponent(
                    placeHolderText = "Title*",
                    value = title,
                    isSingleLine = true,
                    onValueChange = { title = it }
                )

                NoteEditComponent(
                    note = note,
                    onValueChange = { note = it }
                )

                PrioritySliderComponent(
                    priority = priority,
                    onPriorityChanged = { priority = it },
                    modifier = Modifier.padding(
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 6.dp
                    ),
                )

                DateRangePicker(
                    startDate = startDate,
                    endDate = endDate,
                    onStartDateValueChange = { startDate = it },
                    onEndDateValueChange = { endDate = it },
                )

                TimeRangePicker(
                    startTime = startTime,
                    endTime = endTime,
                    onStartTimeValueChange = { startTime = it },
                    onEndTimeValueChange = {
                        if (startTime?.isBefore(it) == true && startDate?.isEqual(endDate) == true) {

                        } else {
                            endTime = it
                        }
                    }
                )

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(
                        onClick = onDismissRequest,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                    ) {
                        Text(text = "Cancel")
                    }

                    TextButton(
                        onClick = {
                            updateTaskGroupTitle(title)
                            updateTaskGroupNote(note)
                            updateTaskGroupPriority(priority)
                            startDate?.let { updateTaskGroupStartDate(it) }
                            startTime?.let { updateTaskGroupStartTime(it) }
                            endDate?.let { updateTaskGroupEndDate(it) }
                            endTime?.let { updateTaskGroupEndTime(it) }
                            onDismissRequest()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                    ) {
                        Text(text = "Ok")
                    }
                }
            }
        }
    }
}