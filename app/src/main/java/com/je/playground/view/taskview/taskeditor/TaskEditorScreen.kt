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
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.collectAsState
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
import com.je.playground.database.tasks.entity.MainTask
import com.je.playground.database.tasks.entity.SubTask
import com.je.playground.view.sharedcomponents.NoteComponent
import com.je.playground.view.taskview.dateTimeToString
import com.je.playground.view.taskview.taskeditor.datetimerangepicker.DateRangePicker
import com.je.playground.view.taskview.taskeditor.datetimerangepicker.TimeRangePicker
import com.je.playground.view.taskview.taskeditor.viewmodel.TaskEditorViewModel
import com.je.playground.view.theme.title

@Composable
fun TaskEditorScreen(
    taskEditorViewModel : TaskEditorViewModel,
    navigateToMainTaskEditorScreen : () -> Unit,
    navigateToSubTaskEditorScreen : (Int) -> Unit,
    onBackPress : () -> Unit,
) {
    when (taskEditorViewModel.taskEditorUiState.collectAsState().value) {
        is TaskEditorViewModel.State.Loading -> {
            //TODO: Add loading screen?
            CircularProgressIndicator()
        }

        is TaskEditorViewModel.State.Ready -> {
            TaskEditorScreen(
                mainTask = taskEditorViewModel.mainTask.collectAsState().value,
                updateMainTask = taskEditorViewModel::updateMainTask,
                subTasks = taskEditorViewModel.subTasks,
                removeSubTask = taskEditorViewModel::removeSubTask,
                saveMainTaskWithSubTasks = {
                    taskEditorViewModel.saveMainTaskWithSubTasks()
                    onBackPress()
                },
                navigateToMainTaskEditorScreen = navigateToMainTaskEditorScreen,
                navigateToSubTaskEditorScreen = navigateToSubTaskEditorScreen,
                onBackPress = onBackPress
            )
        }
    }
}

@Composable
fun TaskEditorScreen(
    mainTask : MainTask,
    updateMainTask : (MainTask) -> Unit,
    subTasks : List<SubTask>,
    removeSubTask : (Int) -> Unit,
    saveMainTaskWithSubTasks : () -> Unit,
    navigateToMainTaskEditorScreen : () -> Unit,
    navigateToSubTaskEditorScreen : (Int) -> Unit,
    onBackPress : () -> Unit,
) {
    var isGroup by rememberSaveable { mutableStateOf(subTasks.isNotEmpty()) }

    TaskEditorContent(
        mainTask = mainTask,
        saveMainTaskWithSubTasks = saveMainTaskWithSubTasks,
        updateMainTask = updateMainTask,
        subTasks = subTasks,
        removeSubTask = removeSubTask,
        isGroup = isGroup,
        toggleIsGroup = {
            isGroup =
                if (subTasks.isNotEmpty()) {
                    true
                } else {
                    !isGroup
                }
        },
        navigateToMainTaskEditorScreen = navigateToMainTaskEditorScreen,
        navigateToSubTaskEditorScreen = navigateToSubTaskEditorScreen,
        onBackPress = onBackPress,
    )
}


@Composable
fun TaskEditorContent(
    mainTask : MainTask,
    updateMainTask : (MainTask) -> Unit,
    saveMainTaskWithSubTasks : () -> Unit,
    subTasks : List<SubTask>,
    removeSubTask : (Int) -> Unit,
    isGroup : Boolean,
    toggleIsGroup : () -> Unit,
    navigateToMainTaskEditorScreen : () -> Unit,
    navigateToSubTaskEditorScreen : (Int) -> Unit,
    onBackPress : () -> Unit
) {
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
                GroupTaskEditor(
                    mainTask = mainTask,
                    subTasks = subTasks,
                    removeSubTask = removeSubTask,
                    navigateToMainTaskEditorScreen = navigateToMainTaskEditorScreen,
                    navigateToSubTaskEditorScreen = navigateToSubTaskEditorScreen
                )
            } else {
                SingleTaskEditor(
                    mainTask = mainTask,
                    updateMainTask = updateMainTask
                )
            }
        }
    }
}

@Composable
fun GroupTaskEditor(
    mainTask : MainTask,
    subTasks : List<SubTask>,
    removeSubTask : (Int) -> Unit,
    navigateToMainTaskEditorScreen : () -> Unit,
    navigateToSubTaskEditorScreen : (Int) -> Unit,
) {
    MainTaskComponent(
        mainTask = mainTask,
        navigateToMainTaskEditorScreen
    )

    SubTasksComponent(
        subTasks = subTasks,
        removeSubTask = removeSubTask,
        navigateToSubTaskEditorScreen = navigateToSubTaskEditorScreen
    )
}

@Composable
fun MainTaskComponent(
    mainTask : MainTask,
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
                text = if (mainTask.title == "") "Title*" else mainTask.title,
                style = title(if (mainTask.title == "") MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onPrimaryContainer),
                textAlign = TextAlign.Start
            )

            if (mainTask.startDate != null || mainTask.startTime != null || mainTask.endDate != null || mainTask.endTime != null) {
                Text(
                    text = dateTimeToString(
                        startDate = mainTask.startDate,
                        startTime = mainTask.startTime,
                        endDate = mainTask.endDate,
                        endTime = mainTask.endTime
                    ),
                    color = Color(0xFFCCCCCC),
                    fontSize = 12.sp,
                    textAlign = TextAlign.Start
                )
            }

            if (mainTask.note != "") {
                NoteComponent(
                    note = mainTask.note,
                    isExpanded = isExpanded
                )
            }
        }
    }
}

@Composable
fun SubTasksComponent(
    subTasks : List<SubTask>,
    removeSubTask : (Int) -> Unit,
    navigateToSubTaskEditorScreen : (Int) -> Unit,
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
            onClick = {
                navigateToSubTaskEditorScreen(-1)
            },
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "New Task",
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }

    subTasks.forEach { subTask ->
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
                        onDismissRequest = { isDropDownMenuExpanded = false },
                        modifier = Modifier.background(MaterialTheme.colorScheme.secondary)
                    ) {
                        Column {
                            TextButton(onClick = {
                                isDropDownMenuExpanded = false
                                navigateToSubTaskEditorScreen(subTasks.indexOf(subTask))
                            }) {
                                Text(
                                    text = "Edit",
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            }

                            TextButton(onClick = {
                                removeSubTask(subTasks.indexOf(subTask))
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

            if (subTask.note != "") {
                NoteComponent(
                    note = subTask.note,
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
