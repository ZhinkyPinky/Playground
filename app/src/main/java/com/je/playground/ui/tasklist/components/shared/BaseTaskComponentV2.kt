package com.je.playground.ui.tasklist.components.shared

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.je.playground.databaseV2.tasks.entity.TaskWithOccasions
import com.je.playground.ui.sharedcomponents.ExpandButtonComponent
import com.je.playground.ui.theme.title
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

/**
 * Base component for tasks to be shown in the task-list.
 * Takes a list of main-content composables to be shown in the main row and a list of sub-content
 * composables to be shown when expanded.
 */
@Composable
fun BaseTaskComponentV2(
    title : String,
    taskWithOccasions : TaskWithOccasions,
    mainRowModifier : Modifier = Modifier,
    mainContent : MutableList<@Composable () -> Unit>? = null,
    subContent : MutableList<@Composable () -> Unit>? = null,
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

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(if (isExpanded) MaterialTheme.colors.primaryVariant else MaterialTheme.colors.primary)
                .then(mainRowModifier)
                .padding(
                    start = 20.dp,
                    top = 2.dp,
                    bottom = 2.dp
                )
        )

        {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = title(MaterialTheme.colors.secondary),
                    textAlign = TextAlign.Start,
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
                    )
                }
            }

            mainContent?.let { MainContentComponent(content = it) }

            if (subContent != null) {
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
            subContent?.let { SubContentComponent(content = it) }

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
            "${
                date.dayOfWeek
                    .toString()
                    .substring(0..2)
                    .lowercase()
                    .replaceFirstChar { it.uppercase() }
            }"

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
