package com.je.playground.ui.taskview.taskeditor

import android.content.Context
import android.util.Log
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
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
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
import com.je.playground.ui.taskview.viewmodel.TasksUiState
import com.je.playground.ui.taskview.viewmodel.TasksViewModel
import com.je.playground.ui.theme.title
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun TaskEditorScreen(
    tasksViewModel : TasksViewModel,
    taskGroupWithTasks : TaskGroupWithTasks = TaskGroupWithTasks(
        TaskGroup(
            priority = 2,
            startDate = LocalDate
                .now()
                .plusDays(12),
            startTime = LocalTime.MIDNIGHT,
            note = "The quick brown fox jumped over the lazy dog." +
                    "The quick brown fox jumped over the lazy dog." +
                    "The quick brown fox jumped over the lazy dog." +
                    "The quick brown fox jumped over the lazy dog.",
        ),
        mutableListOf(
            Task(
                taskGroupId = -1,
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
) {
    val tasksUiState : TasksUiState
    val context = LocalContext.current

    val taskGroup = taskGroupWithTasks.taskGroup.copy()
    var taskGroupTitle by rememberSaveable { mutableStateOf(taskGroup.title) }
    var taskGroupNote by rememberSaveable { mutableStateOf(taskGroup.note) }
    var taskGroupPriority by rememberSaveable { mutableIntStateOf(taskGroup.priority) }
    var taskGroupStartDate by rememberSaveable { mutableStateOf(taskGroup.startDate) }
    var taskGroupStartTime by rememberSaveable { mutableStateOf(taskGroup.startTime) }
    var taskGroupEndDate by rememberSaveable { mutableStateOf(taskGroup.endDate) }
    var taskGroupEndTime by rememberSaveable { mutableStateOf(taskGroup.endTime) }

    val tasks = rememberSaveable(
        saver = listSaver(
            save = {
                if (it.isNotEmpty()) {
                    val first = it.first()
                    if (!canBeSaved(first)) {
                        throw IllegalStateException("${first::class} cannot be saved. By default only types which can be stored in the Bundle class can be saved.")
                    }
                }

                it.toList()
            },
            restore = { it.toMutableStateList() }
        )
    ) {
        taskGroupWithTasks.tasks
            .map { it.copy() }
            .toMutableStateList()
    }

    var showTaskGroupEditorDialog by rememberSaveable { mutableStateOf(false) }
    var showTaskEditorDialog by rememberSaveable { mutableStateOf(false) }

    var isGroup by rememberSaveable { mutableStateOf(tasks.size > 1) }

    TaskEditorContent(
        context = context,
        taskGroupWithTasks = taskGroupWithTasks,
        insertTaskGroupWithTasks = tasksViewModel::insertTaskGroupWithTasks,
        updateTaskGroupWithTasks = tasksViewModel::updateTaskGroupWithTasks,
        deleteTaskGroupWithTasks = tasksViewModel::deleteTaskGroupWithTasks,
        taskGroup = taskGroup,
        taskGroupTitle = taskGroupTitle,
        taskGroupNote = taskGroupNote,
        taskGroupPriority = taskGroupPriority,
        updateTaskGroupPriority = { taskGroupPriority = it },
        taskGroupStartDate = taskGroupStartDate,
        taskGroupStartTime = taskGroupStartTime,
        taskGroupEndDate = taskGroupEndDate,
        taskGroupEndTime = taskGroupEndTime,
        updateTaskGroup = { title, note, priority, startDate, startTime, endDate, endTime ->
            taskGroupTitle = title
            taskGroupNote = note
            taskGroupPriority = priority
            taskGroupStartDate = startDate
            taskGroupStartTime = startTime
            taskGroupEndDate = endDate
            taskGroupEndTime = endTime
        },
        tasks = tasks,
        showTaskGroupEditorDialog = showTaskGroupEditorDialog,
        toggleTaskGroupEditorDialog = { showTaskGroupEditorDialog = !showTaskGroupEditorDialog },
        showTaskEditorDialog = showTaskEditorDialog,
        toggleTaskEditorDialog = { showTaskEditorDialog = !showTaskEditorDialog },
        isGroup = isGroup,
        toggleIsGroup = { isGroup = !isGroup },
        onBackPress = onBackPress,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskEditorContent(
    context : Context,
    taskGroupWithTasks : TaskGroupWithTasks,
    insertTaskGroupWithTasks : (TaskGroupWithTasks) -> Unit,
    updateTaskGroupWithTasks : (TaskGroupWithTasks) -> Unit,
    deleteTaskGroupWithTasks : (TaskGroupWithTasks) -> Unit,
    taskGroup : TaskGroup,
    taskGroupTitle : String,
    taskGroupNote : String,
    taskGroupPriority : Int,
    updateTaskGroupPriority : (Int) -> Unit,
    taskGroupStartDate : LocalDate?,
    taskGroupStartTime : LocalTime?,
    taskGroupEndDate : LocalDate?,
    taskGroupEndTime : LocalTime?,
    updateTaskGroup : (String, String, Int, LocalDate?, LocalTime?, LocalDate?, LocalTime?) -> Unit,
    tasks : SnapshotStateList<Task>,
    showTaskGroupEditorDialog : Boolean,
    toggleTaskGroupEditorDialog : () -> Unit,
    showTaskEditorDialog : Boolean,
    toggleTaskEditorDialog : () -> Unit,
    isGroup : Boolean,
    toggleIsGroup : () -> Unit,
    onBackPress : () -> Unit
) {
    if (showTaskGroupEditorDialog) {
        TaskGroupEditorDialog(
            taskGroupTitle = taskGroupTitle,
            taskGroupNote = taskGroupNote,
            taskGroupPriority = taskGroupPriority,
            taskGroupStartDate = taskGroupStartDate,
            taskGroupStartTime = taskGroupStartTime,
            taskGroupEndDate = taskGroupEndDate,
            taskGroupEndTime = taskGroupEndTime,
            updateTaskGroup = updateTaskGroup,
            onDismissRequest = toggleTaskGroupEditorDialog
        )
    }

    Scaffold(
        topBar = { TopBar(onBackPress) },
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
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .height(IntrinsicSize.Min)
                    .fillMaxWidth()
            ) {
                Button(
                    onClick = { if (isGroup) toggleIsGroup() },
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
                            toggleTaskGroupEditorDialog()
                            toggleIsGroup()
                        }
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
                        contentDescription = "Group",
                        tint = if (isGroup) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.primaryContainer,
                    )
                    Text(text = "Group")
                }
            }

            if (isGroup) {
                TaskGroupEditor(
                    taskGroup = taskGroup,
                    taskGroupTitle = taskGroupTitle,
                    taskGroupNote = taskGroupNote,
                    taskGroupPriority = taskGroupPriority,
                    taskGroupStartDate = taskGroupStartDate,
                    taskGroupStartTime = taskGroupStartTime,
                    taskGroupEndDate = taskGroupEndDate,
                    taskGroupEndTime = taskGroupEndTime,
                    tasks = tasks,
                    toggleTaskGroupEditorDialog = toggleTaskGroupEditorDialog,
                    showTaskEditorDialog = showTaskEditorDialog,
                    toggleTaskEditorDialog = toggleTaskEditorDialog
                )
            } else {
                TaskEditor(
                    isGroup = isGroup,
                    taskGroup = taskGroup,
                    taskGroupPriority = taskGroupPriority,
                    updateTaskGroupPriority = updateTaskGroupPriority,
                    task = Task()
                )
            }
        }
    }
}

@Composable
fun TaskGroupEditor(
    taskGroup : TaskGroup,
    taskGroupTitle : String,
    taskGroupNote : String,
    taskGroupPriority : Int,
    taskGroupStartDate : LocalDate?,
    taskGroupStartTime : LocalTime?,
    taskGroupEndDate : LocalDate?,
    taskGroupEndTime : LocalTime?,
    tasks : SnapshotStateList<Task>,
    toggleTaskGroupEditorDialog : () -> Unit,
    showTaskEditorDialog : Boolean,
    toggleTaskEditorDialog : () -> Unit
) {
    if (showTaskEditorDialog) {
        TaskEditorDialog(
            task = Task(),
            taskGroup = taskGroup,
            isNewTask = true,
            onSave = {
                tasks.add(it)
                toggleTaskEditorDialog()
            },
            onDismissRequest = toggleTaskEditorDialog
        )
    }

    TaskGroupComponent(
        taskGroupTitle = taskGroupTitle,
        taskGroupNote = taskGroupNote,
        taskGroupPriority = taskGroupPriority,
        taskGroupStartDate = taskGroupStartDate,
        taskGroupStartTime = taskGroupStartTime,
        taskGroupEndDate = taskGroupEndDate,
        taskGroupEndTime = taskGroupEndTime,
        toggleTaskGroupEditorDialog = toggleTaskGroupEditorDialog,
    )

    TasksComponent(
        tasks = tasks,
        toggleTaskEditorDialog
    )
}

@Composable
fun TaskGroupComponent(
    taskGroupTitle : String,
    taskGroupNote : String,
    taskGroupPriority : Int,
    taskGroupStartDate : LocalDate?,
    taskGroupStartTime : LocalTime?,
    taskGroupEndDate : LocalDate?,
    taskGroupEndTime : LocalTime?,
    toggleTaskGroupEditorDialog : () -> Unit,
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
            onClick = toggleTaskGroupEditorDialog
        ) {
            Icon(
                imageVector = Icons.Filled.Edit,
                contentDescription = "Edit task-group information",
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(16.dp)
            )
        }
    }

    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable { isExpanded = !isExpanded }
    ) {
        Box(
            modifier = Modifier
                .clip(RectangleShape)
                .background(
                    when (taskGroupPriority) {
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
                .padding(
                    start = 12.dp,
                    top = 6.dp,
                    bottom = 6.dp
                )
        ) {
            Text(
                text = if (taskGroupTitle == "") "Title*" else taskGroupTitle,
                style = title(if (taskGroupTitle == "") MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onPrimaryContainer),
                textAlign = TextAlign.Start
            )

            if (taskGroupStartDate != null || taskGroupStartTime != null || taskGroupEndDate != null || taskGroupEndTime != null) {
                Text(
                    text = dateTimeToString(
                        startDate = taskGroupStartDate,
                        startTime = taskGroupStartTime,
                        endDate = taskGroupEndDate,
                        endTime = taskGroupEndTime
                    ),
                    color = Color(0xFFCCCCCC),
                    fontSize = 12.sp,
                    textAlign = TextAlign.Start
                )
            }

            if (taskGroupNote != "") {
                NoteComponent(
                    note = taskGroupNote,
                    isExpanded = isExpanded
                )
            }
        }
    }
}

@Composable
fun TasksComponent(
    tasks : SnapshotStateList<Task>,
    toggleTaskEditorDialog : () -> Unit
) {
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
            onClick = toggleTaskEditorDialog,
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "New Task",
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }

    tasks.forEach { task ->
        var taskTitle by rememberSaveable { mutableStateOf(task.title) }
        var taskNote by rememberSaveable { mutableStateOf(task.note) }
        var taskStartDate by rememberSaveable { mutableStateOf(task.startDate) }
        var taskStartTime by rememberSaveable { mutableStateOf(task.startTime) }
        var taskEndDate by rememberSaveable { mutableStateOf(task.endDate) }
        var taskEndTime by rememberSaveable { mutableStateOf(task.endTime) }

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
                        text = taskTitle,
                        style = title(MaterialTheme.colorScheme.onPrimary),
                        textAlign = TextAlign.Start,
                    )

                    if (taskStartDate != null || taskStartTime != null || taskEndDate != null || taskEndTime != null) {
                        Text(
                            text = dateTimeToString(
                                startDate = taskStartDate,
                                startTime = taskStartTime,
                                endDate = taskEndDate,
                                endTime = taskEndTime
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

            if (taskNote != "") {
                NoteComponent(
                    note = taskNote,
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
    taskGroupPriority : Int,
    updateTaskGroupPriority : (Int) -> Unit,
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
            placeHolderText = "Title*",
            value = title,
            isSingleLine = true,
            onValueChange = {
                title = it
                task.title = it
            }
        )

        if (!isGroup) {
            PrioritySliderComponent(
                priority = taskGroupPriority,
                onPriorityChanged = updateTaskGroupPriority,
                modifier = Modifier.padding(
                    top = 6.dp,
                    bottom = 6.dp
                )
            )
        }

        if (note != "") {
            NoteEditComponent(
                note = note,
                onValueChange = {
                    note = it
                })
        }

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    onBackPress : () -> Unit
) {
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
                    onClick = { TODO("Not yet implemented") },
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
}
