package com.je.playground.feature.tasks.editor.subTask

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.je.playground.R
import com.je.playground.database.tasks.entity.SubTask
import com.je.playground.designsystem.component.SnackbarComponent
import com.je.playground.designsystem.component.task.taskeditor.EditorTopBar
import com.je.playground.designsystem.datetimepickers.DateTimePicker
import com.je.playground.feature.tasks.editor.SubTaskField
import com.je.playground.feature.tasks.editor.TaskEditorEvent
import com.je.playground.feature.tasks.editor.TaskEditorEvent.UpdateSubTask
import com.je.playground.feature.tasks.editor.TaskEditorViewModel
import com.je.playground.feature.tasks.editor.with

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
                subTask = if (subTaskIndex == -1) SubTask() else taskEditorState.subTasks[subTaskIndex],
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
                    UpdateSubTask(
                        subTaskIndex,
                        listOf(
                            SubTaskField.Title(title),
                            SubTaskField.Note(note),
                            SubTaskField.StartDate(startDate),
                            SubTaskField.StartTime(startTime),
                            SubTaskField.EndDate(endDate),
                            SubTaskField.EndTime(endTime),
                        )
                    ).with(onEvent)

                    onBackPress()
                },
                onBackPress = onBackPress
            )
        },
        snackbarHost = { SnackbarComponent(snackbarHostState = snackbarHostState) },
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(16.dp)
            ) {
                OutlinedTextField(
                    label = { Text(text = stringResource(R.string.title_required)) },
                    value = title,
                    onValueChange = { title = it },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )


                DateTimePicker(
                    dateLabel = "From",
                    timeLabel = "HH:mm",
                    date = startDate,
                    time = startTime,
                    onDateSelected = { startDate = it },
                    onTimeSelected = { startTime = it }
                )

                DateTimePicker(
                    dateLabel = "To",
                    timeLabel = "HH:mm",
                    date = endDate,
                    time = endTime,
                    onDateSelected = { endDate = it },
                    onTimeSelected = { endTime = it }
                )

                OutlinedTextField(
                    label = { Text(text = stringResource(R.string.note)) },
                    value = note ?: "",
                    maxLines = 10,
                    onValueChange = { note = it },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
