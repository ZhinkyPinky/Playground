package com.je.playground.designsystem.component.taskeditor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.je.playground.database.tasks.entity.Task
import com.je.playground.designsystem.component.PrioritySliderComponent
import com.je.playground.designsystem.component.TextFieldComponent
import com.je.playground.designsystem.component.datetimerangepicker.DateRangePicker
import com.je.playground.designsystem.component.datetimerangepicker.TimeRangePicker

@Composable
fun SingleTaskEditor(
    task : Task,
    updateMainTask : (Task) -> Unit
) {
    Divider(
        color = MaterialTheme.colorScheme.background
    )

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(
                start = 12.dp,
                end = 12.dp,
                top = 6.dp,
                bottom = 6.dp
            )
            .wrapContentHeight()
    ) {
        TextFieldComponent(
            label = "Title*",
            placeholder = "Enter a title for the group",
            value = task.title,
            isSingleLine = true,
            onValueChange = { updateMainTask(task.copy(title = it)) },
            modifier = Modifier.padding(
                bottom = 6.dp
            )
        )

        NoteEditComponent(
            note = task.note,
            onValueChange = { updateMainTask(task.copy(note = it)) },
            modifier = Modifier.padding(
                bottom = 12.dp
            )
        )

        PrioritySliderComponent(
            priority = task.priority,
            onPriorityChanged = { updateMainTask(task.copy(priority = it)) },
            modifier = Modifier.padding(
                top = 6.dp,
                bottom = 6.dp
            )
        )

        DateRangePicker(
            startDate = task.startDate,
            endDate = task.endDate,
            onStartDateValueChange = { updateMainTask(task.copy(startDate = it)) },
            onEndDateValueChange = { updateMainTask(task.copy(endDate = it)) },
            clearDates = {
                updateMainTask(
                    task.copy(
                        startDate = null,
                        endDate = null
                    )
                )
            }
        )

        TimeRangePicker(
            startTime = task.startTime,
            endTime = task.endTime,
            onStartTimeValueChange = { updateMainTask(task.copy(startTime = it)) },
            onEndTimeValueChange = { updateMainTask(task.copy(endTime = it)) }
        )

        Box(
            modifier = Modifier
                .clip(RectangleShape)
                .background(
                    when (task.priority) {
                        0 -> Color(0xFF00C853)
                        1 -> Color(0xFFFFAB00)
                        2 -> Color.Red
                        else -> Color.Transparent
                    }
                )
                .fillMaxHeight()
                .width(2.dp)
        )
    }
}