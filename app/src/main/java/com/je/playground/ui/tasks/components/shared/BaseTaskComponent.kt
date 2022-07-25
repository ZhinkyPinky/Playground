package com.je.playground.ui.tasks.components.shared

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
import com.je.playground.ui.theme.title
import java.time.Clock
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun BaseTaskComponent(
    title : String,
    priority : Int? = 3,
    dateFrom : LocalDateTime? = null,
    dateTo : LocalDateTime? = null,
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
                .padding(end = 8.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = title(MaterialTheme.colors.secondary),
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .padding(
                            start = 20.dp,
                            top = 7.dp,
                            end = 8.dp,
                            bottom = 0.dp
                        )
                )

                Text(
                    text =
                    if (dateFrom != null && dateTo != null) "${dateTimeToString(dateFrom)} - ${dateTimeToString(dateTo)}"
                    else if (dateFrom != null) "$dateFrom"
                    else "",
                    color = MaterialTheme.colors.secondaryVariant,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .padding(
                            start = 20.dp,
                            top = 0.dp,
                            end = 8.dp,
                            bottom = 7.dp
                        )
                )

            }

            mainContent?.invoke()

            ExpandButtonComponent(isExpanded) {
                isExpanded = !isExpanded
                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
            }
        }

        Divider(
            modifier = Modifier.padding(top = 1.dp),
            color = MaterialTheme.colors.primaryVariant,
        )

        if (isExpanded) subContent?.invoke()
    }
}

fun dateTimeToString(localDateTime : LocalDateTime) : String {
    var date = localDateTime.format(DateTimeFormatter.ofPattern("hh:mm"))

    if (localDateTime.dayOfYear != LocalDateTime.now().dayOfYear) date += " ${
        localDateTime.month
            .toString()
            .substring(0..2)
            .lowercase()
            .replaceFirstChar { it.uppercaseChar() }
    } ${localDateTime.dayOfMonth}"

    if (localDateTime.year != LocalDateTime.now().year) date += ", ${localDateTime.year}"

    return date
}


