package com.je.playground.ui.taskview.tasklist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.je.playground.database.tasks.entity.SubTask
import com.je.playground.ui.sharedcomponents.CheckboxComponent
import com.je.playground.ui.sharedcomponents.NoteComponent
import com.je.playground.ui.taskview.dateTimeToString
import com.je.playground.ui.theme.title

@Composable
fun SubTaskComponent(
    subTask : SubTask,
    isCompleted : Boolean,
    onCompletion : (Boolean) -> Unit
) {
    var isExpanded by rememberSaveable {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .clickable { isExpanded = !isExpanded }
            .wrapContentHeight()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CheckboxComponent(
                isChecked = isCompleted,
                onCheckedChange = onCompletion
            )

            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = subTask.title,
                    style = title(MaterialTheme.colorScheme.onPrimary),
                    textAlign = TextAlign.Start,
                )

                if (subTask.startDate != null || subTask.startTime != null || subTask.endDate != null || subTask.endTime != null) {
                    Text(
                        text = dateTimeToString(
                            startDate = subTask.startDate,
                            startTime = subTask.startTime,
                            endDate = subTask.endDate,
                            endTime = subTask.endTime
                        ),
                        color = Color(0xFFCCCCCC),
                        fontSize = 12.sp,
                        textAlign = TextAlign.Start
                    )
                }
            }
        }

        if (subTask.note != "") {
            NoteComponent(
                note = subTask.note,
                isExpanded = isExpanded,
                modifier = Modifier.padding(
                    start = 12.dp,
                    end = 12.dp,
                    top = 6.dp,
                    bottom = 6.dp
                )
            )
        }
    }
}