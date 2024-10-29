package com.je.playground.deprecated.exerciseprogrameditor.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.window.DialogProperties
import com.je.playground.database.tasks.entity.SubTask
import com.je.playground.deprecated.datetimerangepicker.DateRangePicker
import com.je.playground.deprecated.datetimerangepicker.TimeRangePicker
import com.je.playground.designsystem.component.task.taskeditor.NoteEditComponent
import com.je.playground.designsystem.component.task.taskeditor.TextFieldComponent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubTaskEditorDialog(
    dialogTitle: String,
    taskId: Long,
    subTask: SubTask,
    isNewTask: Boolean,
    onSave: (SubTask) -> Unit,
    onDismissRequest: () -> Unit
) {
    var title by rememberSaveable { mutableStateOf(subTask.title) }
    var note by rememberSaveable { mutableStateOf(subTask.note) }
    var startDate by rememberSaveable { mutableStateOf(subTask.startDate) }
    var startTime by rememberSaveable { mutableStateOf(subTask.startTime) }
    var endDate by rememberSaveable { mutableStateOf(subTask.endDate) }
    var endTime by rememberSaveable { mutableStateOf(subTask.endTime) }

    BasicAlertDialog(
        onDismissRequest = onDismissRequest,
        modifier = Modifier.padding(24.dp),
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Surface(
            modifier =
            Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .wrapContentHeight()
                .wrapContentWidth()
        ) {
            Column {
                Text(
                    text = dialogTitle,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 12.dp,
                            top = 12.dp
                        )
                )

                HorizontalDivider(
                    modifier = Modifier.padding(
                        top = 12.dp,
                        bottom = 6.dp
                    ),
                    color = MaterialTheme.colorScheme.background
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier
                        .padding(
                            start = 12.dp,
                            end = 12.dp
                        )
                        .wrapContentHeight()
                ) {
                    TextFieldComponent(
                        label = "Title*",
                        placeholder = "Enter a title for the task",
                        value = title,
                        isSingleLine = true,
                        onValueChange = {
                            title = it
                        }
                    )

                    note?.let { currentNote ->
                        NoteEditComponent(
                            note = currentNote,
                            onValueChange = { newNote ->
                                note = newNote
                            })
                    }

                    DateRangePicker(
                        startDate = startDate,
                        endDate = endDate,
                        onStartDateValueChange = {
                            startDate = it
                        },
                        onEndDateValueChange = {
                            endDate = it
                        },
                        clearDates = {
                            startDate = null
                            endDate = null
                        }
                    )

                    TimeRangePicker(
                        startTime = startTime,
                        endTime = endTime,
                        onStartTimeValueChange = {
                            startTime = it
                        },
                        onEndTimeValueChange = {
                            if ((startTime?.isBefore(it) == true)) {

                            } else {
                                endTime = it
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
                                SubTask(
                                    taskId = taskId,
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