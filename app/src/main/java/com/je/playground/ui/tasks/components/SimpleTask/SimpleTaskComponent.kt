package com.je.playground.ui.tasks.components.SimpleTask

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.je.playground.database.tasks.SimpleTask
import com.je.playground.ui.tasks.components.NoteComponent
import com.je.playground.ui.tasks.components.shared.BaseTaskComponent
import com.je.playground.ui.tasks.components.shared.CheckboxComponent
import com.je.playground.ui.tasks.components.shared.MainContentComponent
import com.je.playground.ui.tasks.components.shared.PriorityIconComponent

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SimpleTaskComponent(
    simpleTask : SimpleTask,
    deleteSimpleTask : (SimpleTask) -> Unit
) {
    val hapticFeedback = LocalHapticFeedback.current

    var isCompleted by remember {
        mutableStateOf(false)
    }


    val swipeableState = rememberSwipeableState(initialValue = "On")
    val anchors = mapOf(
        0f to "On",
        150f to "Off",
        300f to "Locked"
    )

    Box(
        modifier = Modifier.swipeable(
            enabled = true,
            state = swipeableState,
            anchors = anchors,
            thresholds = { _, _ -> FractionalThreshold(0.5f) },
            orientation = Orientation.Horizontal
        )
    ) {
        BaseTaskComponent(
            title = simpleTask.name,
            priority = simpleTask.priority,
            dateFrom = simpleTask.dateFrom,
            dateTo = simpleTask.dateTo,
            mainContent = {
                MainContentComponent(
                    content = listOf(
                        {
                            if (simpleTask.priority != -1) {
                                PriorityIconComponent(
                                    simpleTask.priority,
                                    Modifier.padding(end = 16.dp)

                                )
                            }
                        },
                        {
                            CheckboxComponent(isCompleted) {
                                isCompleted = !isCompleted
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                            }
                        }
                    )
                )
            },
            subContent = {
                NoteComponent(note = simpleTask.note)
            }
        )
    }
}

//.align { size, space -> (((space + 25) - size).toFloat() / 2f).toInt() }