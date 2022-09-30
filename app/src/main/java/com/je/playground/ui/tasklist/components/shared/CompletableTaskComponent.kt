package com.je.playground.ui.tasklist.components.shared

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.je.playground.databaseV2.tasks.entity.Task
import com.je.playground.databaseV2.tasks.entity.TaskOccasion
import com.je.playground.databaseV2.tasks.entity.TaskWithOccasions
import com.je.playground.ui.sharedcomponents.CheckboxComponent
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CompletableTaskComponent(
    title : String,
    taskWithOccasions : TaskWithOccasions,
    priority : Int,
    mainContent : MutableList<@Composable () -> Unit>,
    subContent : MutableList<@Composable () -> Unit>,
    updateTaskOccasion : (TaskOccasion) -> Unit,
    deleteTask : (Task) -> Unit
) {
    val hapticFeedback = LocalHapticFeedback.current

    mainContent.add {
        CheckboxComponent(taskWithOccasions.taskOccasions.first().isCompleted) {
            taskWithOccasions.taskOccasions.first().isCompleted = !taskWithOccasions.taskOccasions.first().isCompleted
            updateTaskOccasion(taskWithOccasions.taskOccasions.first())
            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
        }
    }

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
        BaseTaskComponentV2(
            title = title,
            taskWithOccasions = taskWithOccasions,
            mainContent = mainContent,
            subContent = subContent,
            mainRowModifier = Modifier.offset {
                IntOffset(
                    swipeableState.offset.value.roundToInt(),
                    0
                )
            })
    }
}