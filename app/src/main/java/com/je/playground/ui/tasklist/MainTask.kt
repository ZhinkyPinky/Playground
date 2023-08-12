package com.je.playground.ui.tasklist

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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.je.playground.databaseV2.tasks.entity.Task
import com.je.playground.databaseV2.tasks.entity.TaskOccasion
import com.je.playground.databaseV2.tasks.entity.TaskWithOccasions
import com.je.playground.ui.sharedcomponents.CheckboxComponent
import com.je.playground.ui.sharedcomponents.ExpandButtonComponent
import com.je.playground.ui.tasklist.components.shared.*
import com.je.playground.ui.theme.title
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.StringJoiner
import kotlin.math.roundToInt

enum class DragAnchors {
    Start,
    End
}

@OptIn(
    ExperimentalFoundationApi::class
)
@Composable
fun MainTaskComponent(
    taskWithOccasions : TaskWithOccasions,
    updateTaskOccasion : (TaskOccasion) -> Unit,
    deleteTask : (Task) -> Unit,
    subContent : MutableList<@Composable () -> Unit>? = null,
) {
    val hapticFeedback = LocalHapticFeedback.current

    var isExpanded by rememberSaveable {
        mutableStateOf(false)
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
        deleteTask(taskWithOccasions.task)
    }

    Box(
        modifier = Modifier
            .background(
                color = if (!isExpanded) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer
            )
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(
                start = 6.dp,
                end = 6.dp,
                top = 6.dp,
                bottom = 6.dp
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
        taskWithOccasions.simpleTask?.let { simpleTask ->
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
                        state,
                        Orientation.Horizontal
                    )
            ) {
                Box(
                    modifier = Modifier
                        .clip(RectangleShape)
                        .background(
                            when (simpleTask.priority) {
                                0 -> Color.Red
                                1 -> Color(0xFFFFAB00)
                                2 -> Color(0xFF00C853)
                                else -> Color.Transparent
                            }
                        )
                        .fillMaxHeight()
                        .width(2.dp)
                )

                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        if (taskWithOccasions.taskOccasions.size == 1) {
                            CheckboxComponent(
                                isChecked = taskWithOccasions.taskOccasions.first().isCompleted,
                                modifier = Modifier
                            ) {
                                taskWithOccasions.taskOccasions.first().isCompleted = !taskWithOccasions.taskOccasions.first().isCompleted
                                updateTaskOccasion(taskWithOccasions.taskOccasions.first())
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                            }
                        }

                        Column(
                            modifier = Modifier
                                .weight(1f)
                        ) {
                            Text(
                                text = simpleTask.name,
                                style = title(MaterialTheme.colorScheme.onPrimary),
                                textAlign = TextAlign.Start,
                            )

                            if (!taskWithOccasions.taskOccasions.isEmpty()) {
                                Text(
                                    text = dateTimeToString(
                                        startDate = taskWithOccasions.taskOccasions.first().dateFrom,
                                        startTime = taskWithOccasions.taskOccasions.first().timeFrom,
                                        endDate = taskWithOccasions.taskOccasions.first().dateTo,
                                        endTime = taskWithOccasions.taskOccasions.first().timeTo
                                    ),
                                    color = Color(0xFFCCCCCC),
                                    fontSize = 12.sp,
                                    textAlign = TextAlign.Start
                                )
                            }
                        }

                        if (subContent != null) {
                            if (subContent.size > 0) {
                                ExpandButtonComponent(
                                    isExpanded = isExpanded,
                                ) {
                                    isExpanded = !isExpanded
                                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                }
                            }
                        } else {
                            Spacer(
                                modifier = Modifier
                                    .padding(end = 6.dp)
                                    .height(49.dp)
                            )
                        }
                    }

                    if (isExpanded) {
                        subContent?.let { SubContentComponent(content = it) }
                    }
                }
            }
        }
    }
}

private fun dateTimeToString(
    startDate : LocalDate?,
    startTime : LocalTime?,
    endDate : LocalDate?,
    endTime : LocalTime?,
) : String {
    var stringJoinerStart = StringJoiner(", ")
    var stringJoinerEnd = StringJoiner(", ")
    var stringJoinerStartEnd = StringJoiner(" - ")

    if (startDate != null) {
        val startDateString = dateToString(startDate)

        if (startDateString != "") {
            stringJoinerStart.add(startDateString)
        }
    }

    if (startTime != null) {
        stringJoinerStart.add(timeToString(startTime))
    }

    stringJoinerStartEnd.add(stringJoinerStart.toString())

    if (endDate != null || endTime != null) {
        if (endDate != null) {
            val endDateString = dateToString(endDate)

            if (endDateString != "") {
                stringJoinerEnd.add(endDateString)
            }
        }

        if (endTime != null) {
            stringJoinerEnd.add(timeToString(endTime))
        }

        stringJoinerStartEnd.add(stringJoinerEnd.toString())
    }

    return stringJoinerStartEnd.toString()
}

private fun dateToString(date : LocalDate?) : String? {
    val currentYear = LocalDate.now().year
    val currentMonth = LocalDate.now().month
    val currentDay = LocalDate.now().dayOfYear

    val stringJoiner = StringJoiner(", ")

    if (date?.dayOfYear == currentDay) {
        return ""
    }

    if (date != null) {
        if (date.year == currentYear && date.dayOfYear != currentDay) {
            stringJoiner.add(date.dayOfWeek
                                 .toString()
                                 .substring(0..2)
                                 .lowercase()
                                 .replaceFirstChar { it.uppercase() })
        }

        if (date.year != currentYear || date.dayOfYear >= (currentDay + 7) || date.dayOfYear < currentDay) {
            stringJoiner.add("${
                date.month
                    .toString()
                    .substring(0..2)
                    .lowercase()
                    .replaceFirstChar { it.uppercaseChar() }
            } ${date.dayOfMonth}")
        }

        if (date.year != currentYear) {
            stringJoiner.add("${date.year}")
        }
    }


    return stringJoiner.toString()
}

private fun timeToString(time : LocalTime?) : String {
    if (time != null) {
        return time.format(DateTimeFormatter.ofPattern("HH:mm"))
    }

    return ""
}