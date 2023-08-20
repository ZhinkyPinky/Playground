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
import com.je.playground.database.tasks.entity.TaskGroup
import com.je.playground.ui.taskview.viewmodel.Priority

@Composable
fun TaskGroupEditorDialog(
    taskGroup : TaskGroup,
    onSave : (TaskGroup) -> Unit,
    onDismissRequest : () -> Unit
) {
    var title by rememberSaveable { mutableStateOf(taskGroup.title) }
    var note by rememberSaveable { mutableStateOf(taskGroup.note) }
    var priority by rememberSaveable { mutableIntStateOf(taskGroup.priority) }
    var startDate by rememberSaveable { mutableStateOf(taskGroup.startDate) }
    var startTime by rememberSaveable { mutableStateOf(taskGroup.startTime) }
    var endDate by rememberSaveable { mutableStateOf(taskGroup.endDate) }
    var endTime by rememberSaveable { mutableStateOf(taskGroup.endTime) }

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
                    labelText = "Title*",
                    value = title,
                    isSingleLine = true,
                    onValueChange = {
                        title = it
                        taskGroup.title = title
                    }
                )

                NoteEditComponent(onValueChange = {
                    note = it
                    taskGroup.note = note
                })

                PrioritySliderComponent(
                    onPriorityChanged = {
                        priority = Priority.valueOf(it).ordinal
                        taskGroup.priority = priority
                    },
                    modifier = Modifier.padding(
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 6.dp
                    )
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
                            onSave(taskGroup)
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