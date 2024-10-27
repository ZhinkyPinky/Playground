package com.je.playground.designsystem.component.task.tasklist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.je.playground.database.tasks.entity.SubTask
import com.je.playground.designsystem.component.common.CheckboxComponent
import com.je.playground.designsystem.component.task.DateTimeRangeText
import com.je.playground.designsystem.component.task.NoteComponent
import com.je.playground.ui.theme.PlaygroundTheme
import com.je.playground.ui.theme.ThemePreviews
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun SubTaskComponent(
    subTask: SubTask,
    isCompleted: Boolean,
    onCompletion: (Boolean) -> Unit
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }

    Surface {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable { isExpanded = !isExpanded }
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(
                    end = 16.dp,
                    bottom = 12.dp
                )
        ) {
            CheckboxComponent(
                isChecked = isCompleted,
                onCheckedChange = onCompletion,
                modifier = Modifier.align(Alignment.Top)
            )

            Column(modifier = Modifier.padding(top = 12.dp)) {
                Text(
                    text = subTask.title,
                    style = MaterialTheme.typography.titleMedium
                )

                DateTimeRangeText(
                    startDate = subTask.startDate,
                    startTime = subTask.startTime,
                    endDate = subTask.endDate,
                    endTime = subTask.endTime
                )
                if (subTask.note != "") {
                    NoteComponent(
                        note = subTask.note,
                        isExpanded = isExpanded,
                    )
                }
            }
        }
    }
}

@ThemePreviews
@Composable
fun SubTaskComponentPreview() {
    val subTask = SubTask(
        title = "Do other thing",
        note = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam blandit porta fringilla. Proin eu odio eget dolor placerat facilisis. Mauris aliquam purus vitae dolor fringilla congue. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus.",
        startDate = LocalDate.now().plusDays(50),
        endDate = LocalDate.now().plusDays(58),
        startTime = LocalTime.now(),
        endTime = LocalTime.now().plusHours(1)
    )

    PlaygroundTheme {
        SubTaskComponent(
            subTask = subTask,
            isCompleted = subTask.isCompleted,
            onCompletion = { subTask.isCompleted != subTask.isCompleted }
        )
    }
}