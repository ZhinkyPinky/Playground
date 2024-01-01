package com.je.playground.view.schedule

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
import com.je.playground.view.theme.subcontent
import java.time.DayOfWeek

@Composable
fun WeekComponent(
    selectedWeekDays : List<DayOfWeek>,
    insertWeekdayScheduleEntry : (DayOfWeek) -> Unit,
    deleteWeekdayScheduleEntry : (DayOfWeek) -> Unit
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
                WeekdayComponent(
                    dayOfWeek,
                    selectedWeekDays.contains(dayOfWeek),
                    insertWeekdayScheduleEntry,
                    deleteWeekdayScheduleEntry
                )
            }
    }
}

@Composable
fun WeekdayComponent(
    dayOfWeek : DayOfWeek,
    isSelected : Boolean,
    insertWeekdayScheduleEntry : (DayOfWeek) -> Unit,
    deleteWeekdayScheduleEntry : (DayOfWeek) -> Unit
) {
    val hapticFeedback = LocalHapticFeedback.current

    TextButton(
        shape = CircleShape,
        onClick = {
            if (isSelected) {
                deleteWeekdayScheduleEntry(dayOfWeek)
            } else {
                insertWeekdayScheduleEntry(dayOfWeek)
            }

            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
        },
        modifier = Modifier
            .size(36.dp)
    ) {
        Text(
            text = dayOfWeek.name.substring(0..0),
            style = subcontent(if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondary), //TODO: Fix color
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}