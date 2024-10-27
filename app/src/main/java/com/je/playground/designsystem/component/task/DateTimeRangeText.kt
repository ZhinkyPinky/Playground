package com.je.playground.designsystem.component.task

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.StringJoiner

@Composable
fun DateTimeRangeText(
    startDate: LocalDate?,
    endDate: LocalDate?,
    startTime: LocalTime?,
    endTime: LocalTime?
) {
    if (startDate != null || startTime != null || endDate != null || endTime != null) {
        Text(
            text = dateTimeToString(
                startDate = startDate,
                startTime = startTime,
                endDate = endDate,
                endTime = endTime
            ),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

fun dateTimeToString(
    startDate : LocalDate?,
    startTime : LocalTime?,
    endDate : LocalDate?,
    endTime : LocalTime?,
) : String {
    val stringJoinerStart = StringJoiner(", ")
    val stringJoinerEnd = StringJoiner(", ")
    val stringJoinerStartEnd = StringJoiner(" - ")

    if (startDate != null) {
        val startDateString = dateToString(startDate)

        if (startDateString != "") {
            stringJoinerStart.add(startDateString)
        }
    }

    if (startTime != null) {
        stringJoinerStart.add(timeToString(startTime))
    }

    stringJoinerStartEnd.add(stringJoinerStart.toString())

    if (endDate != null || endTime != null) {
        if (endDate != null) {
            val endDateString = dateToString(endDate)

            if (endDateString != "") {
                stringJoinerEnd.add(endDateString)
            }
        }

        if (endTime != null) {
            stringJoinerEnd.add(timeToString(endTime))
        }

        stringJoinerStartEnd.add(stringJoinerEnd.toString())
    }

    return stringJoinerStartEnd.toString()
}

private fun dateToString(date : LocalDate?) : String? {
    val currentYear = LocalDate.now().year
    val currentMonth = LocalDate.now().month
    val currentDay = LocalDate.now().dayOfYear

    val stringJoiner = StringJoiner(", ")

    if (date?.dayOfYear == currentDay) {
        return "Today"
    }

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


    return stringJoiner.toString()
}

private fun timeToString(time : LocalTime?) : String {
    if (time != null) {
        return time.format(DateTimeFormatter.ofPattern("HH:mm"))
    }

    return ""
}
