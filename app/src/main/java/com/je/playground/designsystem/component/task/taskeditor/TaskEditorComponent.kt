package com.je.playground.designsystem.component.task.taskeditor

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.je.playground.database.tasks.entity.Task
import com.je.playground.designsystem.component.task.DateTimeRangeText
import com.je.playground.designsystem.component.task.NoteComponent
import com.je.playground.ui.theme.PlaygroundTheme
import com.je.playground.ui.theme.ThemePreviews
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun TaskEditorComponent(
    task: Task,
    toggleTaskEditorDialog: (Long) -> Unit,
) {
    var isExpanded by rememberSaveable {
        mutableStateOf(false)
    }

    Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
        Surface {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(
                    start = 16.dp,
                    top = 8.dp,
                    end = 16.dp,
                    bottom = 4.dp
                )
            ) {
                Text(
                    text = "Task",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )

                IconButton(onClick = { toggleTaskEditorDialog(task.taskId) }) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Edit task",
                    )
                }
            }
        }

        Surface {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max)
                    .clickable { isExpanded = !isExpanded }
                    .padding(
                        start = 6.dp,
                        top = 8.dp,
                        end = 16.dp,
                        bottom = 8.dp
                    )
            ) {
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

                Column(
                    modifier = Modifier
                        .wrapContentHeight()
                        .padding(
                            start = 8.dp,
                            top = 8.dp,
                            bottom = 8.dp
                        )
                ) {
                    Text(
                        text = task.title.ifBlank { "Title *" },
                        style = MaterialTheme.typography.titleMedium,
                        color = if (task.title.isBlank()) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface
                    )

                    DateTimeRangeText(
                        startDate = task.startDate,
                        endDate = task.endDate,
                        startTime = task.startTime,
                        endTime = task.endTime
                    )

                    task.note?.let {
                        if (it.isNotBlank()) {
                            NoteComponent(
                                note = it,
                                isExpanded = isExpanded
                            )
                        }
                    }
                }
            }
        }
    }
}

@ThemePreviews
@Composable
fun MainTaskEditorComponentPreview() {
    PlaygroundTheme {
        TaskEditorComponent(
            task = Task(
                title = "Do thang",
                note = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam blandit porta fringilla. Proin eu odio eget dolor placerat facilisis. Mauris aliquam purus vitae dolor fringilla congue. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus.",
                startDate = LocalDate.now().plusDays(50),
                endDate = LocalDate.now().plusDays(58),
                startTime = LocalTime.now(),
                endTime = LocalTime.now().plusHours(1)
            ),
            toggleTaskEditorDialog = {})
    }
}