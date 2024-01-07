package com.je.playground.view.taskview.taskeditor.datetimerangepicker

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.NavigateBefore
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.je.playground.R
import com.je.playground.view.theme.regularText
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Year

@Composable
fun DateRangePicker(
    startDate : LocalDate?,
    endDate : LocalDate?,
    onStartDateValueChange : (LocalDate?) -> Unit,
    onEndDateValueChange : (LocalDate?) -> Unit,
    clearDates : () -> Unit
) {
    var showStartDateSelection by rememberSaveable { mutableStateOf(false) }
    var showEndDateSelection by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(
                top = 6.dp,
                bottom = 6.dp
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.background,
                shape = RectangleShape
            )
        ) {
            TextField(
                value = startDate?.toString() ?: "",
                onValueChange = { },
                label = { Text(text = "Start Date") },
                enabled = false,
                textStyle = regularText(MaterialTheme.colorScheme.onPrimary),
                colors = TextFieldDefaults.colors(
                    disabledLabelColor = MaterialTheme.colorScheme.onSecondary,
                    focusedLabelColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.secondaryContainer,
                    cursorColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer
                ),
                singleLine = true,
                modifier = Modifier
                    .weight(1f)
                    .clickable { showStartDateSelection = !showStartDateSelection }
            )

            TextField(
                value = endDate?.toString() ?: "",
                onValueChange = { },
                label = { Text(text = "End Date") },
                enabled = false,
                textStyle = regularText(MaterialTheme.colorScheme.onPrimary),
                trailingIcon = {
                    if (startDate != null) {
                        Icon(
                            imageVector = Icons.Filled.Clear,
                            contentDescription = "Clear text-field.",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier
                                .size(16.dp)
                                .clickable {
                                    clearDates()
                                }
                        )
                    }
                },
                colors = TextFieldDefaults.colors(
                    disabledLabelColor = if (startDate != null) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.background,
                    focusedLabelColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSecondary,
                    cursorColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer
                ),
                singleLine = true,
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        if (startDate != null) {
                            showEndDateSelection = !showEndDateSelection
                        }
                    }
            )
        }
    }

    if (showStartDateSelection) {
        DateSelectionDialog(
            title = "Select start date",
            startDate = startDate,
            endDate = endDate,
            startDateOrEndDate = true,
            onDismissRequest = { showStartDateSelection = !showStartDateSelection },
            onSave = {
                showStartDateSelection = !showStartDateSelection
                onStartDateValueChange(it)
            },
            onCancel = { showStartDateSelection = !showStartDateSelection }
        )
    }

    if (showEndDateSelection) {
        DateSelectionDialog(
            title = "Select end date",
            startDate = startDate,
            endDate = endDate,
            startDateOrEndDate = false,
            onDismissRequest = { showEndDateSelection = !showEndDateSelection },
            onSave = {
                showEndDateSelection = !showEndDateSelection
                onEndDateValueChange(it)
            },
            onCancel = { showEndDateSelection = !showEndDateSelection }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateSelectionDialog(
    title : String,
    startDate : LocalDate?,
    endDate : LocalDate?,
    startDateOrEndDate : Boolean,
    onDismissRequest : () -> Unit,
    onSave : (LocalDate) -> Unit,
    onCancel : () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
            .wrapContentSize()
    ) {
        Column {
            Text(
                text = title,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 12.dp,
                        top = 12.dp
                    )
            )

            MonthAndYearComponent(
                startDate = startDate,
                endDate = endDate,
                startDateOrEndDate = startDateOrEndDate,
                onSave = onSave,
                onCancel = onCancel
            )
        }
    }
}

@Composable
fun MonthAndYearComponent(
    startDate : LocalDate?,
    endDate : LocalDate?,
    startDateOrEndDate : Boolean,
    onSave : (LocalDate) -> Unit,
    onCancel : () -> Unit
) {
    val hapticFeedback = LocalHapticFeedback.current

    var date : LocalDate by remember { mutableStateOf(LocalDate.now()) }
    var newStartDate by rememberSaveable { mutableStateOf(startDate) }
    var newEndDate by rememberSaveable { mutableStateOf(endDate) }

    Column(
        modifier = Modifier
            //.background(MaterialTheme.colorScheme.primaryContainer)
            .wrapContentSize()
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = {
                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                date = date.minusMonths(1)
                if (date.month.ordinal == 11) date = date.minusYears(1)
            }) {
                Icon(
                    imageVector = Icons.Filled.NavigateBefore,
                    contentDescription = stringResource(R.string.previous_month),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Text(
                text = "${
                    date.month
                        .toString()
                        .substring(0..2)
                        .lowercase()
                        .replaceFirstChar { it.uppercase() }
                } ${date.year}",
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )

            IconButton(onClick = {
                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                date = date.plusMonths(1)
                if (date.month.ordinal == 0) date = date.plusYears(1)
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
            startDate = newStartDate,
            endDate = newEndDate,
            onDateChosen = {
                if (startDateOrEndDate) {
                    if (newEndDate == null) {
                        newStartDate = it
                    } else if (newEndDate!!.isEqual(it) || newEndDate!!.isAfter(it)) {
                        newStartDate = it
                    }
                } else {
                    if (newStartDate == null || newStartDate!!.isEqual(it) || newStartDate!!.isBefore(it)) {
                        newEndDate = it
                    }
                }
            }
        )

        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        ) {
            TextButton(
                onClick = { onCancel() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "Cancel")
            }

            TextButton(
                onClick = {
                    if (startDateOrEndDate) {
                        newStartDate?.let { onSave(it) }
                    } else {
                        newEndDate?.let { onSave(it) }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "Ok")
            }
        }
    }
}

@Composable
fun MonthGridComponent(
    date : LocalDate,
    startDate : LocalDate?,
    endDate : LocalDate?,
    onDateChosen : (LocalDate) -> Unit
) {
    Column {
        val firstDayOfMonth = LocalDate.of(
            date.year,
            date.month,
            1
        ).dayOfWeek.ordinal

        var dayCounter = -(firstDayOfMonth) + 1

        val lengthOfMonth = LocalDate
            .now()
            .lengthOfMonth()

        val monthGridButton = @Composable { day : Int ->
            if (day > 0 && day <= date.month.length(Year.of(date.year).isLeap)
            ) {
                MonthGridButton(
                    date = LocalDate.of(
                        date.year,
                        date.month,
                        day
                    ),
                    startDate = startDate,
                    endDate = endDate,
                    isSelected = LocalDate.now() == LocalDate.of(
                        date.year,
                        date.month,
                        day
                    ),
                    onDateChosen = onDateChosen
                )
            } else {
                Spacer(
                    modifier = Modifier
                        .size(40.dp)
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            DayOfWeek.entries
                .forEach {
                    Text(
                        text = it.name
                            .first()
                            .toString(),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.size(40.dp)
                    )
                }
        }

        while (dayCounter < lengthOfMonth) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
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
    date : LocalDate,
    startDate : LocalDate?,
    endDate : LocalDate?,
    isSelected : Boolean,
    onDateChosen : (LocalDate) -> Unit
) {
    val hapticFeedback = LocalHapticFeedback.current

    TextButton(
        onClick = {
            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
            onDateChosen(date)
        },
        enabled = true,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (startDate != null && endDate == null && startDate.isEqual(date)) {
                Color(0xFFFFAB00)
            } else if (startDate != null && endDate != null && startDate.isBefore(date.plusDays(1)) && endDate.isAfter(date.minusDays(1))) {
                Color(0xFFFFAB00)
            } else {
                Color.Transparent
            }
        ),
        shape = CircleShape,
        modifier = Modifier
            .size(40.dp)
    ) {
        Text(
            text = date.dayOfMonth.toString(),
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

    return if ((date.dayOfMonth >= LocalDate.now().dayOfMonth && date.month.ordinal == currentMonth.ordinal || date.month.ordinal > currentMonth.ordinal) || date.year > currentYear
    ) {
        MaterialTheme.colorScheme.onPrimary
    } else MaterialTheme.colorScheme.onSecondaryContainer
}