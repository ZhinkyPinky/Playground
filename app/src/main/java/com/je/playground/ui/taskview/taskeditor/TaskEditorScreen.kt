package com.je.playground.ui.taskview.taskeditor

import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.je.playground.database.tasks.entity.Task
import com.je.playground.database.tasks.entity.TaskGroup
import com.je.playground.database.tasks.entity.TaskGroupWithTasks
import com.je.playground.ui.sharedcomponents.NoteComponent
import com.je.playground.ui.taskview.dateTimeToString
import com.je.playground.ui.taskview.taskeditor.datetimerangepicker.DateRangePicker
import com.je.playground.ui.taskview.taskeditor.datetimerangepicker.TimeRangePicker
import com.je.playground.ui.taskview.viewmodel.Priority
import com.je.playground.ui.taskview.viewmodel.TaskTypeV2
import com.je.playground.ui.taskview.viewmodel.TasksUiState
import com.je.playground.ui.taskview.viewmodel.TasksViewModel
import com.je.playground.ui.theme.title
import java.time.LocalDate

@Composable
fun TaskEditorScreenV2(
    tasksViewModel : TasksViewModel,
    onBackPress : () -> Unit,
) {
    val tasksUiState : TasksUiState

    TaskEditorScreenV2(
        onBackPress = onBackPress,
        insertTaskGroup = {},
        updateTaskGroup = {}
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskEditorScreenV2(
    taskGroupWithTasks : TaskGroupWithTasks = TaskGroupWithTasks(
        TaskGroup(
            title = "",
            type = TaskTypeV2.RegularTask.ordinal,
            priority = 1
        ),
        mutableListOf(
            Task(
                taskGroupId = -1L,
                title = "Test",
                note = "The quick brown fox jumped over the lazy dog." +
                        "The quick brown fox jumped over the lazy dog." +
                        "The quick brown fox jumped over the lazy dog." +
                        "The quick brown fox jumped over the lazy dog.",
                startDate = LocalDate
                    .now()
                    .plusDays(5)
            )
        )
    ),
    onBackPress : () -> Unit,
    insertTaskGroup : () -> Unit,
    updateTaskGroup : () -> Unit
) {
    val context = LocalContext.current

    val taskGroup = taskGroupWithTasks.taskGroup
    val tasks = taskGroupWithTasks.tasks

    var showTaskGroupEditorDialog by rememberSaveable { mutableStateOf(false) }
    if (showTaskGroupEditorDialog) {
        TaskGroupEditorDialog(
            taskGroup = taskGroup,
            onSave = {},
            onDismissRequest = {
                showTaskGroupEditorDialog = false
            }
        )
    }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            text = "Edit",
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 22.sp,
                            maxLines = 1,
                            textAlign = TextAlign.Start,

                            modifier = Modifier
                                .padding(
                                    start = 0.dp,
                                    top = 4.dp,
                                    end = 8.dp,
                                    bottom = 4.dp
                                )
                                .weight(1f)
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBackPress) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.padding(end = 2.dp)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    actions = {
                        IconButton(
                            onClick = {
                                if (taskGroup.title != "") {
                                    onBackPress()
                                } else Toast
                                    .makeText(
                                        context,
                                        "Need title",
                                        Toast.LENGTH_SHORT
                                    )
                                    .show()
                            },
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Save,
                                contentDescription = "Save task",
                                tint = MaterialTheme.colorScheme.onPrimary,
                            )
                        }
                    })
                Divider(
                    color = MaterialTheme.colorScheme.background
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.spacedBy(1.dp),
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(paddingValues)
        ) {
            var isGroup by rememberSaveable {
                mutableStateOf(tasks.size > 1)
            }

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .height(IntrinsicSize.Min)
                    .fillMaxWidth()
            ) {
                Button(
                    onClick = { isGroup = false },
                    shape = MaterialTheme.shapes.small.copy(CornerSize(0)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isGroup) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = "Single",
                        tint = if (isGroup) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.onSecondaryContainer,
                    )
                    Text(
                        text = "Single"
                    )
                }

                Button(
                    onClick = {
                        if (!isGroup) {
                            showTaskGroupEditorDialog = true
                        }
                        isGroup = true
                    },
                    shape = MaterialTheme.shapes.small.copy(CornerSize(0)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (!isGroup) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = "Single",
                        tint = if (isGroup) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.primaryContainer,
                    )
                    Text(text = "Group")
                }
            }

            if (isGroup) {
                TaskGroupEditor(
                    taskGroup = taskGroup,
                    tasks = tasks,
                    showTaskGroupEditorDialog = {
                        showTaskGroupEditorDialog = !showTaskGroupEditorDialog
                    }
                )
            } else {
                TaskEditor(
                    isGroup = isGroup,
                    taskGroup = taskGroup,
                    task = Task(
                        taskGroupId = taskGroup.taskGroupId,
                        title = ""
                    )
                )
            }
        }
    }
}

@Composable
fun TaskGroupEditor(
    taskGroup : TaskGroup,
    tasks : MutableList<Task>,
    showTaskGroupEditorDialog : () -> Unit
) {
    var showTaskEditorDialog by rememberSaveable { mutableStateOf(false) }
    if (showTaskEditorDialog) {
        TaskEditorDialog(
            task = Task(
                taskGroupId = taskGroup.taskGroupId,
                title = ""
            ),
            taskGroup = taskGroup,
            isNewTask = true,
            onSave = {
                tasks.add(it)
                showTaskEditorDialog = !showTaskEditorDialog
            },
            onDismissRequest = { showTaskEditorDialog = !showTaskEditorDialog }
        )
    }

    var title by rememberSaveable { mutableStateOf(taskGroup.title) }
    var priority by rememberSaveable { mutableIntStateOf(taskGroup.priority) }
    var note by rememberSaveable { mutableStateOf(taskGroup.note) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
    ) {
        Text(
            text = "Group",
            modifier = Modifier
                .weight(1f)
        )

        IconButton(
            onClick = showTaskGroupEditorDialog
        ) {
            Icon(
                imageVector = Icons.Filled.Edit,
                contentDescription = "Edit task group information",
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(16.dp)
            )
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(MaterialTheme.colorScheme.primaryContainer)
    ) {
        Box(
            modifier = Modifier
                .clip(RectangleShape)
                .background(
                    when (taskGroup.priority) {
                        0 -> Color.Red
                        1 -> Color(0xFFFFAB00)
                        2 -> Color(0xFF00C853)
                        else -> Color.Transparent
                    }
                )
                .fillMaxHeight()
                .width(2.dp)
        )

        Column(
            modifier = Modifier
                .wrapContentHeight()
        ) {
            Text(
                text = if (title == "") "Title*" else title,
                style = title(if (title == "") MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onPrimaryContainer),
                textAlign = TextAlign.Start,
            )

            PrioritySliderComponent(
                onPriorityChanged = {
                    priority = Priority.valueOf(it).ordinal
                    taskGroup.priority = priority
                },
                modifier = Modifier.padding(
                    top = 6.dp,
                    bottom = 6.dp
                )
            )

            NoteEditComponent(onValueChange = {
                note = it
                taskGroup.note = note
            })
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(
                start = 12.dp
            )
    ) {
        Text(
            text = "Tasks",
            modifier = Modifier
                .weight(1f)
        )

        IconButton(
            onClick = { showTaskEditorDialog = !showTaskEditorDialog },
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "New Task",
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }

    tasks.forEach { task ->
        var isExpanded by rememberSaveable {
            mutableStateOf(false)
        }

        Column(
            modifier = Modifier
                .clickable { isExpanded = !isExpanded }
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(
                    start = 12.dp,
                    end = 12.dp,
                    top = 6.dp,
                    bottom = 6.dp
                )
                .wrapContentHeight()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text(
                        text = task.title,
                        style = title(MaterialTheme.colorScheme.onPrimary),
                        textAlign = TextAlign.Start,
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
                }

                IconButton(onClick = {

                }) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = "Note"
                    )
                }
            }

            task.note?.let {
                NoteComponent(
                    note = it,
                    isExpanded = isExpanded,
                    modifier = Modifier.padding(
                        top = 6.dp,
                        bottom = 6.dp
                    )
                )
            }
        }
    }
}

@Composable
fun TaskEditor(
    isGroup : Boolean,
    taskGroup : TaskGroup,
    task : Task,
) {
    var title by rememberSaveable { mutableStateOf(task.title) }
    var note by rememberSaveable { mutableStateOf(task.note) }
    var startDate by rememberSaveable { mutableStateOf(task.startDate) }
    var startTime by rememberSaveable { mutableStateOf(task.startTime) }
    var endDate by rememberSaveable { mutableStateOf(task.endDate) }
    var endTime by rememberSaveable { mutableStateOf(task.endTime) }

    Column(
        modifier = Modifier
            .wrapContentHeight()
    ) {
        TextFieldComponent(
            labelText = "Title*",
            value = title,
            isSingleLine = true,
            onValueChange = {
                title = it
                task.title = it
            }
        )

        if (!isGroup) {
            var priority by rememberSaveable {
                mutableIntStateOf(taskGroup.priority)
            }

            PrioritySliderComponent(
                onPriorityChanged = {
                    priority = Priority.valueOf(it).ordinal
                    taskGroup.priority = priority
                },
                modifier = Modifier.padding(
                    top = 6.dp,
                    bottom = 6.dp
                )
            )
        }

        NoteEditComponent(onValueChange = {
            note = it
            task.note = note
        })

        DateRangePicker(
            startDate = startDate,
            endDate = endDate,
            onStartDateValueChange = {
                startDate = it
                task.startDate = startDate

                Log.i(
                    "startDate",
                    "$startDate"
                )
            },
            onEndDateValueChange = {
                endDate = it
                task.endDate = endDate

                Log.i(
                    "endDate",
                    "$endDate"
                )
            },
        )

        TimeRangePicker(
            startTime = startTime,
            endTime = endTime,
            onStartTimeValueChange = {
                startTime = it
                task.startTime = startTime
            },
            onEndTimeValueChange = {
                if ((startTime?.isBefore(it) == true)) {

                } else {
                    endTime = it
                    task.endTime = endTime
                }
            }
        )
    }
}
