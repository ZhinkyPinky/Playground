package com.je.playground.ui.schedule

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
    taskWithOccasions : TaskWithOccasions?,
    insertWeekdayScheduleEntry : (WeekdaySchedule) -> Unit,
    deleteWeekdayScheduleEntry : (WeekdaySchedule) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(
                start = 8.dp,
                end = 8.dp,
                bottom = 8.dp
            )
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        DayOfWeek
            .values()
            .toList()
            .forEach { dayOfWeek ->
                var weekdayScheduleEntry : WeekdaySchedule? = null
                taskWithOccasions?.weekdaySchedule?.forEach { if (it.weekday == dayOfWeek) weekdayScheduleEntry = it }

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
    taskWithOccasions : TaskWithOccasions?,
    dayOfWeek : DayOfWeek,
    weekdayScheduleEntry : WeekdaySchedule?,
    insertWeekdayScheduleEntry : (WeekdaySchedule) -> Unit,
    deleteWeekdayScheduleEntry : (WeekdaySchedule) -> Unit
) {
    val hapticFeedback = LocalHapticFeedback.current

    TextButton(
        shape = CircleShape,
        onClick = {
            if (taskWithOccasions != null) {
                if (weekdayScheduleEntry == null) insertWeekdayScheduleEntry(
                    WeekdaySchedule(
                        taskWithOccasions.task.id,
                        dayOfWeek
                    )
                )
                else deleteWeekdayScheduleEntry(weekdayScheduleEntry)
            }

            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
        },
        modifier = Modifier
            .size(36.dp)
    ) {
        Text(
            text = dayOfWeek.name.substring(0..0),
            style = subcontent(if (weekdayScheduleEntry != null) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onPrimary), //TODO: Fix color
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}