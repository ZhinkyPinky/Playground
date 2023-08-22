package com.je.playground.ui.taskview.taskeditor.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.window.DialogProperties
import com.je.playground.ui.taskview.taskeditor.NoteEditComponent
import com.je.playground.ui.taskview.taskeditor.PrioritySliderComponent
import com.je.playground.ui.taskview.taskeditor.TextFieldComponent
import com.je.playground.ui.taskview.taskeditor.datetimerangepicker.DateRangePicker
import com.je.playground.ui.taskview.taskeditor.datetimerangepicker.TimeRangePicker
import java.time.LocalDate
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTaskEditorDialog(
    mainTaskTitle : String,
    mainTaskNote : String,
    mainTaskPriority : Int,
    mainTaskStartDate : LocalDate?,
    mainTaskStartTime : LocalTime?,
    mainTaskEndDate : LocalDate?,
    mainTaskEndTime : LocalTime?,
    updateMainTask : (String, String, Int, LocalDate?, LocalTime?, LocalDate?, LocalTime?) -> Unit,
    onDismissRequest : () -> Unit,
) {
    var title by rememberSaveable { mutableStateOf(mainTaskTitle) }
    var note by rememberSaveable { mutableStateOf(mainTaskNote) }
    var priority by rememberSaveable { mutableIntStateOf(mainTaskPriority) }
    var startDate by rememberSaveable { mutableStateOf(mainTaskStartDate) }
    var startTime by rememberSaveable { mutableStateOf(mainTaskStartTime) }
    var endDate by rememberSaveable { mutableStateOf(mainTaskEndDate) }
    var endTime by rememberSaveable { mutableStateOf(mainTaskEndTime) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
        modifier = Modifier.padding(24.dp)
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
                    text = "New task group",
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
                        top = 12.dp,
                        bottom = 6.dp
                    )
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.padding(
                        start = 12.dp,
                        end = 12.dp
                    )
                ) {
                    TextFieldComponent(
                        label = "Title*",
                        placeholder = "Enter a title for the group",
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
                            updateMainTask(
                                title,
                                note,
                                priority,
                                startDate,
                                startTime,
                                endDate,
                                endTime
                            )
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