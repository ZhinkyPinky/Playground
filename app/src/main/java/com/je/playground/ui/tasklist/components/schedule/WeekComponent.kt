package com.je.playground.ui.tasklist.components.schedule

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.je.playground.databaseV2.tasks.entity.TaskWithOccasions
import com.je.playground.databaseV2.tasks.entity.WeekdaySchedule
import com.je.playground.ui.theme.subcontent
import java.time.DayOfWeek

@Composable
fun WeekComponent(
    taskWithOccasions : TaskWithOccasions,
    insertWeekdayScheduleEntry : (WeekdaySchedule) -> Unit,
    deleteWeekdayScheduleEntry : (WeekdaySchedule) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(
                bottom = 8.dp
            )
            .fillMaxWidth()
    ) {
        DayOfWeek
            .values()
            .toList()
            .forEach { dayOfWeek ->
                var weekdayScheduleEntry : WeekdaySchedule? = null
                taskWithOccasions.weekdaySchedule.forEach { if (it.weekday == dayOfWeek) weekdayScheduleEntry = it }

                WeekdayComponent(
                    taskWithOccasions,
                    dayOfWeek,
                    weekdayScheduleEntry,
                    insertWeekdayScheduleEntry,
                    deleteWeekdayScheduleEntry
                )
            }
    }
}

@Composable
fun WeekdayComponent(
    taskWithOccasions : TaskWithOccasions,
    dayOfWeek : DayOfWeek,
    weekdayScheduleEntry : WeekdaySchedule?,
    insertWeekdayScheduleEntry : (WeekdaySchedule) -> Unit,
    deleteWeekdayScheduleEntry : (WeekdaySchedule) -> Unit
) {
    val hapticFeedback = LocalHapticFeedback.current

    TextButton(
        shape = CircleShape,
        onClick = {
            if (weekdayScheduleEntry == null) insertWeekdayScheduleEntry(
                WeekdaySchedule(
                    taskWithOccasions.task.id,
                    dayOfWeek
                )
            )
            else deleteWeekdayScheduleEntry(weekdayScheduleEntry)

            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
        },
        modifier = Modifier
            .size(48.dp)
    ) {
        Text(
            text = dayOfWeek.name.substring(0..0),
            style = subcontent(if (weekdayScheduleEntry != null) MaterialTheme.colors.secondary else MaterialTheme.colors.secondaryVariant),
            modifier = Modifier
        )
    }
}