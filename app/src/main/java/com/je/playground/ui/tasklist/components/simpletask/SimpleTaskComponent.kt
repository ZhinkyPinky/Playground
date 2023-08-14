package com.je.playground.ui.tasklist.components.simpletask

import androidx.compose.runtime.Composable
import com.je.playground.databaseV2.tasks.entity.Task
import com.je.playground.databaseV2.tasks.entity.TaskOccasion
import com.je.playground.databaseV2.tasks.entity.TaskWithOccasions

@Composable
fun SimpleTaskComponent(
    taskWithOccasions : TaskWithOccasions,
    updateTaskOccasion : (TaskOccasion) -> Unit,
    deleteTask : (Task) -> Unit
) {
    taskWithOccasions.simpleTask?.let { simpleTask ->
        /*
        MainTaskComponent(
            taskWithOccasions = taskWithOccasions,
            updateTaskOccasion = updateTaskOccasion,
            deleteTask = deleteTask,
            subContent = if (simpleTask.note != null) mutableListOf(
                {
                    simpleTask.note?.let { NoteComponent(note = it) }
                }) else mutableListOf(),
        )
        */
    }


    /*
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
     */
}


//.align { size, space -> (((space + 25) - size).toFloat() / 2f).toInt() }