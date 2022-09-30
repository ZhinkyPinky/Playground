package com.je.playground.ui.tasklist.components.schedule

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.je.playground.databaseV2.tasks.entity.TaskWithOccasions
import com.je.playground.databaseV2.tasks.entity.WeekdaySchedule
import com.je.playground.ui.sharedcomponents.ExpandButtonComponent
import com.je.playground.ui.theme.title

@Composable
fun ScheduleComponent(
    taskWithOccasions : TaskWithOccasions,
    insertWeekdayScheduleEntry : (WeekdaySchedule) -> Unit,
    deleteWeekdayScheduleEntry : (WeekdaySchedule) -> Unit
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
            .padding(
                start = 6.dp,
                end = 6.dp
            )
    ) {
        Row {
            Text(
                text = "Schedule",
                style = title(MaterialTheme.colors.secondary),
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .weight(1f)
                    .padding(
                        start = 16.dp,
                        top = 8.dp
                    )
            )

            ExpandButtonComponent(
                isExpanded = isExpanded
            ) {
                isExpanded = !isExpanded
                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
            }
        }

        WeekComponent(
            taskWithOccasions = taskWithOccasions,
            insertWeekdayScheduleEntry = insertWeekdayScheduleEntry,
            deleteWeekdayScheduleEntry = deleteWeekdayScheduleEntry
        )

        if (isExpanded) {
            MonthAndYearComponent(taskWithOccasions.taskOccasions)
        }
    }

    Divider(
        color = MaterialTheme.colors.primaryVariant,
        thickness = if (isExpanded) 3.dp else 1.dp
    )
}


