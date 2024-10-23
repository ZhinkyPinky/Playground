package com.je.playground.designsystem.component.schedule

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.je.playground.designsystem.component.ExpandButtonComponent
import com.je.playground.designsystem.theme.title
import java.time.DayOfWeek

@Composable
fun ScheduleComponent(
    selectedWeekDays : List<DayOfWeek>,
    insertWeekdayScheduleEntry : (DayOfWeek) -> Unit,
    deleteWeekdayScheduleEntry : (DayOfWeek) -> Unit
) {
    val hapticFeedback = LocalHapticFeedback.current

    var isExpanded by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .background(if (isExpanded) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.primaryContainer)
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
                style = title(MaterialTheme.colorScheme.onPrimary),
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
            selectedWeekDays = selectedWeekDays,
            insertWeekdayScheduleEntry = insertWeekdayScheduleEntry,
            deleteWeekdayScheduleEntry = deleteWeekdayScheduleEntry
        )

        if (isExpanded) {
            MonthAndYearComponent(
                //taskWithOccasions?.taskOccasions
            )
        }
    }
}


