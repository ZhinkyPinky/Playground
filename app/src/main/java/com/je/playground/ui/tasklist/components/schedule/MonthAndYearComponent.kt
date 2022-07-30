package com.je.playground.ui.tasklist.components.schedule

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NavigateBefore
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import com.je.playground.R
import java.time.LocalDate

@Composable
fun MonthAndYearComponent() {
    val hapticFeedback = LocalHapticFeedback.current

    var year by remember {
        mutableStateOf(LocalDate.now().year)
    }

    var month by remember {
        mutableStateOf(LocalDate.now().month)
    }

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
                    tint = MaterialTheme.colors.secondary
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
                color = MaterialTheme.colors.secondary,
            )

            IconButton(onClick = {
                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                month = month.plus(1)
                if (month.ordinal == 0) year++
            }) {
                Icon(
                    imageVector = Icons.Filled.NavigateNext,
                    contentDescription = stringResource(R.string.next_month),
                    tint = MaterialTheme.colors.secondary
                )
            }
        }

        MonthGridComponent(
            year,
            month
        )
    }
}