package com.je.playground.deprecated

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.je.playground.database.tasks.entity.Task
import com.je.playground.deprecated.datetimerangepicker.DateRangePicker
import com.je.playground.deprecated.datetimerangepicker.TimeRangePicker
import com.je.playground.designsystem.component.task.taskeditor.NoteEditComponent
import com.je.playground.designsystem.component.task.taskeditor.PrioritySliderComponent
import com.je.playground.designsystem.component.task.taskeditor.TextFieldComponent
import com.je.playground.feature.tasks.editor.TaskEditorEvent
import com.je.playground.feature.tasks.editor.TaskField
import com.je.playground.feature.tasks.editor.update

@Composable
fun SingleTaskEditor(
    task: Task, onEvent: (TaskEditorEvent) -> Unit
) {
    Surface {
        Column(modifier = Modifier.padding(12.dp)) {
            TextFieldComponent(
                label = "Title*",
                placeholder = "Enter a title for the group",
                value = task.title,
                isSingleLine = true,
                onValueChange = { TaskField.Title(it).update(onEvent) },
            )

            NoteEditComponent(
                note = task.note,
                onValueChange = { TaskField.Note(it).update(onEvent) },
            )

            PrioritySliderComponent(
                priority = task.priority,
                onPriorityChanged = { TaskField.Priority(it).update(onEvent) },
            )

            DateRangePicker(startDate = task.startDate,
                endDate = task.endDate,
                onStartDateValueChange = { TaskField.EndDate(it).update(onEvent) },
                onEndDateValueChange = { TaskField.StartDate(it).update(onEvent) },
                clearDates = {
                    TaskEditorEvent.updateTask(
                        onEvent,
                        listOf(
                            TaskField.StartDate(null),
                            TaskField.EndDate(null)
                        )
                    )
                })

            TimeRangePicker(startTime = task.startTime,
                endTime = task.endTime,
                onStartTimeValueChange = { TaskField.StartTime(it).update(onEvent) },
                onEndTimeValueChange = { TaskField.EndTime(it).update(onEvent) })

            Box(
                modifier = Modifier
                    .clip(RectangleShape)
                    .background(
                        when (task.priority) {
                            0 -> com.je.playground.ui.theme.lowPriority
                            1 -> com.je.playground.ui.theme.mediumPriority
                            2 -> com.je.playground.ui.theme.highPriority
                            else -> Color.Transparent
                        }
                    )
                    .fillMaxHeight()
                    .width(2.dp)
            )
        }
    }
}