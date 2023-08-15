package com.je.playground.ui.taskeditor.v2

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.je.playground.databaseV2.tasks.entity.TaskGroup
import com.je.playground.databaseV2.tasks.entity.TaskGroupWithTasks
import com.je.playground.databaseV2.tasks.entity.TaskV2
import com.je.playground.ui.taskeditor.NoteEditComponent
import com.je.playground.ui.taskeditor.PrioritySliderComponent
import com.je.playground.ui.taskeditor.TextFieldComponent
import com.je.playground.ui.taskeditor.daterangepicker.DateRangePicker
import com.je.playground.ui.tasklist.viewmodel.Priority
import com.je.playground.ui.tasklist.viewmodel.TaskTypeV2
import com.je.playground.ui.tasklist.viewmodel.TasksUiState
import com.je.playground.ui.tasklist.viewmodel.TasksViewModel

@Composable
fun TaskEditorScreenV2(
    tasksViewModel : TasksViewModel,
    onBackPress : () -> Unit,
) {
    val tasksUiState : TasksUiState

    TaskEditorScreenV2(
        onBackPress = onBackPress,
        insertTask = {},
        updateTask = {}
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskEditorScreenV2(
    taskGroupWithTasks : TaskGroupWithTasks = TaskGroupWithTasks(
        TaskGroup(
            title = "",
            type = TaskTypeV2.RegularTask.ordinal,
        ),
        mutableListOf(
            TaskV2(
                taskGroupId = -1L,
                title = ""
            )
        )
    ),
    onBackPress : () -> Unit,
    insertTask : () -> Unit,
    updateTask : () -> Unit
) {
    val context = LocalContext.current

    val taskGroup = taskGroupWithTasks.taskGroup
    val tasks = taskGroupWithTasks.tasks

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
                    onClick = { isGroup = true },
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
                    tasks = tasks
                )
            } else {
                TaskEditor(
                    isGroup = isGroup,
                    taskGroup = taskGroup,
                    task = tasks.first()
                )
            }
            /*
            TaskTypeComponent(
                taskType = taskType,
                onValueChange = { taskType = it }
            )

            if (TaskType
                    .values()
                    .any { it.name == taskType }
            ) {
                TextFieldComponent(
                    labelText = "Title*",
                    value = title,
                    isSingleLine = true,
                    onValueChange = { title = it }
                )

                Divider()

                when (taskType) {
                    "SimpleTask" -> SimpleTaskEditComponent(
                        context = context,
                        note = note,
                        dateFrom = dateFrom,
                        dateTo = dateTo,
                        timeFrom = timeFrom,
                        timeTo = timeTo,
                        onPriorityChanged = { priority = it },
                        onNoteChanged = { note = it },
                        onDateFromChanged = {
                            dateFrom = it
                            if (dateTo?.isBefore(dateFrom) == true) {
                                dateTo = dateFrom
                            }
                        },
                        onDateToChanged = {
                            dateTo = it
                            if (dateFrom?.isAfter(dateTo) == true) {
                                dateFrom = dateTo
                            }
                        },
                        onTimeFromChanged = {
                            if (dateFrom == null) dateFrom = LocalDate.now();
                            timeFrom = it
                        },
                        onTimeToChanged = {
                            if (dateTo == null) dateTo = dateFrom;
                            timeTo = it
                        }
                    )

                    "ExerciseProgram" -> ExerciseProgramEditComponent()
                }
            }
             */
        }
    }
}

@Composable
fun TaskGroupEditor(
    taskGroup : TaskGroup,
    tasks : MutableList<TaskV2>
) {
    var title by rememberSaveable { mutableStateOf(taskGroup.title) }
    var priority by rememberSaveable { mutableIntStateOf(taskGroup.priority) }
    var note by rememberSaveable { mutableStateOf(taskGroup.note) }

    Text(
        text = "Group",
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
            .fillMaxWidth()
            .padding(
                start = 12.dp,
                top = 12.dp,
                bottom = 12.dp
            )
    )

    Column {
        TextFieldComponent(
            labelText = "Title*",
            value = title,
            isSingleLine = true,
            onValueChange = {
                title = it
                taskGroup.title = title
            }
        )

        PrioritySliderComponent(onPriorityChanged = {
            priority = Priority.valueOf(it).ordinal
            taskGroup.priority = priority
        })


        NoteEditComponent(onValueChange = {
            note = it
            taskGroup.note = note
        })

    }

    Divider()

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
    ) {
        Text(
            text = "Tasks",
            modifier = Modifier
                .weight(1f)
                .padding(
                    start = 12.dp,
                    top = 12.dp,
                    bottom = 12.dp
                )
        )

        IconButton(
            onClick = {
                tasks.add(
                    TaskV2(
                        taskGroupId = -1L,
                        title = ""
                    )
                )
            },
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "New Task",
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }

    tasks.forEach { task ->
        TaskEditor(
            isGroup = true,
            taskGroup = taskGroup,
            task = task
        )
        Divider()
    }

}


@Composable
fun TaskEditor(
    isGroup : Boolean,
    taskGroup : TaskGroup,
    task : TaskV2,
) {
    var title by rememberSaveable { mutableStateOf(task.title) }
    var note by rememberSaveable { mutableStateOf(task.note) }
    var startDate by rememberSaveable { mutableStateOf(task.startDate) }
    var startTime by rememberSaveable { mutableStateOf(task.startTime) }
    var endDate by rememberSaveable { mutableStateOf(task.endDate) }
    var endTime by rememberSaveable { mutableStateOf(task.endTime) }

    Column {
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

            PrioritySliderComponent(onPriorityChanged = {
                priority = Priority.valueOf(it).ordinal
                taskGroup.priority = priority
            })
        }

        NoteEditComponent(onValueChange = {
            note = it
            task.note = note
        })

        DateRangePicker(
            startDate = task.startDate,
            endDate = task.endDate,
            onStartDateValueChange = {
                startDate = it
                task.startDate = startDate
            },
            onEndDateValueChange = {
                endDate = it
                task.endDate = endDate
            },
        )

        /*
        DateTimePicker(
            context = LocalContext.current,
            dateLabel = "Start date",
            timeLabel = "Start time",
            savedDate = task.dateFrom,
            savedTime = task.timeFrom,
            onDateValueChange = { task.dateFrom = it },
            onTimeValueChange = {}
        )

        //if (task.dateFrom != null || task.timeFrom != null) {
            DateTimePicker(
                context = LocalContext.current,
                dateLabel = "End date",
                timeLabel = "End Time",
                savedDate = task.dateTo,
                savedTime = task.timeTo,
                onDateValueChange = { },
                onTimeValueChange = { }
            )
        //}
         */
    }
}
