package com.je.playground.ui.taskview.tasklist

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.je.playground.database.tasks.entity.Task
import com.je.playground.database.tasks.entity.TaskGroupWithTasks
import com.je.playground.ui.sharedcomponents.NoteComponent
import com.je.playground.ui.taskview.dateTimeToString
import com.je.playground.ui.theme.title
import kotlin.math.roundToInt

enum class DragAnchors {
    Start,
    End
}

@OptIn(
    ExperimentalFoundationApi::class
)
@Composable
fun TaskGroupComponent(
    taskGroupWithTasks : TaskGroupWithTasks,
    deleteTask : (Task) -> Unit,
) {
    val hapticFeedback = LocalHapticFeedback.current

    val isExpanded by rememberSaveable {
        mutableStateOf(false)
    }

    var completionCounter = 0
    for (i in 0 until taskGroupWithTasks.tasks.size) {
        if (taskGroupWithTasks.tasks[i].isCompleted) {
            completionCounter++
        }
    }

    var completion by rememberSaveable {
        mutableIntStateOf(
            completionCounter
        )
    }

    val density = LocalDensity.current
    val contentSizePx = with(density) { LocalConfiguration.current.screenWidthDp.dp.toPx() }

    val anchors = DraggableAnchors {
        DragAnchors.Start at 0f
        DragAnchors.End at -contentSizePx
    }

    val state = remember {
        AnchoredDraggableState(
            initialValue = DragAnchors.Start,
            anchors = anchors,
            positionalThreshold = { distance : Float -> distance * 0.7f },
            velocityThreshold = { with(density) { 100.dp.toPx() } },
            animationSpec = tween()
        )
    }

    if (state.currentValue == DragAnchors.End) {
        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
        //deleteTask(taskWithOccasions.task)
    }

    Box(
        modifier = Modifier
            .background(
                color = if (!isExpanded) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer
            )
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(
                6.dp
            )
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )
            .onSizeChanged {
                state.updateAnchors(
                    DraggableAnchors {
                        DragAnchors
                            .values()
                            .forEach { anchor ->
                                anchor at contentSizePx * anchors.positionOf(anchor)
                            }
                    }
                )
            }
    )
    {
        Row(
            modifier = Modifier
                .height(IntrinsicSize.Max)
                .offset {
                    IntOffset(
                        x = state
                            .requireOffset()
                            .roundToInt(),
                        y = 0
                    )
                }
                .anchoredDraggable(
                    state = state,
                    enabled = taskGroupWithTasks.taskGroup.isCompleted,
                    orientation = Orientation.Horizontal
                )
        ) {
            Box(
                modifier = Modifier
                    .clip(RectangleShape)
                    .background(
                        when (taskGroupWithTasks.taskGroup.priority) {
                            0 -> Color.Red
                            1 -> Color(0xFFFFAB00)
                            2 -> Color(0xFF00C853)
                            else -> Color.Transparent
                        }
                    )
                    .fillMaxHeight()
                    .width(2.dp)
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(1.dp)
            ) {
                if (taskGroupWithTasks.tasks.size > 1) {
                    Column(
                        modifier = Modifier
                            .padding(
                                top = 12.dp,
                                start = 12.dp,
                                end = 10.dp
                            )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(bottom = 12.dp)
                        ) {
                            Text(
                                text = taskGroupWithTasks.taskGroup.title,
                                style = title(MaterialTheme.colorScheme.onPrimary),
                                textAlign = TextAlign.Start,
                                modifier = Modifier.weight(1f)
                            )

                            Icon(
                                imageVector = Icons.Filled.MoreVert,
                                contentDescription = "Note"
                            )

                            if (taskGroupWithTasks.taskGroup.startDate != null || taskGroupWithTasks.taskGroup.startTime != null || taskGroupWithTasks.taskGroup.endDate != null || taskGroupWithTasks.taskGroup.endTime != null) {
                                Text(
                                    text = dateTimeToString(
                                        startDate = taskGroupWithTasks.taskGroup.startDate,
                                        startTime = taskGroupWithTasks.taskGroup.startTime,
                                        endDate = taskGroupWithTasks.taskGroup.endDate,
                                        endTime = taskGroupWithTasks.taskGroup.endTime
                                    ),
                                    color = Color(0xFFCCCCCC),
                                    fontSize = 12.sp,
                                    textAlign = TextAlign.Start
                                )
                            }
                        }

                        LinearProgressIndicator(
                            progress = (completion.toFloat() / taskGroupWithTasks.tasks.size),
                            color = MaterialTheme.colorScheme.onPrimary,
                            trackColor = MaterialTheme.colorScheme.background,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                        )
                    }

                    taskGroupWithTasks.taskGroup.note?.let {
                        NoteComponent(
                            note = it,
                            isExpanded = isExpanded
                        )
                    }
                }

                taskGroupWithTasks.tasks.forEach { task ->
                    var isCompleted by rememberSaveable {
                        mutableStateOf(task.isCompleted)
                    }
                    TaskComponent(
                        task = task,
                        isCompleted,
                        onCompletion = {
                            isCompleted = !isCompleted
                            task.isCompleted = isCompleted

                            if (isCompleted) {
                                completion++
                            } else {
                                completion--
                            }

                            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                        }
                    )
                }
            }
        }
    }
}

