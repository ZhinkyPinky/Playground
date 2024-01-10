package com.je.playground.view.taskview.taskeditor

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
import com.je.playground.database.tasks.entity.MainTask
import com.je.playground.view.taskview.taskeditor.datetimerangepicker.DateRangePicker
import com.je.playground.view.taskview.taskeditor.datetimerangepicker.TimeRangePicker

@Composable
fun SingleTaskEditor(
    mainTask : MainTask,
    updateMainTask : (MainTask) -> Unit
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
            value = mainTask.title,
            isSingleLine = true,
            onValueChange = { updateMainTask(mainTask.copy(title = it)) },
            modifier = Modifier.padding(
                bottom = 6.dp
            )
        )

        NoteEditComponent(
            note = mainTask.note,
            onValueChange = { updateMainTask(mainTask.copy(note = it)) },
            modifier = Modifier.padding(
                bottom = 12.dp
            )
        )

        PrioritySliderComponent(
            priority = mainTask.priority,
            onPriorityChanged = { updateMainTask(mainTask.copy(priority = it)) },
            modifier = Modifier.padding(
                top = 6.dp,
                bottom = 6.dp
            )
        )

        DateRangePicker(
            startDate = mainTask.startDate,
            endDate = mainTask.endDate,
            onStartDateValueChange = { updateMainTask(mainTask.copy(startDate = it)) },
            onEndDateValueChange = { updateMainTask(mainTask.copy(endDate = it)) },
            clearDates = {
                updateMainTask(
                    mainTask.copy(
                        startDate = null,
                        endDate = null
                    )
                )
            }
        )

        TimeRangePicker(
            startTime = mainTask.startTime,
            endTime = mainTask.endTime,
            onStartTimeValueChange = { updateMainTask(mainTask.copy(startTime = it)) },
            onEndTimeValueChange = { updateMainTask(mainTask.copy(endTime = it)) }
        )

        Box(
            modifier = Modifier
                .clip(RectangleShape)
                .background(
                    when (mainTask.priority) {
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