package com.je.playground.ui.tasklist.components.schedule

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.je.playground.databaseV2.tasks.entity.TaskOccasion
import java.time.LocalDate
import java.time.Month

@Composable fun MonthGridComponent(
    year : Int,
    month : Month,
    taskOccasions : List<TaskOccasion>
) {
    val hapticFeedback = LocalHapticFeedback.current

    Column(modifier = Modifier.padding(bottom = 8.dp)) {
        val currentYear = LocalDate.now().year
        val currentMonth = LocalDate.now().month
        var currentDayOfWeek = LocalDate.of(
            LocalDate.now().year,
            LocalDate.now().month,
            LocalDate.now().dayOfMonth
        ).dayOfWeek.ordinal

        val firstDayOfMonth = LocalDate.of(
            year,
            month,
            1
        ).dayOfWeek

        var dayCounter = -(firstDayOfMonth.ordinal) + 1
        val lengthOfMonth = LocalDate
            .now()
            .lengthOfMonth()
        while (dayCounter < lengthOfMonth) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                for (n in 1..7) {
                    TextButton(
                        onClick = {
                            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                        },
                        enabled = dayCounter > 0,
                        shape = CircleShape,
                        modifier = Modifier
                            .size(50.dp)
                    ) {
                        Text(
                            text = "$dayCounter",
                            color = dateColorSelection(
                                day = dayCounter,
                                month = month,
                                year = year,
                                taskOccasions = taskOccasions
                            )

                            /*
                            if (((dayCounter) <= 0) || (dayCounter > month.length(LocalDate.now().isLeapYear))) {
                                Color.Transparent
                            } else if (containsDate(
                                    LocalDate.of(
                                        year,
                                        month,
                                        dayCounter
                                    ),
                                    taskOccasions
                                )
                            ) {
                                MaterialTheme.colors.onSecondary
                            } else if ((dayCounter >= LocalDate.now().dayOfMonth && month.ordinal == currentMonth.ordinal || month.ordinal > currentMonth.ordinal) || year > currentYear) {
                                MaterialTheme.colors.secondary
                            } else {
                                MaterialTheme.colors.secondaryVariant
                            }
                            */,
                            textAlign = TextAlign.Center
                        )

                        dayCounter++
                    }
                }
            }
        }
    }
}

@Composable
fun dateColorSelection(
    day : Int,
    month : Month,
    year : Int,
    taskOccasions : List<TaskOccasion>
) : Color {
    if (day <= 0 || day > month.length(LocalDate.now().isLeapYear)) return Color.Transparent

    val currentYear = LocalDate.now().year
    val currentMonth = LocalDate.now().month

    val date = LocalDate.of(
        year,
        month,
        day
    )

    taskOccasions.forEach { taskOccasion ->
        if (taskOccasion.dateFrom != null) {
            if (taskOccasion.dateFrom == date) {
                return when (taskOccasion.isCompleted) {
                    true -> MaterialTheme.colors.onPrimary
                    false -> if (taskOccasion.dateFrom < LocalDate.now()) MaterialTheme.colors.onSecondary else Color(0xFFFFAB00)
                }
            }
        }
    }

    return if ((date.dayOfMonth >= LocalDate.now().dayOfMonth && month.ordinal == currentMonth.ordinal || month.ordinal > currentMonth.ordinal) || year > currentYear) {
        MaterialTheme.colors.secondary
    } else MaterialTheme.colors.secondaryVariant
}

fun containsDate(
    date : LocalDate,
    taskOccasions : List<TaskOccasion>
) : Boolean {
    taskOccasions.forEach { taskOccasion ->
        print("$date : ${taskOccasion.dateFrom}")
        if (taskOccasion.dateFrom == date) return true
    }

    return false
}