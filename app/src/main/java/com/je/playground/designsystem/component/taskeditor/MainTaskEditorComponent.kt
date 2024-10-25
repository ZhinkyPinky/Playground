package com.je.playground.designsystem.component.taskeditor

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.je.playground.database.tasks.entity.Task
import com.je.playground.designsystem.component.NoteComponent
import com.je.playground.designsystem.dateTimeToString
import com.je.playground.designsystem.theme.title

@Composable
fun MainTaskEditorComponent(
    task : Task,
    toggleMainTaskEditorDialog : (Long) -> Unit,
) {
    var isExpanded by rememberSaveable {
        mutableStateOf(false)
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(start = 12.dp)
    ) {
        Text(
            text = "Group",
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier
                .weight(1f)
        )

        IconButton(
            onClick = { toggleMainTaskEditorDialog(task.mainTaskId) }
        ) {
            Icon(
                imageVector = Icons.Filled.Edit,
                contentDescription = "Edit main task",
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(16.dp)
            )
        }
    }

    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .clickable {
                isExpanded = !isExpanded
            }
            .padding(
                start = 6.dp,
                top = 6.dp,
                bottom = 6.dp
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
                    start = 6.dp,
                    end = 12.dp,
                    top = 6.dp,
                    bottom = 6.dp
                )
        ) {
            Text(
                text = if (task.title == "") "Title*" else task.title,
                style = title(if (task.title == "") MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onPrimaryContainer),
                textAlign = TextAlign.Start
            )

            if (task.startDate != null || task.startTime != null || task.endDate != null || task.endTime != null) {
                Text(
                    text = dateTimeToString(
                        startDate = task.startDate,
                        startTime = task.startTime,
                        endDate = task.endDate,
                        endTime = task.endTime
                    ),
                    color = Color(0xFFCCCCCC),
                    fontSize = 12.sp,
                    textAlign = TextAlign.Start
                )
            }

            if (task.note != "") {
                NoteComponent(
                    note = task.note,
                    isExpanded = isExpanded
                )
            }
        }
    }
}
