package com.je.playground.designsystem.datetimepickers

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.je.playground.ui.theme.PlaygroundTheme
import com.je.playground.ui.theme.ThemePreviews
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit

@Composable
fun DateTimeRangePicker(
    startDateLabel: String,
    startTimeLabel: String,
    endDateLabel: String,
    endTimeLabel: String,
    startDate: LocalDate?,
    startTime: LocalTime?,
    endDate: LocalDate?,
    endTime: LocalTime?,
    onStartDateSelected: (LocalDate?) -> Unit,
    onStartTimeSelected: (LocalTime) -> Unit,
    onEndDateSelected: (LocalDate?) -> Unit,
    onEndTimeSelected: (LocalTime) -> Unit,
) {
    Column {
        DateTimePicker(
            dateLabel = startDateLabel,
            timeLabel = startTimeLabel,
            date = startDate,
            time = startTime,
            onDateSelected = { newStartDate ->
                var newEndDate: LocalDate? = null
                var newEndTime: LocalTime? = null

                if (endDate != null && endDate.isBefore(newStartDate)) {
                    newEndDate = newStartDate
                }

                if (startTime != null && endTime != null) {
                    if (startTime.isAfter(endTime)) {
                        newEndTime = startTime.plusHours(1)

                        if (newEndTime == LocalTime.MIDNIGHT) {
                            newEndDate = newStartDate?.plusDays(1)
                        }
                    }
                }

                newStartDate.let(onStartDateSelected)
                newEndDate?.let(onEndDateSelected)
                newEndTime?.let(onEndTimeSelected)
            },
            onTimeSelected = { newStartTime ->
                var newEndDate: LocalDate? = null
                var newEndTime: LocalTime? = null

                if (endTime != null &&
                    startDate != null &&
                    startDate == endDate &&
                    endTime.isBefore(newStartTime)
                ) {
                    newEndTime = newStartTime.plusHours(1).truncatedTo(ChronoUnit.HOURS)

                    if (newEndTime == LocalTime.MIDNIGHT) {
                        newEndDate = endDate.plusDays(1)
                    }
                }

                newEndDate?.let(onEndDateSelected)
                newStartTime.let(onStartTimeSelected)
                newEndTime?.let(onEndTimeSelected)
            },
        )

        startDate?.let {
            DateTimePicker(
                leadingIcon = { Spacer(modifier = Modifier.size(24.dp)) },
                dateLabel = endDateLabel,
                timeLabel = endTimeLabel,
                date = endDate,
                time = endTime,
                dateRequired = false,
                onDateSelected = { newEndDate ->
                    var newStartDate: LocalDate? = null

                    if (startDate.isAfter(newEndDate)) {
                        newStartDate = newEndDate
                    }

                    newStartDate?.let(onStartDateSelected)
                    newEndDate.let(onEndDateSelected)
                },
                onTimeSelected = { newEndTime ->
                    var newStartDate: LocalDate? = null
                    var newEndDate: LocalDate? = null
                    var newStartTime: LocalTime? = null

                    if (endDate == null) {
                        if (startTime != null && newEndTime.isBefore(startTime)) {
                            newEndDate = startDate.plusDays(1)
                        } else {
                            newEndDate = startDate
                        }
                    }

                    if (startTime == null || startDate == endDate && startTime.isAfter(newEndTime)) {
                        newStartTime = newEndTime.minusHours(1).truncatedTo(ChronoUnit.HOURS)

                        if (newStartTime.hour == 23) {
                            newStartDate = startDate.minusDays(1)
                        }
                    }

                    newStartDate?.let(onStartDateSelected)
                    newStartTime?.let(onStartTimeSelected)
                    newEndDate?.let(onEndDateSelected)
                    newEndTime.let(onEndTimeSelected)
                },
            )
        }
    }
}

@ThemePreviews
@Composable
fun DateTimeRangePickerEmptyPreview() {
    PlaygroundTheme {
        Surface {
            HorizontalDivider(modifier = Modifier.padding(top = 16.dp))

            DateTimeRangePicker(
                startDateLabel = "Select a start date",
                startTimeLabel = "Select a start time",
                endDateLabel = "Select an end date",
                endTimeLabel = "Select an end time",
                startDate = null,
                startTime = null,
                endDate = null,
                endTime = null,
                onStartDateSelected = {},
                onStartTimeSelected = {},
                onEndDateSelected = {},
                onEndTimeSelected = {},
            )

            HorizontalDivider(modifier = Modifier.padding(bottom = 16.dp))
        }
    }
}

@ThemePreviews
@Composable
fun DateTimeRangePickerWithStartDatePreview() {
    PlaygroundTheme {
        Surface {
            HorizontalDivider(modifier = Modifier.padding(top = 16.dp))

            DateTimeRangePicker(
                startDateLabel = "Select a start date",
                startTimeLabel = "Select a start time",
                endDateLabel = "Select an end date",
                endTimeLabel = "Select an end time",
                startDate = LocalDate.now(),
                startTime = null,
                endDate = null,
                endTime = null,
                onStartDateSelected = {},
                onStartTimeSelected = {},
                onEndDateSelected = {},
                onEndTimeSelected = {},
            )

            HorizontalDivider(modifier = Modifier.padding(bottom = 16.dp))
        }
    }
}
