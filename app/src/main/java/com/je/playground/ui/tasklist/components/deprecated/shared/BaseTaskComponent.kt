package com.je.playground.ui.tasklist.components.deprecated.shared

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
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
import com.je.playground.ui.sharedcomponents.PriorityIconComponent
import com.je.playground.ui.theme.title
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BaseTaskComponent(
    title : String,
    priority : Int? = -1,
    isCompletable : Boolean = false,
    taskWithOccasions : TaskWithOccasions,
    mainContent : (@Composable () -> Unit)? = null,
    subContent : (@Composable () -> Unit)? = null,
    updateTaskOccasion : ((TaskOccasion) -> Unit)? = null,
    deleteTask : ((Task) -> Unit)? = null
) {
    val hapticFeedback = LocalHapticFeedback.current

    var isExpanded by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMedium,
                )
            )
    ) {
        val swipeableState = rememberSwipeableState(false)
        val anchors = mapOf(
            0f to false,
            -300f to true
        )

        if (swipeableState.currentValue) {
            deleteTask?.let { it(taskWithOccasions.task) }
        }

        Box(
            modifier = Modifier
                .swipeable(
                    state = swipeableState,
                    anchors = anchors,
                    orientation = Orientation.Horizontal,
                    enabled = (isCompletable && if (taskWithOccasions.taskOccasions.isNotEmpty()) taskWithOccasions.taskOccasions.first().isCompleted else false),
                    thresholds = { _, _ -> FractionalThreshold(0.5f) }
                )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(if (isExpanded) MaterialTheme.colors.primaryVariant else MaterialTheme.colors.primary)
                    .offset {
                        IntOffset(
                            swipeableState.offset.value.roundToInt(),
                            0
                        )
                    }
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        style = title(MaterialTheme.colors.secondary),
                        textAlign = TextAlign.Start,
                        modifier = Modifier
                            .padding(
                                start = 20.dp,
                                top = 0.dp,
                                end = 8.dp,
                                bottom = 0.dp
                            )
                    )

                    if (taskWithOccasions.taskOccasions.isNotEmpty()) {
                        val dateFrom = taskWithOccasions.taskOccasions.first().dateFrom
                        val timeFrom = taskWithOccasions.taskOccasions.first().timeFrom
                        val dateTo = taskWithOccasions.taskOccasions.first().dateTo
                        val timeTo = taskWithOccasions.taskOccasions.first().timeTo

                        Text(
                            text = if (dateTo != null && timeTo != null)
                                "${
                                    dateTimeToString(
                                        dateFrom,
                                        timeFrom
                                    )
                                } - ${
                                    dateTimeToString(
                                        dateTo,
                                        timeTo
                                    )
                                }"
                            else
                                dateTimeToString(
                                    dateFrom,
                                    timeFrom
                                ),
                            color = MaterialTheme.colors.secondaryVariant,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Start,
                            modifier = Modifier
                                .padding(
                                    start = 20.dp,
                                    top = 0.dp,
                                    end = 8.dp,
                                    bottom = 0.dp
                                )

                        )
                    }
                }

                if (priority != -1) {
                    PriorityIconComponent(
                        priority,
                        Modifier.padding(end = 8.dp)
                    )
                }

                if (isCompletable) {
                    CheckboxComponent(taskWithOccasions.taskOccasions.first().isCompleted) {
                        taskWithOccasions.taskOccasions.first().isCompleted = !taskWithOccasions.taskOccasions.first().isCompleted
                        updateTaskOccasion?.let { it1 -> it1(taskWithOccasions.taskOccasions.first()) }
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                    }
                }

                //mainContent?.invoke()

                ExpandButtonComponent(
                    isExpanded,
                    Modifier.padding(end = 6.dp)
                ) {
                    isExpanded = !isExpanded
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                }
            }
        }

        Divider(
            thickness = if (isExpanded) 3.dp else 1.dp,
            color = MaterialTheme.colors.primaryVariant,
            modifier = Modifier.padding(
                top = 1.dp,
                bottom = 1.dp
            )
        )

        if (isExpanded) {
            subContent?.invoke()

            Divider(
                thickness = 3.dp,
                color = MaterialTheme.colors.primaryVariant,
                modifier = Modifier.padding(
                    top = 1.dp,
                    bottom = 1.dp
                )
            )
        }
    }
}

private fun dateTimeToString(
    date : LocalDate?,
    time : LocalTime?,
) : String {
    val currentYear = LocalDate.now().year
    val currentMonth = LocalDate.now().month
    val currentDay = LocalDate.now().dayOfYear

    var formattedString = ""

    if (date != null) {
        if (date.year == currentYear) formattedString =
            date.dayOfWeek
                .toString()
                .substring(0..2)
                .lowercase()
                .replaceFirstChar { it.uppercase() }

        if (date.year != currentYear
            ||
            date.dayOfYear >= currentDay && date.dayOfYear < currentDay + 7
        ) formattedString += ", ${
            date.month
                .toString()
                .substring(0..2)
                .lowercase()
                .replaceFirstChar { it.uppercaseChar() }
        } ${date.dayOfMonth}"

        if (date.year != currentYear) formattedString += ", ${date.year}"
    }

    if (time != null) {
        formattedString += ", ${time.format(DateTimeFormatter.ofPattern("HH:mm"))}"
    }

    return formattedString
}


