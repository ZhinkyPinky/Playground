package com.je.playground.ui.tasklist.components.shared

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
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
import java.util.StringJoiner

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
            .padding(top = 1.dp)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(if (isExpanded) MaterialTheme.colors.primaryVariant else MaterialTheme.colors.primary)
                .then(mainRowModifier)
        )

        {
            Box(
                modifier = Modifier
                    .clip(RectangleShape)
                    .background(
                        when (taskWithOccasions.simpleTask?.priority) {
                            0 -> Color.Red
                            1 -> Color(0xFFFFAB00)
                            2 -> MaterialTheme.colors.onPrimary
                            else -> Color.Transparent
                        }
                    )
                    .height(60.dp)
                    .width(2.dp)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            ) {
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
                if (subContent.size > 0) {
                    ExpandButtonComponent(
                        isExpanded,
                        Modifier.padding(end = 6.dp)
                    ) {
                        isExpanded = !isExpanded
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                    }
                } else {
                    Spacer(
                        modifier = Modifier
                            .padding(end = 6.dp)
                            .height(49.dp)
                    )
                }

            }
        }

        if (isExpanded) {
            subContent?.let { SubContentComponent(content = it) }
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
    val stringJoiner = StringJoiner(", ")

    if (date != null) {
        if (date.year == currentYear && date.dayOfYear != currentDay) {
            stringJoiner.add(date.dayOfWeek
                                 .toString()
                                 .substring(0..2)
                                 .lowercase()
                                 .replaceFirstChar { it.uppercase() })
        }

        if (date.year != currentYear || date.dayOfYear >= (currentDay + 7) || date.dayOfYear < currentDay) {
            stringJoiner.add("${
                date.month
                    .toString()
                    .substring(0..2)
                    .lowercase()
                    .replaceFirstChar { it.uppercaseChar() }
            } ${date.dayOfMonth}")
        }

        if (date.year != currentYear) {
            stringJoiner.add("${date.year}")
        }
    }

    if (time != null) {
        stringJoiner.add(time.format(DateTimeFormatter.ofPattern("HH:mm")))
    }

    return stringJoiner.toString()
}
