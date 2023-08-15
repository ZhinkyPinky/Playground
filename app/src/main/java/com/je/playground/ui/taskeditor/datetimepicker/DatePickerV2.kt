package com.je.playground.ui.taskeditor.datetimepicker

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NavigateBefore
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.je.playground.R
import com.je.playground.databaseV2.tasks.entity.TaskOccasion
import com.je.playground.databaseV2.tasks.entity.TaskWithOccasions
import com.je.playground.databaseV2.tasks.entity.WeekdaySchedule
import com.je.playground.ui.sharedcomponents.ExpandButtonComponent
import com.je.playground.ui.theme.subcontent
import com.je.playground.ui.theme.title
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.time.Year


@Composable
fun ScheduleComponent(
    taskWithOccasions : TaskWithOccasions? = null,
    onDateChosen : (LocalDate) -> Unit
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
                style = title(MaterialTheme.colorScheme.onPrimaryContainer),
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
            weekdaySchedule = taskWithOccasions?.weekdaySchedule
        )

        /*
        if (isExpanded) {
            MonthAndYearComponent(
                taskWithOccasions?.taskOccasions,
                onDateChosen
            )
        }
         */
    }

    /*
    Divider(
        color = MaterialTheme.colorScheme.background,
        thickness = if (isExpanded) 3.dp else 1.dp
    )
     */
}

@Composable
fun WeekComponent(
    weekdaySchedule : List<WeekdaySchedule>? = null
) : MutableList<DayOfWeek> {
    val weekDays : MutableList<DayOfWeek> = emptyList<DayOfWeek>().toMutableList()

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
    ) {
        DayOfWeek
            .values()
            .toList()
            .forEach { dayOfWeek ->
                weekdaySchedule?.forEach { if (it.weekday == dayOfWeek) weekDays.add(dayOfWeek) }

                val hapticFeedback = LocalHapticFeedback.current

                TextButton(
                    shape = CircleShape,
                    onClick = {
                        if (weekDays.contains(dayOfWeek)) weekDays.remove(dayOfWeek)
                        else weekDays.add(dayOfWeek)
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                    },
                    modifier = Modifier
                        .size(35.dp)
                ) {
                    Text(
                        text = dayOfWeek.name.substring(0..0),
                        style = subcontent(if (weekDays.contains(dayOfWeek)) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSecondaryContainer),
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
            }
    }

    return weekDays
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
            .size(35.dp)
    ) {
        Text(
            text = dayOfWeek.name.substring(0..0),
            style = subcontent(if (weekdayScheduleEntry != null) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSecondaryContainer),
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}

@Composable
fun MonthAndYearComponent(
    taskOccasions : List<TaskOccasion>? = null,
    onDateChosen : (LocalDate) -> Unit
) {
    val hapticFeedback = LocalHapticFeedback.current

    var year by remember {
        mutableStateOf(LocalDate.now().year)
    }

    var month by remember {
        mutableStateOf(LocalDate.now().month)
    }

    var day by remember {
        mutableStateOf(LocalDate.now().dayOfMonth)
    }

    var date : LocalDate by remember { mutableStateOf(LocalDate.now()) }

    Column {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = {
                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                month = month.minus(1)
                if (month.ordinal == 11) year--
            }) {
                Icon(
                    imageVector = Icons.Filled.NavigateBefore,
                    contentDescription = stringResource(R.string.previous_month),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Text(
                text = "${
                    month
                        .toString()
                        .substring(0..2)
                        .lowercase()
                        .replaceFirstChar { it.uppercase() }
                } $year",
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )

            IconButton(onClick = {
                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                month = month.plus(1)
                if (month.ordinal == 0) year++
            }) {
                Icon(
                    imageVector = Icons.Filled.NavigateNext,
                    contentDescription = stringResource(R.string.next_month),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        MonthGridComponent(
            date = date,
            year = year,
            month = month,
            day = day,
            onDateChosen = onDateChosen
        )
    }
}

@Composable
fun MonthGridComponent(
    date : LocalDate,
    year : Int,
    month : Month,
    day : Int?,
    taskOccasions : List<TaskOccasion>? = null,
    onDateChosen : (LocalDate) -> Unit
) {
    Column(modifier = Modifier.padding(bottom = 8.dp)) {
        val firstDayOfMonth = LocalDate.of(
            year,
            month,
            1
        ).dayOfWeek.ordinal

        var dayCounter = -(firstDayOfMonth) + 1

        val lengthOfMonth = LocalDate
            .now()
            .lengthOfMonth()

        val days = remember {
            List(42) { i -> dayCounter + i }.toMutableStateList()
        }

        days.forEach { println(it) }

        val monthGridButton = @Composable { day : Int ->
            if (day > 0 && day < month.length(Year.of(year).isLeap)
            ) {
                MonthGridButton(
                    taskOccasions = taskOccasions,
                    day = day,
                    date = date,
                    startDate = LocalDate.now(),
                    endDate = LocalDate
                        .now()
                        .plusDays(10),
                    isEnabled = dayCounter in 1..lengthOfMonth,
                    isSelected = LocalDate.now() == LocalDate.of(
                        year,
                        month,
                        day
                    ),
                    onDateChosen = onDateChosen
                )
            } else {
                Spacer(
                    modifier = Modifier
                        .size(50.dp)
                )
            }
        }

        while (dayCounter < lengthOfMonth) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                for (n in 1..7) {
                    monthGridButton(dayCounter)
                    dayCounter++
                }
            }
        }
    }
}

@Composable
fun MonthGridButton(
    taskOccasions : List<TaskOccasion>? = null,
    day : Int,
    startDate : LocalDate = LocalDate.MIN,
    endDate : LocalDate = LocalDate.MAX,
    date : LocalDate,
    isEnabled : Boolean,
    onDateChosen : (LocalDate) -> Unit,
    isSelected : Boolean
) {
    val hapticFeedback = LocalHapticFeedback.current

    TextButton(
        onClick = {
            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
            if (date.isAfter(startDate)) {

            }

            onDateChosen(
                date
            )
        },
        enabled = isEnabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (date.isAfter(startDate) && date.isBefore(endDate)) Color(0xFFFFAB00) else Color.Transparent
        ),
        shape = CircleShape,
        modifier = Modifier
            .size(50.dp)
    ) {
        Text(
            text = "$day",
            color = dateColorSelection(
                date = date
            ),
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
fun dateColorSelection(
    date : LocalDate
) : Color {
    val currentYear = LocalDate.now().year
    val currentMonth = LocalDate.now().month

    /*
    taskOccasions?.forEach { taskOccasion ->
        if (taskOccasion.dateFrom != null) {
            if (taskOccasion.dateFrom == date) {
                return when (taskOccasion.isCompleted) {
                    true -> MaterialTheme.colors.onPrimary
                    false -> if (taskOccasion.dateFrom < LocalDate.now()) MaterialTheme.colors.onSecondary else Color(0xFFFFAB00)
                }
            }
        }
    }
     */

    return if ((date.dayOfMonth >= LocalDate.now().dayOfMonth && date.month.ordinal == currentMonth.ordinal || date.month.ordinal > currentMonth.ordinal) || date.year > currentYear
    ) {
        MaterialTheme.colorScheme.onPrimary
    } else MaterialTheme.colorScheme.onSecondaryContainer
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