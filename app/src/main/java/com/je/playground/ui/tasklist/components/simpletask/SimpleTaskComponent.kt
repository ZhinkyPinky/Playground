package com.je.playground.ui.tasklist.components.simpletask

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.je.playground.databaseV2.tasks.entity.Task
import com.je.playground.databaseV2.tasks.entity.TaskOccasion
import com.je.playground.databaseV2.tasks.entity.TaskWithOccasions
import com.je.playground.ui.sharedcomponents.PriorityIconComponent
import com.je.playground.ui.tasklist.components.NoteComponent
import com.je.playground.ui.tasklist.components.shared.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SimpleTaskComponent(
    taskWithOccasions : TaskWithOccasions,
    updateTaskOccasion : (TaskOccasion) -> Unit,
    deleteTask : (Task) -> Unit
) {
    val hapticFeedback = LocalHapticFeedback.current

    val swipeableState = rememberSwipeableState(false)

    val widthPx = with(LocalDensity.current) { LocalConfiguration.current.screenWidthDp.dp.toPx() }
    val anchors = mapOf(
        0f to false,
        -widthPx to true
    )

    if (swipeableState.currentValue) {
        deleteTask(taskWithOccasions.task)
    }

    Box(
        modifier = Modifier
            .swipeable(
                state = swipeableState,
                anchors = anchors,
                orientation = Orientation.Horizontal,
                enabled = taskWithOccasions.taskOccasions.first().isCompleted,
                thresholds = { _, _ -> FractionalThreshold(0.7f) }
            )
    )
    {
        taskWithOccasions.simpleTask?.let {
            CompletableTaskComponent(
                title = it.name,
                taskWithOccasions = taskWithOccasions,
                priority = it.priority,
                mainContent = mutableListOf(
                    {
                        if (taskWithOccasions.simpleTask.priority != -1) {
                            PriorityIconComponent(
                                taskWithOccasions.simpleTask.priority,
                                Modifier.padding(end = 8.dp)
                            )
                        }
                    }),
                subContent = if (taskWithOccasions.simpleTask.note != null) mutableListOf(
                    {
                        taskWithOccasions.simpleTask.note?.let { it -> NoteComponent(note = it) }
                    }) else mutableListOf(),
                updateTaskOccasion = updateTaskOccasion,
                deleteTask = deleteTask
            )

            /*
            BaseTaskComponentV2(
                title = it.name,
                taskWithOccasions = taskWithOccasions,
                mainRowModifier = Modifier.offset {
                    IntOffset(
                        swipeableState.offset.value.roundToInt(),
                        0
                    )
                },

                mainContent = {
                    MainContentComponent(
                        content = listOf(
                            {
                                if (taskWithOccasions.simpleTask.priority != -1) {
                                    PriorityIconComponent(
                                        taskWithOccasions.simpleTask.priority,
                                        Modifier.padding(end = 8.dp)
                                    )
                                }
                            },
                            {
                                CheckboxComponent(taskWithOccasions.taskOccasions.first().isCompleted) {
                                    taskWithOccasions.taskOccasions.first().isCompleted = !taskWithOccasions.taskOccasions.first().isCompleted
                                    updateTaskOccasion(taskWithOccasions.taskOccasions.first())
                                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                }
                            }
                        )
                    )
                },

                subContent = {
                    NoteComponent(note = taskWithOccasions.simpleTask.note)
                }
            )
            */
        }
    }
}


//.align { size, space -> (((space + 25) - size).toFloat() / 2f).toInt() }