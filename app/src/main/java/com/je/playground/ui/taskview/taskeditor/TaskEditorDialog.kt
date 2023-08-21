package com.je.playground.ui.taskview.taskeditor

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.je.playground.database.tasks.entity.Task
import com.je.playground.database.tasks.entity.TaskGroup
import com.je.playground.ui.taskview.taskeditor.datetimerangepicker.DateRangePicker
import com.je.playground.ui.taskview.taskeditor.datetimerangepicker.TimeRangePicker

@Composable
fun TaskEditorDialog(
    task : Task,
    taskGroup : TaskGroup,
    isNewTask : Boolean,
    onSave : (Task) -> Unit,
    onDismissRequest : () -> Unit
) {
    var title by rememberSaveable { mutableStateOf(task.title) }
    var note by rememberSaveable { mutableStateOf(task.note) }
    var startDate by rememberSaveable { mutableStateOf(task.startDate) }
    var startTime by rememberSaveable { mutableStateOf(task.startTime) }
    var endDate by rememberSaveable { mutableStateOf(task.endDate) }
    var endTime by rememberSaveable { mutableStateOf(task.endTime) }

    Dialog(
        onDismissRequest = onDismissRequest,
    ) {
        Surface(
            modifier =
            Modifier.background(MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column {
                Text(
                    text = "New task",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 12.dp,
                            top = 12.dp
                        )
                )

                Divider(
                    color = MaterialTheme.colorScheme.background,
                    modifier = Modifier.padding(
                        12.dp
                    )
                )

                Column(
                    modifier = Modifier.wrapContentHeight()
                ) {
                    TextFieldComponent(
                        label = "Title*",
                        placeholder = "Enter a title for the task",
                        value = title,
                        isSingleLine = true,
                        onValueChange = {
                            title = it
                            task.title = it
                        }
                    )

                    NoteEditComponent(
                        note = note,
                        onValueChange = {
                            note = it
                            task.note = note
                        })

                    DateRangePicker(
                        startDate = startDate,
                        endDate = endDate,
                        onStartDateValueChange = {
                            startDate = it
                            task.startDate = startDate

                            Log.i(
                                "startDate",
                                "$startDate"
                            )
                        },
                        onEndDateValueChange = {
                            endDate = it
                            task.endDate = endDate

                            Log.i(
                                "endDate",
                                "$endDate"
                            )
                        },
                    )

                    TimeRangePicker(
                        startTime = startTime,
                        endTime = endTime,
                        onStartTimeValueChange = {
                            startTime = it
                            task.startTime = startTime
                        },
                        onEndTimeValueChange = {
                            if ((startTime?.isBefore(it) == true)) {

                            } else {
                                endTime = it
                                task.endTime = endTime
                            }
                        }
                    )
                }

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
                            onSave(
                                Task(
                                    taskGroupId = taskGroup.taskGroupId,
                                    title = title,
                                    note = note,
                                    startDate = startDate,
                                    startTime = startTime,
                                    endDate = endDate,
                                    endTime = endTime
                                )
                            )
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