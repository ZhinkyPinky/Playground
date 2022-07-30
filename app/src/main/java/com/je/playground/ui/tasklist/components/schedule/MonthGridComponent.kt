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
import java.time.LocalDate
import java.time.Month

@Composable fun MonthGridComponent(
    year : Int,
    month : Month
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
                            color = if (((dayCounter) <= 0) || (dayCounter > month.length(LocalDate.now().isLeapYear))) {
                                Color.Transparent
                            } else if ((dayCounter >= LocalDate.now().dayOfMonth && month.ordinal == currentMonth.ordinal || month.ordinal > currentMonth.ordinal) || year > currentYear) {
                                MaterialTheme.colors.secondary
                            } else {
                                MaterialTheme.colors.secondaryVariant
                            },
                            textAlign = TextAlign.Center
                        )

                        dayCounter++
                    }
                }
            }
        }
    }
}