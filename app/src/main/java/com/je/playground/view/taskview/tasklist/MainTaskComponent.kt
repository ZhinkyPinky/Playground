package com.je.playground.view.taskview.tasklist

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
import com.je.playground.database.tasks.entity.MainTask
import com.je.playground.database.tasks.entity.MainTaskWithSubTasks
import com.je.playground.view.dialog.ConfirmationDialog
import com.je.playground.view.sharedcomponents.CheckboxComponent
import com.je.playground.view.sharedcomponents.NoteComponent
import com.je.playground.view.taskview.dateTimeToString
import com.je.playground.view.theme.title


@Composable
fun MainTaskComponent(
    mainTaskWithSubTasks: MainTaskWithSubTasks,
    navigateToTaskEditScreen: (Long) -> Unit,
    updateMainTask: (MainTask) -> Unit,
    updateMainTaskWithSubTasks: (MainTaskWithSubTasks) -> Unit,
    deleteMainTaskWithSubTasks: (MainTaskWithSubTasks) -> Unit,
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
            enableDismissFromEndToStart = mainTaskWithSubTasks.mainTask.isCompleted,
            backgroundContent = {
                DismissBackground(
                    dismissState = dismissState
                )
            }
        ) {
            MainTaskComponentContent(
                mainTaskWithSubTasks = mainTaskWithSubTasks,
                navigateToTaskEditScreen = navigateToTaskEditScreen,
                updateMainTaskWithSubTasks = updateMainTaskWithSubTasks,
                deleteMainTaskWithSubTasks = deleteMainTaskWithSubTasks
            )
        }
    }

    LaunchedEffect(
        isVisible
    ) {
        if (!isVisible) {
            mainTaskWithSubTasks.mainTask.isArchived = true
            updateMainTask(mainTaskWithSubTasks.mainTask)
            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
        }
    }
}

@Composable
fun MainTaskComponentContent(
    mainTaskWithSubTasks: MainTaskWithSubTasks,
    navigateToTaskEditScreen: (Long) -> Unit,
    updateMainTaskWithSubTasks: (MainTaskWithSubTasks) -> Unit,
    deleteMainTaskWithSubTasks: (MainTaskWithSubTasks) -> Unit,
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }

    var completionCounter = 0
    for (i in 0 until mainTaskWithSubTasks.subTasks.size) {
        if (mainTaskWithSubTasks.subTasks[i].isCompleted) {
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
                MainTaskPriorityIndicator(mainTaskWithSubTasks)

                Column(
                    verticalArrangement = Arrangement.spacedBy(1.dp),
                ) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            if (mainTaskWithSubTasks.subTasks.isEmpty()) {
                                CheckboxComponent(
                                    isChecked = mainTaskWithSubTasks.mainTask.isCompleted,
                                    onCheckedChange = {
                                        mainTaskWithSubTasks.mainTask.isCompleted =
                                            !mainTaskWithSubTasks.mainTask.isCompleted
                                        updateMainTaskWithSubTasks(mainTaskWithSubTasks)
                                    }
                                )
                            }

                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = if (mainTaskWithSubTasks.subTasks.isNotEmpty()) 12.dp else 0.dp)
                            ) {
                                Text(
                                    text = mainTaskWithSubTasks.mainTask.title,
                                    style = title(MaterialTheme.colorScheme.onPrimary),
                                    textAlign = TextAlign.Start,
                                )

                                if (mainTaskWithSubTasks.mainTask.startDate != null || mainTaskWithSubTasks.mainTask.startTime != null || mainTaskWithSubTasks.mainTask.endDate != null || mainTaskWithSubTasks.mainTask.endTime != null) {
                                    Text(
                                        text = dateTimeToString(
                                            startDate = mainTaskWithSubTasks.mainTask.startDate,
                                            startTime = mainTaskWithSubTasks.mainTask.startTime,
                                            endDate = mainTaskWithSubTasks.mainTask.endDate,
                                            endTime = mainTaskWithSubTasks.mainTask.endTime
                                        ),
                                        color = Color(0xFFCCCCCC),
                                        fontSize = 12.sp,
                                        textAlign = TextAlign.Start
                                    )
                                }
                            }

                            MainTaskDropDownMenu(
                                mainTaskWithSubTasks = mainTaskWithSubTasks,
                                navigateToTaskEditScreen = navigateToTaskEditScreen,
                                deleteMainTaskWithSubTasks = deleteMainTaskWithSubTasks
                            )

                        }

                        if (mainTaskWithSubTasks.mainTask.note != "") {
                            NoteComponent(
                                note = mainTaskWithSubTasks.mainTask.note,
                                isExpanded = isExpanded,
                                modifier = Modifier.padding(
                                    start = 12.dp,
                                    end = 12.dp,
                                    bottom = 6.dp
                                )
                            )
                        }

                        if (mainTaskWithSubTasks.subTasks.isNotEmpty()) {
                            LinearProgressIndicator(
                                progress = completionCounter.toFloat() / mainTaskWithSubTasks.subTasks.size,
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

                    if (mainTaskWithSubTasks.subTasks.isNotEmpty()) {
                        SubTasks(
                            mainTaskWithSubTasks = mainTaskWithSubTasks,
                            completionCounter,
                            updateMainTaskWithSubTasks
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MainTaskPriorityIndicator(
    mainTaskWithSubTasks: MainTaskWithSubTasks
) {
    Box(
        modifier = Modifier
            .clip(RectangleShape)
            .background(
                when (mainTaskWithSubTasks.mainTask.priority) {
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
        SwipeToDismissBoxValue.StartToEnd -> Color.Transparent
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
        Spacer(modifier = Modifier)

        if (direction == SwipeToDismissBoxValue.EndToStart) Icon(
            imageVector = Icons.Filled.Archive,
            contentDescription = "Archive"
        )
    }
}

@Composable
fun SubTasks(
    mainTaskWithSubTasks: MainTaskWithSubTasks,
    completionCounter: Int,
    updateMainTaskWithSubTasks: (MainTaskWithSubTasks) -> Unit
) {
    val hapticFeedback = LocalHapticFeedback.current

    var completion by rememberSaveable { mutableIntStateOf(completionCounter) }

    mainTaskWithSubTasks.subTasks.forEach { task ->
        SubTaskComponent(
            subTask = task,
            isCompleted = task.isCompleted,
            onCompletion = {
                task.isCompleted = !task.isCompleted

                if (task.isCompleted) {
                    completion++
                } else {
                    completion--
                }

                mainTaskWithSubTasks.mainTask.isCompleted =
                    (completion == mainTaskWithSubTasks.subTasks.size)

                updateMainTaskWithSubTasks(mainTaskWithSubTasks)
                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
            }
        )
    }
}

@Composable
fun MainTaskDropDownMenu(
    mainTaskWithSubTasks: MainTaskWithSubTasks,
    navigateToTaskEditScreen: (Long) -> Unit,
    deleteMainTaskWithSubTasks: (MainTaskWithSubTasks) -> Unit
) {
    var isDropDownMenuExpanded by rememberSaveable { mutableStateOf(false) }
    var isDeletionDialogDisplayed by rememberSaveable { mutableStateOf(false) }

    if (isDeletionDialogDisplayed) {
        DeletionDialog(
            mainTaskWithSubTasks = mainTaskWithSubTasks,
            deleteMainTaskWithSubTasks = deleteMainTaskWithSubTasks,
            dismissDialog = { isDeletionDialogDisplayed = false }
        )
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
            TextButton(onClick = {
                isDropDownMenuExpanded = false
                navigateToTaskEditScreen(mainTaskWithSubTasks.mainTask.mainTaskId)
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
    mainTaskWithSubTasks: MainTaskWithSubTasks,
    deleteMainTaskWithSubTasks: (MainTaskWithSubTasks) -> Unit,
    dismissDialog: () -> Unit
) {
    ConfirmationDialog(
        title = "Delete",
        contentText = "This will delete the task permanently. You cannot undo this action.",
        confirmButtonText = "Delete",
        confirm = { deleteMainTaskWithSubTasks(mainTaskWithSubTasks) },
        onDismissRequest = dismissDialog
    )
}





