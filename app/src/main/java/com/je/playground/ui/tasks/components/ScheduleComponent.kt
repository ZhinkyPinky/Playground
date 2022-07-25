package com.je.playground.ui.tasks.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NavigateBefore
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.je.playground.R
import com.je.playground.ui.tasks.components.shared.ExpandButtonComponent
import com.je.playground.ui.theme.subcontent
import com.je.playground.ui.theme.title
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun ScheduleComponent() {
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
        Row(modifier = Modifier.padding(end = 8.dp)) {
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

            ExpandButtonComponent(isExpanded = isExpanded) {
                isExpanded = !isExpanded
                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
            }
        }

        WeekComponent(
            DayOfWeek
                .values()
                .toList()
        )

        if (isExpanded) {
            MonthAndYearComponent()
        }
    }

    Divider(
        color = MaterialTheme.colors.primaryVariant,
        thickness = if (isExpanded) 3.dp else 1.dp
    )
}

@Composable
fun WeekComponent(week : List<DayOfWeek>) {
    val hapticFeedback = LocalHapticFeedback.current

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(
                start = 16.dp,
                end = 27.dp,
                bottom = 8.dp
            )
            .fillMaxWidth()
    ) {
        week.forEach { day ->
            var selected by remember {
                mutableStateOf(false)
            }

            Text(
                text = day.name.substring(0..0),
                style = subcontent(if (selected) MaterialTheme.colors.secondary else MaterialTheme.colors.secondaryVariant),
                modifier = Modifier
                    .clickable {
                        selected = !selected
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                    }
            )
        }
    }
}

@Composable
fun MonthAndYearComponent() {
    val hapticFeedback = LocalHapticFeedback.current

    Column {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 8.dp)
        ) {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Filled.NavigateBefore,
                    contentDescription = stringResource(R.string.previous_month),
                    tint = MaterialTheme.colors.secondary
                )
            }

            Text(
                text = LocalDate
                    .now()
                    .format(DateTimeFormatter.ofPattern("MMM yyyy")),
                color = MaterialTheme.colors.secondary,
            )

            IconButton(onClick = { hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress) }) {
                Icon(
                    imageVector = Icons.Filled.NavigateNext,
                    contentDescription = stringResource(R.string.next_month),
                    tint = MaterialTheme.colors.secondary
                )
            }
        }

        MonthGrid()
    }
}

@Composable fun MonthGrid() {
    val hapticFeedback = LocalHapticFeedback.current

    Column(modifier = Modifier.padding(bottom = 8.dp)) {
        var currentDayOfWeek = LocalDate.of(
            LocalDate.now().year,
            LocalDate.now().month,
            LocalDate.now().dayOfMonth
        ).dayOfWeek.ordinal

        val year = LocalDate.now().year
        val month = LocalDate.now().month
        val firstDayOfMonth = LocalDate.of(
            year,
            month,
            1
        ).dayOfWeek


        var dayCounter = 1
        val lengthOfMonth = LocalDate
            .now()
            .lengthOfMonth()
        while (dayCounter < lengthOfMonth) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 2.dp,
                        end = 5.dp
                    )
            ) {
                for (n in 1..7) {
                    TextButton(
                        onClick = {
                            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                        },
                        enabled = dayCounter - firstDayOfMonth.ordinal > 0,
                        shape = CircleShape,
                        modifier = Modifier
                            .size(50.dp)
                    ) {
                        Text(
                            text = "${dayCounter - firstDayOfMonth.ordinal}",
                            color = if ((dayCounter - firstDayOfMonth.ordinal) <= 0) {
                                Color.Transparent
                            } else if (dayCounter < LocalDate.now().dayOfMonth + firstDayOfMonth.ordinal) {
                                MaterialTheme.colors.secondaryVariant
                            } else {
                                MaterialTheme.colors.secondary
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


