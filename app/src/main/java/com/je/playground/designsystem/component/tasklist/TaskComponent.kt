package com.je.playground.designsystem.component.tasklist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.je.playground.database.tasks.entity.SubTask
import com.je.playground.database.tasks.entity.Task
import com.je.playground.database.tasks.entity.TaskWithSubTasks
import com.je.playground.designsystem.ThemePreviews
import com.je.playground.designsystem.component.CheckboxComponent
import com.je.playground.designsystem.component.NoteComponent
import com.je.playground.designsystem.component.dialog.ConfirmationDialog
import com.je.playground.designsystem.dateTimeToString
import com.je.playground.designsystem.theme.PlaygroundTheme
import com.je.playground.designsystem.theme.title
import com.je.playground.feature.tasks.list.TaskListEvent

@Composable
fun TaskComponent(
    taskWithSubTasks: TaskWithSubTasks,
    navigateToTaskEditScreen: (Long) -> Unit,
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
                navigateToTaskEditScreen = navigateToTaskEditScreen,
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
    navigateToTaskEditScreen: (Long) -> Unit,
    onEvent: (TaskListEvent) -> Unit
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }

    var completionCounter = 0
    for (i in 0 until taskWithSubTasks.subTasks.size) {
        if (taskWithSubTasks.subTasks[i].isCompleted) {
            completionCounter++
        }
    }

    Box(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.primaryContainer
                //TODO: if (isExpanded && mainTaskWithSubTasks.mainTask.note != "") MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.primaryContainer
            )
            .wrapContentHeight()
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )
            .clickable { isExpanded = !isExpanded }
    )
    {
        Column(
            modifier = Modifier.padding(
                start = 6.dp,
                top = 6.dp,
                bottom = 6.dp
            )

        ) {
            Row(
                modifier = Modifier
                    .height(IntrinsicSize.Max)
            ) {
                TaskPriorityIndicator(taskWithSubTasks)

                Column(
                    verticalArrangement = Arrangement.spacedBy(1.dp),
                ) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            if (taskWithSubTasks.subTasks.isEmpty()) {
                                CheckboxComponent(
                                    isChecked = taskWithSubTasks.task.isCompleted,
                                    onCheckedChange = {
                                        onEvent(TaskListEvent.ToggleTaskCompletion(task = taskWithSubTasks.task))
                                    }
                                )
                            }

                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = if (taskWithSubTasks.subTasks.isNotEmpty()) 12.dp else 0.dp)
                            ) {
                                Text(
                                    text = taskWithSubTasks.task.title,
                                    style = title(MaterialTheme.colorScheme.onPrimary),
                                    textAlign = TextAlign.Start,
                                )

                                if (taskWithSubTasks.task.startDate != null || taskWithSubTasks.task.startTime != null || taskWithSubTasks.task.endDate != null || taskWithSubTasks.task.endTime != null) {
                                    Text(
                                        text = dateTimeToString(
                                            startDate = taskWithSubTasks.task.startDate,
                                            startTime = taskWithSubTasks.task.startTime,
                                            endDate = taskWithSubTasks.task.endDate,
                                            endTime = taskWithSubTasks.task.endTime
                                        ),
                                        color = Color(0xFFCCCCCC),
                                        fontSize = 12.sp,
                                        textAlign = TextAlign.Start
                                    )
                                }
                            }

                            TaskDropDownMenu(
                                taskWithSubTasks = taskWithSubTasks,
                                navigateToTaskEditScreen = navigateToTaskEditScreen,
                                onEvent = onEvent
                            )

                        }

                        if (taskWithSubTasks.task.note != "") {
                            NoteComponent(
                                note = taskWithSubTasks.task.note,
                                isExpanded = isExpanded,
                                modifier = Modifier.padding(
                                    start = 12.dp,
                                    end = 12.dp,
                                    bottom = 6.dp
                                )
                            )
                        }

                        if (taskWithSubTasks.subTasks.isNotEmpty()) {
                            LinearProgressIndicator(
                                progress = completionCounter.toFloat() / taskWithSubTasks.subTasks.size,
                                color = MaterialTheme.colorScheme.onPrimary,
                                trackColor = MaterialTheme.colorScheme.background,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(1.dp)
                                    .padding(
                                        start = 6.dp,
                                        end = 6.dp
                                    )
                            )
                        }
                    }

                    if (taskWithSubTasks.subTasks.isNotEmpty()) {
                        SubTasks(
                            taskWithSubTasks = taskWithSubTasks,
                            completionCounter,
                            onEvent,
                        )
                    }
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
                    0 -> Color(0xFF00C853)
                    1 -> Color(0xFFFFAB00)
                    2 -> Color.Red
                    else -> Color.Transparent
                }
            )
            .fillMaxHeight()
            .width(2.dp)
    )
}


@Composable
fun DismissBackground(dismissState: SwipeToDismissBoxState) {
    val color: Color = when (dismissState.targetValue) {
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
        /*
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
    taskWithSubTasks: TaskWithSubTasks,
    completionCounter: Int,
    onEvent: (TaskListEvent) -> Unit,
) {
    val hapticFeedback = LocalHapticFeedback.current

    var completion by rememberSaveable { mutableIntStateOf(completionCounter) }

    taskWithSubTasks.subTasks.forEach { subTask ->
        SubTaskComponent(
            subTask = subTask,
            isCompleted = subTask.isCompleted,
            onCompletion = {
                if (subTask.isCompleted) {
                    completion++
                } else {
                    completion--
                }

                onEvent(TaskListEvent.ToggleSubTaskCompletion(subTask))
                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
            }
        )
    }
}

@Composable
fun TaskDropDownMenu(
    taskWithSubTasks: TaskWithSubTasks,
    navigateToTaskEditScreen: (Long) -> Unit,
    onEvent: (TaskListEvent) -> Unit
) {
    var isDropDownMenuExpanded by rememberSaveable { mutableStateOf(false) }
    var isDeletionDialogDisplayed by rememberSaveable { mutableStateOf(false) }

    if (isDeletionDialogDisplayed) {
        DeletionDialog(
            taskWithSubTasks = taskWithSubTasks,
            onEvent = onEvent,
            dismissDialog = { isDeletionDialogDisplayed = false }
        )
    }

    Box {
        IconButton(onClick = {
            isDropDownMenuExpanded = !isDropDownMenuExpanded
        }) {
            Icon(
                imageVector = Icons.Filled.MoreVert,
                contentDescription = "Note",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }

        DropdownMenu(
            expanded = isDropDownMenuExpanded,
            onDismissRequest = { isDropDownMenuExpanded = false },
            modifier = Modifier.background(MaterialTheme.colorScheme.secondary)
        ) {
            TextButton(onClick = {
                isDropDownMenuExpanded = false
                navigateToTaskEditScreen(taskWithSubTasks.task.mainTaskId)
            }) {
                Text(
                    text = "Edit",
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            TextButton(onClick = {
                isDeletionDialogDisplayed = true
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

@Composable
fun DeletionDialog(
    taskWithSubTasks: TaskWithSubTasks,
    onEvent: (TaskListEvent) -> Unit,
    dismissDialog: () -> Unit
) {
    ConfirmationDialog(
        title = "Delete",
        contentText = "This will delete the task permanently. You cannot undo this action.",
        confirmButtonText = "Delete",
        confirm = { onEvent(TaskListEvent.DeleteTaskWithSubTasks(taskWithSubTasks)) },

        onDismissRequest = dismissDialog
    )
}


@ThemePreviews
@Composable
fun TaskComponentPreview() {
    PlaygroundTheme {
        TaskComponent(
            taskWithSubTasks = TaskWithSubTasks(
                task = Task(
                    title = "Test",
                    note = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."

                )
            ),
            navigateToTaskEditScreen = {},
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
                    note = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."

                ),
                subTasks = listOf(
                    SubTask(
                        title = "TestSubTask1",
                        note = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                        isCompleted = true
                    ),
                    SubTask(
                        title = "TestSubTask2",
                        note = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                        isCompleted = false
                    )
                )
            ),
            navigateToTaskEditScreen = {},
            onEvent = {}
        )
    }
}

