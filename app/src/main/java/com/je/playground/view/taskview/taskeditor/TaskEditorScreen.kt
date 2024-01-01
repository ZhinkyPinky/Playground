package com.je.playground.view.taskview.taskeditor

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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.je.playground.database.tasks.entity.MainTask
import com.je.playground.database.tasks.entity.MainTaskWithSubTasks
import com.je.playground.database.tasks.entity.SubTask
import com.je.playground.view.sharedcomponents.NoteComponent
import com.je.playground.view.taskview.dateTimeToString
import com.je.playground.view.taskview.taskeditor.datetimerangepicker.DateRangePicker
import com.je.playground.view.taskview.taskeditor.datetimerangepicker.TimeRangePicker
import com.je.playground.view.taskview.taskeditor.dialog.MainTaskEditorDialog
import com.je.playground.view.taskview.taskeditor.dialog.SubTaskEditorDialog
import com.je.playground.view.taskview.viewmodel.TasksViewModel
import com.je.playground.view.theme.title
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun TaskEditorScreen(
    mainTaskId : Long?,
    tasksViewModel : TasksViewModel,
    mainTaskWithSubtasks : MainTaskWithSubTasks = MainTaskWithSubTasks(
        MainTask(),
        mutableListOf()
    ),
    onBackPress : () -> Unit,
) {
    var mainTaskWithSubTasks = MainTaskWithSubTasks(
        mainTask = MainTask(),
        subTasks = emptyList()
    )

    var mainTask = mainTaskWithSubTasks.mainTask
    val mainTaskIdToSave by rememberSaveable {
        if (mainTaskId != null) {
            mutableLongStateOf(mainTaskId)
        } else {
            mutableLongStateOf(mainTask.mainTaskId)
        }
    }
    var mainTaskTitle by rememberSaveable { mutableStateOf(mainTask.title) }
    var mainTaskNote by rememberSaveable { mutableStateOf(mainTask.note) }
    var mainTaskPriority by rememberSaveable { mutableIntStateOf(mainTask.priority) }
    var mainTaskStartDate by rememberSaveable { mutableStateOf(mainTask.startDate) }
    var mainTaskStartTime by rememberSaveable { mutableStateOf(mainTask.startTime) }
    var mainTaskEndDate by rememberSaveable { mutableStateOf(mainTask.endDate) }
    var mainTaskEndTime by rememberSaveable { mutableStateOf(mainTask.endTime) }

    val subTasks = rememberSaveable(
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
        mainTaskWithSubtasks.subTasks
            .map { it.copy() }
            .toMutableStateList()
    }

    var showMainTaskEditorDialog by rememberSaveable { mutableStateOf(false) }

    var isGroup by rememberSaveable { mutableStateOf(false) }
    var isLoading by rememberSaveable { mutableStateOf(true) }

    LaunchedEffect(
        Unit
    ) {
        mainTaskId?.let { id ->
            tasksViewModel
                .selectMainTaskWithSubTasksByMainTaskId(id)
                ?.let {
                    mainTaskWithSubTasks = it
                    mainTask = mainTaskWithSubTasks.mainTask
                    mainTaskTitle = mainTask.title
                    mainTaskNote = mainTask.note
                    mainTaskPriority = mainTask.priority
                    mainTaskStartDate = mainTask.startDate
                    mainTaskStartTime = mainTask.startTime
                    mainTaskEndDate = mainTask.endDate
                    mainTaskEndTime = mainTask.endTime

                    mainTaskWithSubTasks.subTasks.forEach { subTask ->
                        subTasks.add(subTask)
                    }

                    isGroup = subTasks.size > 0
                }
        }

        isLoading = false
    }

    if (!isLoading) {
        TaskEditorContent(
            saveMainTaskWithSubTasks = {
                mainTask.mainTaskId = mainTaskIdToSave
                mainTask.title = mainTaskTitle
                mainTask.note = mainTaskNote
                mainTask.priority = mainTaskPriority
                mainTask.startDate = mainTaskStartDate
                mainTask.startTime = mainTaskStartTime
                mainTask.endDate = mainTaskEndDate
                mainTask.endTime = mainTaskEndTime

                tasksViewModel.saveMainTaskWithSubTasks(
                    mainTaskWithSubTasks = MainTaskWithSubTasks(
                        mainTask = mainTask,
                        subTasks = subTasks.toList()
                    )
                )

                onBackPress()
            },
            deleteMainTaskWithTasks = tasksViewModel::deleteMainTaskWithSubTasks,
            mainTaskId = mainTaskId.takeIf { it != null } ?: mainTask.mainTaskId,
            mainTaskTitle = mainTaskTitle,
            updateMainTaskTitle = {
                mainTaskTitle = it
                mainTask.title = it
            },
            mainTaskNote = mainTaskNote,
            updateMainTaskNote = {
                mainTaskNote = it
                mainTask.note = it
            },
            mainTaskPriority = mainTaskPriority,
            updateMainTaskPriority = {
                mainTaskPriority = it
                mainTask.priority = it
            },
            mainTaskStartDate =
            mainTaskStartDate,
            updateMainTaskStartDate = {
                mainTaskStartDate = it
                mainTask.startDate = it
            },
            mainTaskStartTime = mainTaskStartTime,
            updateMainTaskStartTime = {
                mainTaskStartTime = it
                mainTask.startTime = it
            },
            mainTaskEndDate = mainTaskEndDate,
            updateMainTaskEndDate = {
                mainTaskEndDate = it
                mainTask.endDate = it
            },
            mainTaskEndTime = mainTaskEndTime,
            updateMainTaskEndTime = {
                mainTaskEndTime = it
                mainTask.endTime = it
            },
            updateMainTask = { title, note, priority, startDate, startTime, endDate, endTime ->
                mainTaskTitle = title
                mainTask.title = title
                mainTaskNote = note
                mainTask.note = note
                mainTaskPriority = priority
                mainTask.priority = priority
                mainTaskStartDate = startDate
                mainTask.startDate = startDate
                mainTaskStartTime = startTime
                mainTask.startTime = startTime
                mainTaskEndDate = endDate
                mainTask.endDate = endDate
                mainTaskEndTime = endTime
                mainTask.endTime = endTime
            },
            subTasks = subTasks,
            showMainTaskEditorDialog = showMainTaskEditorDialog,
            toggleMainTaskEditorDialog = { showMainTaskEditorDialog = !showMainTaskEditorDialog },
            isGroup = isGroup,
            toggleIsGroup = {
                isGroup =
                    if (subTasks.size > 0) {
                        true
                    } else {
                        !isGroup
                    }
            },
            onBackPress = onBackPress,
        )
    }
}

@Composable
fun TaskEditorContent(
    saveMainTaskWithSubTasks : () -> Unit,
    deleteMainTaskWithTasks : (MainTaskWithSubTasks) -> Unit,
    mainTaskId : Long,
    mainTaskTitle : String,
    updateMainTaskTitle : (String) -> Unit,
    mainTaskNote : String,
    updateMainTaskNote : (String) -> Unit,
    mainTaskPriority : Int,
    updateMainTaskPriority : (Int) -> Unit,
    mainTaskStartDate : LocalDate?,
    updateMainTaskStartDate : (LocalDate?) -> Unit,
    mainTaskStartTime : LocalTime?,
    updateMainTaskStartTime : (LocalTime?) -> Unit,
    mainTaskEndDate : LocalDate?,
    updateMainTaskEndDate : (LocalDate?) -> Unit,
    mainTaskEndTime : LocalTime?,
    updateMainTaskEndTime : (LocalTime?) -> Unit,
    updateMainTask : (String, String, Int, LocalDate?, LocalTime?, LocalDate?, LocalTime?) -> Unit,
    subTasks : SnapshotStateList<SubTask>,
    showMainTaskEditorDialog : Boolean,
    toggleMainTaskEditorDialog : () -> Unit,
    isGroup : Boolean,
    toggleIsGroup : () -> Unit,
    onBackPress : () -> Unit
) {
    if (showMainTaskEditorDialog) {
        MainTaskEditorDialog(
            mainTaskTitle = mainTaskTitle,
            mainTaskNote = mainTaskNote,
            mainTaskPriority = mainTaskPriority,
            mainTaskStartDate = mainTaskStartDate,
            mainTaskStartTime = mainTaskStartTime,
            mainTaskEndDate = mainTaskEndDate,
            mainTaskEndTime = mainTaskEndTime,
            updateMainTask = updateMainTask,
            onDismissRequest = toggleMainTaskEditorDialog
        )
    }

    Scaffold(
        topBar = {
            TopBar(
                saveMainTaskWithSubTasks = saveMainTaskWithSubTasks,
                onBackPress
            )
        },
        containerColor = if (isGroup) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.primary
    ) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.spacedBy(1.dp),
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
                .padding(paddingValues)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .height(IntrinsicSize.Max)
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
                MainTaskEditor(
                    mainTaskId = mainTaskId,
                    mainTaskTitle = mainTaskTitle,
                    mainTaskNote = mainTaskNote,
                    mainTaskPriority = mainTaskPriority,
                    mainTaskStartDate = mainTaskStartDate,
                    mainTaskStartTime = mainTaskStartTime,
                    mainTaskEndDate = mainTaskEndDate,
                    mainTaskEndTime = mainTaskEndTime,
                    subTasks = subTasks,
                    toggleMainTaskEditorDialog = toggleMainTaskEditorDialog,
                )
            } else {
                SingleTaskEditor(
                    mainTaskTitle = mainTaskTitle,
                    updateMainTaskTitle = updateMainTaskTitle,
                    mainTaskNote = mainTaskNote,
                    updateMainTaskNote = updateMainTaskNote,
                    mainTaskPriority = mainTaskPriority,
                    updateMainTaskPriority = updateMainTaskPriority,
                    mainTaskStartDate = mainTaskStartDate,
                    updateMainTaskStartDate = updateMainTaskStartDate,
                    mainTaskStartTime = mainTaskStartTime,
                    updateMainTaskStartTime = updateMainTaskStartTime,
                    mainTaskEndDate = mainTaskEndDate,
                    updateMainTaskEndDate = updateMainTaskEndDate,
                    mainTaskEndTime = mainTaskEndTime,
                    updateMainTaskEndTime = updateMainTaskEndTime
                )
            }
        }
    }
}

@Composable
fun MainTaskEditor(
    mainTaskId : Long,
    mainTaskTitle : String,
    mainTaskNote : String,
    mainTaskPriority : Int,
    mainTaskStartDate : LocalDate?,
    mainTaskStartTime : LocalTime?,
    mainTaskEndDate : LocalDate?,
    mainTaskEndTime : LocalTime?,
    subTasks : SnapshotStateList<SubTask>,
    toggleMainTaskEditorDialog : () -> Unit,
) {
    MainTaskComponent(
        mainTaskTitle = mainTaskTitle,
        mainTaskNote = mainTaskNote,
        mainTaskPriority = mainTaskPriority,
        mainTaskStartDate = mainTaskStartDate,
        mainTaskStartTime = mainTaskStartTime,
        mainTaskEndDate = mainTaskEndDate,
        mainTaskEndTime = mainTaskEndTime,
        toggleMainTaskEditorDialog = toggleMainTaskEditorDialog,
    )

    SubTasksComponent(
        mainTaskId = mainTaskId,
        subTasks = subTasks,
    )
}

@Composable
fun MainTaskComponent(
    mainTaskTitle : String,
    mainTaskNote : String,
    mainTaskPriority : Int,
    mainTaskStartDate : LocalDate?,
    mainTaskStartTime : LocalTime?,
    mainTaskEndDate : LocalDate?,
    mainTaskEndTime : LocalTime?,
    toggleMainTaskEditorDialog : () -> Unit,
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
            onClick = toggleMainTaskEditorDialog
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
                    when (mainTaskPriority) {
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
                text = if (mainTaskTitle == "") "Title*" else mainTaskTitle,
                style = title(if (mainTaskTitle == "") MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onPrimaryContainer),
                textAlign = TextAlign.Start
            )

            if (mainTaskStartDate != null || mainTaskStartTime != null || mainTaskEndDate != null || mainTaskEndTime != null) {
                Text(
                    text = dateTimeToString(
                        startDate = mainTaskStartDate,
                        startTime = mainTaskStartTime,
                        endDate = mainTaskEndDate,
                        endTime = mainTaskEndTime
                    ),
                    color = Color(0xFFCCCCCC),
                    fontSize = 12.sp,
                    textAlign = TextAlign.Start
                )
            }

            if (mainTaskNote != "") {
                NoteComponent(
                    note = mainTaskNote,
                    isExpanded = isExpanded
                )
            }
        }
    }
}

@Composable
fun SubTasksComponent(
    mainTaskId : Long,
    subTasks : SnapshotStateList<SubTask>,
) {
    var showNewSubTaskEditorDialog by rememberSaveable { mutableStateOf(false) }

    if (showNewSubTaskEditorDialog) {
        SubTaskEditorDialog(
            dialogTitle = "New subtask",
            mainTaskId = mainTaskId,
            subTask = SubTask(),
            isNewTask = true,
            onSave = {
                subTasks.add(it)
                showNewSubTaskEditorDialog = false
            },
            onDismissRequest = { showNewSubTaskEditorDialog = false }
        )
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
            onClick = { showNewSubTaskEditorDialog = true },
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "New Task",
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }

    subTasks.forEach { subTask ->
        var subTaskTitle by rememberSaveable { mutableStateOf(subTask.title) }
        var subTaskNote by rememberSaveable { mutableStateOf(subTask.note) }
        var subTaskStartDate by rememberSaveable { mutableStateOf(subTask.startDate) }
        var subTaskStartTime by rememberSaveable { mutableStateOf(subTask.startTime) }
        var subTaskEndDate by rememberSaveable { mutableStateOf(subTask.endDate) }
        var subTaskEndTime by rememberSaveable { mutableStateOf(subTask.endTime) }

        var showEditSubTaskEditorDialog by rememberSaveable { mutableStateOf(false) }

        if (showEditSubTaskEditorDialog) {
            SubTaskEditorDialog(
                dialogTitle = "Edit subtask",
                mainTaskId = mainTaskId,
                subTask = subTask,
                isNewTask = true,
                onSave = {
                    subTask.title = it.title
                    subTaskTitle = it.title
                    subTask.note = it.note
                    subTaskNote = it.note
                    subTask.startDate = it.startDate
                    subTaskStartDate = it.startDate
                    subTask.startTime = it.startTime
                    subTaskStartTime = it.startTime
                    subTask.endDate = it.endDate
                    subTaskEndDate = it.endDate
                    subTask.endTime = it.endTime
                    subTaskEndTime = it.endTime

                    showEditSubTaskEditorDialog = false
                },
                onDismissRequest = { showEditSubTaskEditorDialog = false }
            )
        }


        var isExpanded by rememberSaveable {
            mutableStateOf(false)
        }

        var isDropDownMenuExpanded by rememberSaveable {
            mutableStateOf(false)
        }

        Column(
            modifier = Modifier
                .clickable { isExpanded = !isExpanded }
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(
                    start = 12.dp,
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
                        text = subTaskTitle,
                        style = title(MaterialTheme.colorScheme.onPrimary),
                        textAlign = TextAlign.Start,
                    )

                    if (subTaskStartDate != null || subTaskStartTime != null || subTaskEndDate != null || subTaskEndTime != null) {
                        Text(
                            text = dateTimeToString(
                                startDate = subTaskStartDate,
                                startTime = subTaskStartTime,
                                endDate = subTaskEndDate,
                                endTime = subTaskEndTime
                            ),
                            color = Color(0xFFCCCCCC),
                            fontSize = 12.sp,
                            textAlign = TextAlign.Start
                        )
                    }
                }

                Box {
                    IconButton(onClick = {
                        isDropDownMenuExpanded = !isDropDownMenuExpanded
                    }) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = "Note"
                        )
                    }

                    DropdownMenu(
                        expanded = isDropDownMenuExpanded,
                        onDismissRequest = { isDropDownMenuExpanded = false }) {
                        Column {
                            TextButton(onClick = {
                                isDropDownMenuExpanded = false
                                showEditSubTaskEditorDialog = true
                            }) {
                                Text(
                                    text = "Edit",
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            }

                            TextButton(onClick = {
                                subTasks.remove(subTask)
                                isDropDownMenuExpanded = false
                            }) {
                                Text(
                                    text = "Delete",
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }
                    }
                }
            }

            if (subTaskNote != "") {
                NoteComponent(
                    note = subTaskNote,
                    isExpanded = isExpanded,
                    modifier = Modifier.padding(
                        top = 6.dp,
                        bottom = 6.dp,
                        end = 12.dp
                    )
                )

            }
        }
    }
}

@Composable
fun SingleTaskEditor(
    mainTaskTitle : String,
    updateMainTaskTitle : (String) -> Unit,
    mainTaskNote : String,
    updateMainTaskNote : (String) -> Unit,
    mainTaskPriority : Int,
    updateMainTaskPriority : (Int) -> Unit,
    mainTaskStartDate : LocalDate?,
    updateMainTaskStartDate : (LocalDate?) -> Unit,
    mainTaskStartTime : LocalTime?,
    updateMainTaskStartTime : (LocalTime?) -> Unit,
    mainTaskEndDate : LocalDate?,
    updateMainTaskEndDate : (LocalDate?) -> Unit,
    mainTaskEndTime : LocalTime?,
    updateMainTaskEndTime : (LocalTime?) -> Unit,
) {
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
            value = mainTaskTitle,
            isSingleLine = true,
            onValueChange = updateMainTaskTitle,
            modifier = Modifier.padding(
                bottom = 6.dp
            )
        )

        NoteEditComponent(
            note = mainTaskNote,
            onValueChange = updateMainTaskNote,
            modifier = Modifier.padding(
                bottom = 12.dp
            )
        )

        PrioritySliderComponent(
            priority = mainTaskPriority,
            onPriorityChanged = updateMainTaskPriority,
            modifier = Modifier.padding(
                top = 6.dp,
                bottom = 6.dp
            )
        )

        DateRangePicker(
            startDate = mainTaskStartDate,
            endDate = mainTaskEndDate,
            onStartDateValueChange = updateMainTaskStartDate,
            onEndDateValueChange = updateMainTaskEndDate,
        )

        TimeRangePicker(
            startTime = mainTaskStartTime,
            endTime = mainTaskEndTime,
            onStartTimeValueChange = updateMainTaskStartTime,
            onEndTimeValueChange = updateMainTaskEndTime
        )

        Box(
            modifier = Modifier
                .clip(RectangleShape)
                .background(
                    when (mainTaskPriority) {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    saveMainTaskWithSubTasks : () -> Unit,
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
                    onClick = { saveMainTaskWithSubTasks() },
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
