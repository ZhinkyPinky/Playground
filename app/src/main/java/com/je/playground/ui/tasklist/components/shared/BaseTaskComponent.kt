package com.je.playground.ui.tasklist.components.shared

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.je.playground.databaseV2.tasks.entity.TaskWithOccasions
import com.je.playground.ui.theme.title
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun BaseTaskComponent(
    title : String,
    priority : Int? = 3,
    taskWithOccasions : TaskWithOccasions,
    mainContent : (@Composable () -> Unit)? = null,
    subContent : (@Composable () -> Unit)? = null
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
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = title(MaterialTheme.colors.secondary),
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .padding(
                            start = 20.dp,
                            top = 0.dp,
                            end = 8.dp,
                            bottom = 0.dp
                        )
                )

                if (taskWithOccasions.taskOccasions.isNotEmpty()) {
                    val dateFrom = taskWithOccasions.taskOccasions.first().dateFrom
                    val timeFrom = taskWithOccasions.taskOccasions.first().timeFrom
                    val dateTo = taskWithOccasions.taskOccasions.first().dateTo
                    val timeTo = taskWithOccasions.taskOccasions.first().timeTo

                    Text(
                        text = if (dateTo != null || timeTo != null)
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
                        modifier = Modifier
                            .padding(
                                start = 20.dp,
                                top = 0.dp,
                                end = 8.dp,
                                bottom = 0.dp
                            )

                    )
                }
            }

            mainContent?.invoke()

            ExpandButtonComponent(
                isExpanded,
                Modifier.padding(end = 6.dp)
            ) {
                isExpanded = !isExpanded
                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
            }
        }

        Divider(
            modifier = Modifier.padding(
                top = 1.dp,
                bottom = 1.dp
            ),
            color = MaterialTheme.colors.primaryVariant,
        )

        if (isExpanded) subContent?.invoke()
    }
}

fun dateTimeToString(
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
            }, "

        if (date.year != currentYear
            ||
            date.dayOfYear >= currentDay && date.dayOfYear < currentDay + 7
        ) formattedString += "${
            date.month
                .toString()
                .substring(0..2)
                .lowercase()
                .replaceFirstChar { it.uppercaseChar() }
        } ${date.dayOfMonth}, "

        if (date.year != currentYear) formattedString += "${date.year}, "
    }

    formattedString += time?.format(DateTimeFormatter.ofPattern("HH:mm"))

    return formattedString
}


