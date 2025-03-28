package com.je.playground.designsystem.component.task.taskeditor

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.je.playground.database.tasks.entity.SubTask
import com.je.playground.designsystem.component.task.DateTimeRangeText
import com.je.playground.designsystem.component.task.NoteComponent
import com.je.playground.feature.tasks.editor.overview.TaskEditorOverviewEvent
import com.je.playground.feature.utility.Result
import com.je.playground.ui.theme.PlaygroundTheme
import com.je.playground.ui.theme.ThemePreviews
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun SubTasksComponent(
    taskId: Long,
    subTasks: List<SubTask>,
    onEvent: (TaskEditorOverviewEvent) -> Unit,
    navigateToSubTaskEditor: (Long, Long) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
        Surface {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(
                    start = 16.dp,
                    top = 8.dp,
                    end = 4.dp,
                    bottom = 4.dp
                )
            ) {
                Text(
                    text = "Subtasks",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )

                IconButton(onClick = { navigateToSubTaskEditor(taskId, 0L) }) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "New Task",
                    )
                }
            }
        }

        subTasks.forEach { subTask ->
            SubTaskComponent(
                subTask = subTask,
                onEvent = onEvent,
                navigateToSubTaskEditor = navigateToSubTaskEditor
            )
        }
    }
}

@Composable
fun SubTaskComponent(
    subTask: SubTask,
    onEvent: (TaskEditorOverviewEvent) -> Unit,
    navigateToSubTaskEditor: (Long, Long) -> Unit
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    var dropDownMenuExpanded by rememberSaveable { mutableStateOf(false) }

    Surface {
        Column(
            modifier = Modifier
                .clickable { expanded = !expanded }
                .wrapContentHeight()
        ) {
            Row(
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 4.dp,
                    top = 6.dp,
                    bottom = 6.dp
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = subTask.title,
                        overflow = TextOverflow.Ellipsis,
                        softWrap = expanded,
                        style = MaterialTheme.typography.titleMedium
                    )

                    DateTimeRangeText(
                        startDate = subTask.startDate,
                        startTime = subTask.startTime,
                        endDate = subTask.endDate,
                        endTime = subTask.endTime
                    )

                    subTask.note?.let {
                        if (it.isNotBlank()) {
                            NoteComponent(
                                note = it,
                                isExpanded = expanded,
                            )
                        }
                    }
                }

                Box(modifier = Modifier.align(Alignment.Top)) {
                    IconButton(onClick = { dropDownMenuExpanded = !dropDownMenuExpanded }) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = "Note"
                        )
                    }

                    DropdownMenu(
                        expanded = dropDownMenuExpanded,
                        onDismissRequest = { dropDownMenuExpanded = false },
                    ) {
                        TextButton(onClick = {
                            dropDownMenuExpanded = false
                            navigateToSubTaskEditor(subTask.taskId, subTask.subTaskId)
                        }) { Text(text = "Edit") }

                        TextButton(onClick = {
                            dropDownMenuExpanded = false
                            onEvent(TaskEditorOverviewEvent.RemoveSubTask(subTask))
                        }) { Text(text = "Delete") }
                    }
                }
            }
        }
    }
}


@ThemePreviews
@Composable
fun SubTasksComponentPreview() {
    PlaygroundTheme {
        SubTasksComponent(
            taskId = 0,
            subTasks = listOf(
                SubTask(
                    taskId = 0,
                    title = "Stuff",
                ),
                SubTask(
                    taskId = 0,
                    title = "Do stuff",
                    startDate = LocalDate.now().plusDays(50),
                    endDate = LocalDate.now().plusDays(58),
                    startTime = LocalTime.now(),
                    endTime = LocalTime.now().plusHours(1)
                ),
                SubTask(
                    taskId = 0,
                    title = "Do thing",
                    note = "Lorem ipsum dolor sit amet.",
                    startDate = LocalDate.now().plusDays(50),
                    endDate = LocalDate.now().plusDays(58),
                    startTime = LocalTime.now(),
                    endTime = LocalTime.now().plusHours(1)
                ),
                SubTask(
                    taskId = 0,
                    title = "Do other thing",
                    note = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam blandit porta fringilla. Proin eu odio eget dolor placerat facilisis. Mauris aliquam purus vitae dolor fringilla congue. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus.",
                    startDate = LocalDate.now().plusDays(50),
                    endDate = LocalDate.now().plusDays(58),
                    startTime = LocalTime.now(),
                    endTime = LocalTime.now().plusHours(1)
                )
            ),
            onEvent = { Result.Success },
            navigateToSubTaskEditor = { _, _ -> }
        )
    }
}