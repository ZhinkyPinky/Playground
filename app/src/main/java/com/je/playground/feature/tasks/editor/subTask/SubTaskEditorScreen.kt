package com.je.playground.feature.tasks.editor.subTask

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.je.playground.database.tasks.entity.SubTask
import com.je.playground.designsystem.component.SnackbarComponent
import com.je.playground.designsystem.component.task.taskeditor.TextFieldComponent
import com.je.playground.deprecated.datetimerangepicker.DateRangePicker
import com.je.playground.deprecated.datetimerangepicker.TimeRangePicker
import com.je.playground.designsystem.component.task.taskeditor.EditorTopBar
import com.je.playground.designsystem.component.task.taskeditor.NoteEditComponent
import com.je.playground.feature.tasks.editor.SubTaskField
import com.je.playground.feature.tasks.editor.TaskEditorEvent
import com.je.playground.feature.tasks.editor.TaskEditorViewModel

@Composable
fun SubTaskEditorScreen(
    viewModel: TaskEditorViewModel,
    subTaskIndex: Int = -1,
    onBackClick: () -> Unit
) {
    val taskEditorState: TaskEditorViewModel.State by viewModel.taskEditorUiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.snackbarFlow.collect { event ->
            event.consume()?.let { message ->
                snackbarHostState.showSnackbar(message)
            }
        }
    }

    SubTaskEditorScreen(
        taskEditorState = taskEditorState,
        subTaskIndex = subTaskIndex,
        snackbarHostState = snackbarHostState,
        onEvent = viewModel::onEvent,
        onBackClick = onBackClick
    )
}

@Composable
fun SubTaskEditorScreen(
    taskEditorState: TaskEditorViewModel.State,
    subTaskIndex: Int,
    snackbarHostState: SnackbarHostState,
    onEvent: (TaskEditorEvent) -> Unit,
    onBackClick: () -> Unit
) {
    when (taskEditorState) {
        TaskEditorViewModel.State.Loading -> {}
        is TaskEditorViewModel.State.Ready -> {
            SubTaskEditorContent(
                subTaskIndex = subTaskIndex,
                subTask = taskEditorState.subTasks[subTaskIndex],
                snackbarHostState = snackbarHostState,
                onEvent = onEvent,
                onBackPress = onBackClick
            )
        }

        TaskEditorViewModel.State.Saved -> onBackClick()
    }
}

@Composable
fun SubTaskEditorContent(
    subTaskIndex: Int,
    subTask: SubTask,
    snackbarHostState: SnackbarHostState,
    onEvent: (TaskEditorEvent) -> Unit,
    onBackPress: () -> Unit
) {
    var title by rememberSaveable { mutableStateOf(subTask.title) }
    var note by rememberSaveable { mutableStateOf(subTask.note) }
    var startDate by rememberSaveable { mutableStateOf(subTask.startDate) }
    var startTime by rememberSaveable { mutableStateOf(subTask.startTime) }
    var endDate by rememberSaveable { mutableStateOf(subTask.endDate) }
    var endTime by rememberSaveable { mutableStateOf(subTask.endTime) }

    Scaffold(
        topBar = {
            EditorTopBar(
                text = "Edit",
                onEvent = {
                    TaskEditorEvent.updateSubTask(
                        onEvent,
                        subTaskIndex,
                        listOf(
                            SubTaskField.Title(title),
                            SubTaskField.Note(note),
                            SubTaskField.StartDate(startDate),
                            SubTaskField.StartTime(startTime),
                            SubTaskField.EndDate(endDate),
                            SubTaskField.EndTime(endTime),
                        )
                    )
                },
                onBackPress = onBackPress
            )
        },
        snackbarHost = {
            SnackbarComponent(snackbarHostState = snackbarHostState)
        },
        containerColor = MaterialTheme.colorScheme.primary,
    ) { paddingValues ->
        Box(
            modifier = Modifier.padding(paddingValues)
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
                    value = title,
                    isSingleLine = true,
                    onValueChange = { title = it },
                    modifier = Modifier.padding(
                        bottom = 6.dp
                    )
                )

                NoteEditComponent(
                    note = note,
                    onValueChange = { note = it },
                    modifier = Modifier.padding(
                        bottom = 12.dp
                    )
                )

                DateRangePicker(
                    startDate = startDate,
                    endDate = endDate,
                    onStartDateValueChange = { startDate = it },
                    onEndDateValueChange = { endDate = it },
                    clearDates = {
                        startDate = null
                        endDate = null
                    }
                )

                TimeRangePicker(
                    startTime = startTime,
                    endTime = endTime,
                    onStartTimeValueChange = { startTime = it },
                    onEndTimeValueChange = { endTime = it }
                )
            }
        }
    }
}
