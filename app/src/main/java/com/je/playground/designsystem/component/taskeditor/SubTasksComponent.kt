package com.je.playground.designsystem.component.taskeditor

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.je.playground.database.tasks.entity.SubTask
import com.je.playground.designsystem.component.NoteComponent
import com.je.playground.designsystem.dateTimeToString
import com.je.playground.designsystem.theme.title
import com.je.playground.feature.tasks.editor.TaskEditorEvent

@Composable
fun SubTasksComponent(
    subTasks: List<SubTask>,
    onEvent: (TaskEditorEvent) -> Unit,
    navigateToSubTaskEditorScreen: (Long, Int) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(
                start = 12.dp
            )
    ) {
        Text(
            text = "Tasks",
            modifier = Modifier
                .weight(1f)
        )

        IconButton(
            onClick = {
                navigateToSubTaskEditorScreen(-1L, -1)
            },
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "New Task",
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }

    subTasks.forEach { subTask ->
        var isExpanded by rememberSaveable {
            mutableStateOf(false)
        }

        var isDropDownMenuExpanded by rememberSaveable {
            mutableStateOf(false)
        }

        Column(
            modifier = Modifier
                .clickable { isExpanded = !isExpanded }
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(
                    start = 12.dp,
                    top = 6.dp,
                    bottom = 6.dp
                )
                .wrapContentHeight()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text(
                        text = subTask.title,
                        style = title(MaterialTheme.colorScheme.onPrimary),
                        textAlign = TextAlign.Start,
                    )

                    if (subTask.startDate != null || subTask.startTime != null || subTask.endDate != null || subTask.endTime != null) {
                        Text(
                            text = dateTimeToString(
                                startDate = subTask.startDate,
                                startTime = subTask.startTime,
                                endDate = subTask.endDate,
                                endTime = subTask.endTime
                            ),
                            color = Color(0xFFCCCCCC),
                            fontSize = 12.sp,
                            textAlign = TextAlign.Start
                        )
                    }
                }

                Box {
                    IconButton(onClick = {
                        isDropDownMenuExpanded = !isDropDownMenuExpanded
                    }) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = "Note"
                        )
                    }

                    DropdownMenu(
                        expanded = isDropDownMenuExpanded,
                        onDismissRequest = { isDropDownMenuExpanded = false },
                        modifier = Modifier.background(MaterialTheme.colorScheme.secondary)
                    ) {
                        Column { //TODO: Not needed?
                            TextButton(onClick = {
                                isDropDownMenuExpanded = false
                                navigateToSubTaskEditorScreen(
                                    subTask.subTaskId,
                                    subTasks.indexOf(subTask)
                                )
                            }) {
                                Text(
                                    text = "Edit",
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            }

                            TextButton(onClick = {
                                onEvent(TaskEditorEvent.RemoveSubTask(subTasks.indexOf(subTask)))
                                isDropDownMenuExpanded = false
                            }) {
                                Text(
                                    text = "Delete",
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }
                    }
                }
            }

            if (subTask.note != "") {
                NoteComponent(
                    note = subTask.note,
                    isExpanded = isExpanded,
                    modifier = Modifier.padding(
                        top = 6.dp,
                        bottom = 6.dp,
                        end = 12.dp
                    )
                )

            }
        }
    }
}