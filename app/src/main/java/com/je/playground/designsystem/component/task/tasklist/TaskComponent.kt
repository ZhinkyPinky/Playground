package com.je.playground.designsystem.component.task.tasklist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.je.playground.R
import com.je.playground.database.tasks.entity.SubTask
import com.je.playground.database.tasks.entity.Task
import com.je.playground.database.tasks.entity.TaskWithSubTasks
import com.je.playground.designsystem.component.common.CheckboxComponent
import com.je.playground.designsystem.component.dialog.ConfirmationDialog
import com.je.playground.designsystem.component.task.DateTimeRangeText
import com.je.playground.designsystem.component.task.NoteComponent
import com.je.playground.feature.tasks.list.TaskListEvent
import com.je.playground.ui.theme.PlaygroundTheme
import com.je.playground.ui.theme.ThemePreviews
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun TaskComponent(
    taskWithSubTasks: TaskWithSubTasks,
    navigateToTaskEditorOverview: (Long) -> Unit,
    onEvent: (TaskListEvent) -> Unit
) {
    val hapticFeedback = LocalHapticFeedback.current

    var isVisible by remember { mutableStateOf(true) }
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            if (it == SwipeToDismissBoxValue.EndToStart) {
                isVisible = false
                true
            } else {
                false
            }
        },
        positionalThreshold = { 150.dp.value }
    )

    AnimatedVisibility(
        visible = isVisible,
        exit = fadeOut(spring())
    ) {
        SwipeToDismissBox(
            state = dismissState,
            enableDismissFromStartToEnd = false,
            enableDismissFromEndToStart = taskWithSubTasks.task.isCompleted,
            backgroundContent = {
                DismissBackground(
                    dismissState = dismissState
                )
            }
        ) {
            TaskComponentContent(
                taskWithSubTasks = taskWithSubTasks,
                navigateToTaskEditorOverview = navigateToTaskEditorOverview,
                onEvent = onEvent
            )
        }
    }

    LaunchedEffect(
        isVisible
    ) {
        if (!isVisible) {
            onEvent(TaskListEvent.ToggleTaskArchived(taskWithSubTasks.task))
            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
        }
    }
}

@Composable
fun TaskComponentContent(
    taskWithSubTasks: TaskWithSubTasks,
    navigateToTaskEditorOverview: (Long) -> Unit,
    onEvent: (TaskListEvent) -> Unit
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }

    //TODO: Find better solution.
    var completionCounter = 0
    for (i in 0 until taskWithSubTasks.subTasks.size) {
        if (taskWithSubTasks.subTasks[i].isCompleted) {
            completionCounter++
        }
    }

    Surface {
        Row(
            modifier = Modifier
                .height(IntrinsicSize.Max)
                .fillMaxWidth()
                .clickable { isExpanded = !isExpanded }
                .animateContentSize()
                .padding(
                    start = 6.dp,
                    top = 6.dp,
                    bottom = 6.dp
                )
        ) {
            TaskPriorityIndicator(taskWithSubTasks)

            Column {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 6.dp)
                ) {
                    if (taskWithSubTasks.subTasks.isEmpty()) {
                        CheckboxComponent(
                            isChecked = taskWithSubTasks.task.isCompleted,
                            onCheckedChange = {
                                onEvent(TaskListEvent.ToggleTaskCompletion(task = taskWithSubTasks.task))
                            },
                            modifier = Modifier.align(Alignment.Top)
                        )
                    }

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(
                                start = 8.dp,
                                top = 6.dp,
                                end = 16.dp,
                                bottom = 6.dp
                            )
                            .align(Alignment.CenterVertically)
                    ) {
                        Text(
                            text = taskWithSubTasks.task.title,
                            overflow = TextOverflow.Ellipsis,
                            softWrap = isExpanded,
                            style = MaterialTheme.typography.titleMedium
                        )

                        DateTimeRangeText(
                            startDate = taskWithSubTasks.task.startDate,
                            startTime = taskWithSubTasks.task.startTime,
                            endDate = taskWithSubTasks.task.endDate,
                            endTime = taskWithSubTasks.task.endTime
                        )

                        taskWithSubTasks.task.note?.let {
                            if (it.isNotBlank()) {
                                NoteComponent(
                                    note = it,
                                    isExpanded = isExpanded,
                                )
                            }
                        }
                    }

                    TaskDropDownMenu(
                        taskWithSubTasks = taskWithSubTasks,
                        navigateToTaskEditorOverview = navigateToTaskEditorOverview,
                        onEvent = onEvent
                    )
                }


                if (taskWithSubTasks.subTasks.isNotEmpty()) {
                    LinearProgressIndicator(
                        progress = { completionCounter.toFloat() / taskWithSubTasks.subTasks.size },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .padding(
                                start = 8.dp,
                                end = 16.dp
                            )
                    )

                    SubTasks(
                        subTasks = taskWithSubTasks.subTasks,
                        onEvent,
                    )
                }
            }

        }
    }
}

@Composable
fun TaskPriorityIndicator(
    taskWithSubTasks: TaskWithSubTasks
) {
    Box(
        modifier = Modifier
            .clip(RectangleShape)
            .background(
                when (taskWithSubTasks.task.priority) {
                    0 -> com.je.playground.ui.theme.lowPriority
                    1 -> com.je.playground.ui.theme.mediumPriority
                    2 -> com.je.playground.ui.theme.highPriority
                    else -> Color.Transparent
                }
            )
            .fillMaxHeight()
            .width(2.dp)
    )
}


@Composable
fun DismissBackground(dismissState: SwipeToDismissBoxState) {
    val color: Color = when (dismissState.targetValue) { //TODO: Less hard colored.
        SwipeToDismissBoxValue.StartToEnd -> MaterialTheme.colorScheme.background
        SwipeToDismissBoxValue.EndToStart -> Color(0xFF1DE9B6)
        SwipeToDismissBoxValue.Settled -> Color.Transparent
    }

    val direction = dismissState.dismissDirection

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(color)
            .padding(
                12.dp,
                8.dp
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        /* TODO: Right swipe?
        if (direction == DismissDirection.StartToEnd) Icon(
            Icons.Default.Delete,
            contentDescription = "delete"
        )
        */
        Spacer(modifier = Modifier)

        if (direction == SwipeToDismissBoxValue.EndToStart) Icon(
            imageVector = Icons.Filled.Archive,
            contentDescription = "Archive"
        )
    }
}

@Composable
fun SubTasks(
    subTasks: List<SubTask>,
    onEvent: (TaskListEvent) -> Unit,
) {
    val hapticFeedback = LocalHapticFeedback.current

    subTasks.forEach { subTask ->
        SubTaskComponent(
            subTask = subTask,
            isCompleted = subTask.isCompleted,
            onCompletion = {
                onEvent(TaskListEvent.ToggleSubTaskCompletion(subTask))
                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
            }
        )
    }
}

@Composable
fun TaskDropDownMenu(
    taskWithSubTasks: TaskWithSubTasks,
    navigateToTaskEditorOverview: (Long) -> Unit,
    onEvent: (TaskListEvent) -> Unit
) {
    var isDropDownMenuExpanded by rememberSaveable { mutableStateOf(false) }
    var isDeletionDialogDisplayed by rememberSaveable { mutableStateOf(false) }

    if (isDeletionDialogDisplayed) {
        ConfirmationDialog(
            title = stringResource(R.string.delete),
            text = stringResource(R.string.delete_task_warning),
            confirmButtonText = stringResource(R.string.delete),
            onConfirm = { onEvent(TaskListEvent.Delete(taskWithSubTasks)) },
            onDismissRequest = { isDeletionDialogDisplayed = false }
        )
    }

    Box {
        IconButton(onClick = {
            isDropDownMenuExpanded = !isDropDownMenuExpanded
        }) {
            Icon(
                imageVector = Icons.Filled.MoreVert,
                contentDescription = "",
            )
        }

        DropdownMenu(
            expanded = isDropDownMenuExpanded,
            onDismissRequest = { isDropDownMenuExpanded = false },
        ) {
            TextButton(onClick = {
                isDropDownMenuExpanded = false
                navigateToTaskEditorOverview(taskWithSubTasks.task.taskId)
            }) { Text(text = "Edit") }

            TextButton(onClick = {
                isDeletionDialogDisplayed = true
                isDropDownMenuExpanded = false
            }) { Text(text = "Delete") }
        }
    }
}

@ThemePreviews
@Composable
fun TaskComponentSimplePreview() {
    PlaygroundTheme {
        TaskComponent(
            taskWithSubTasks = TaskWithSubTasks(task = Task(title = "Test")),
            navigateToTaskEditorOverview = {},
            onEvent = {}
        )
    }
}

@ThemePreviews
@Composable
fun TaskComponentPreview() {
    PlaygroundTheme {
        TaskComponent(
            taskWithSubTasks = TaskWithSubTasks(
                task = Task(
                    title = "Test",
                    note = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                    startDate = LocalDate.now().plusDays(50),
                    endDate = LocalDate.now().plusDays(58),
                    startTime = LocalTime.now(),
                    endTime = LocalTime.now().plusHours(1)
                )
            ),
            navigateToTaskEditorOverview = {},
            onEvent = {}
        )
    }
}

@ThemePreviews
@Composable
fun TaskComponentWithSubtasksPreview() {
    PlaygroundTheme {
        TaskComponent(
            taskWithSubTasks = TaskWithSubTasks(
                task = Task(
                    title = "Test",
                    note = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                    startDate = LocalDate.now().plusDays(50),
                    endDate = LocalDate.now().plusDays(58),
                    startTime = LocalTime.now(),
                    endTime = LocalTime.now().plusHours(1)
                ),
                subTasks = listOf(
                    SubTask(
                        title = "TestSubTask1",
                        note = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                        startDate = LocalDate.now().plusDays(50),
                        endDate = LocalDate.now().plusDays(58),
                        startTime = LocalTime.now(),
                        endTime = LocalTime.now().plusHours(1),
                        isCompleted = true
                    ),
                    SubTask(
                        title = "TestSubTask2",
                        note = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                        isCompleted = false
                    )
                )
            ),
            navigateToTaskEditorOverview = {},
            onEvent = {}
        )
    }
}

