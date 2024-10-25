package com.je.playground.feature.tasks.editor.mainTask

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.je.playground.R
import com.je.playground.database.tasks.entity.Task
import com.je.playground.designsystem.theme.ThemePreviews
import com.je.playground.designsystem.component.PrioritySliderComponent
import com.je.playground.designsystem.component.TextFieldComponent
import com.je.playground.designsystem.component.datetimerangepicker.DateRangePicker
import com.je.playground.designsystem.component.datetimerangepicker.TimeRangePicker
import com.je.playground.designsystem.component.taskeditor.EditorTopBar
import com.je.playground.designsystem.component.taskeditor.NoteEditComponent
import com.je.playground.designsystem.theme.PlaygroundTheme
import com.je.playground.feature.tasks.editor.TaskEditorEvent
import com.je.playground.feature.tasks.editor.TaskEditorViewModel
import com.je.playground.feature.tasks.editor.TaskField

@Composable
fun MainTaskEditorScreen(
    viewModel: TaskEditorViewModel,
    onBackClick: () -> Unit
) {
    val state: TaskEditorViewModel.State by viewModel.taskEditorUiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.snackbarFlow.collect { event ->
            event.consume()?.let { message ->
                snackbarHostState.showSnackbar(message)
            }
        }
    }

    MainTaskEditorScreen(
        state = state,
        snackbarHostState = snackbarHostState,
        onEvent = viewModel::onEvent,
        onBackClick = onBackClick
    )
}

@Composable
fun MainTaskEditorScreen(
    state: TaskEditorViewModel.State,
    snackbarHostState: SnackbarHostState,
    onEvent: (TaskEditorEvent) -> Unit,
    onBackClick: () -> Unit
) {
    when (state) {
        is TaskEditorViewModel.State.Loading -> {}
        is TaskEditorViewModel.State.Ready -> {
            MainTaskEditorContent(
                task = state.task,
                snackbarHostState = snackbarHostState,
                onEvent = onEvent,
                onBackClick = onBackClick
            )
        }

        is TaskEditorViewModel.State.Saved -> {}
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTaskEditorContent(
    task: Task,
    snackbarHostState: SnackbarHostState,
    onEvent: (TaskEditorEvent) -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.edit)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.go_back)
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.primary
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
                    label = stringResource(R.string.title_required),
                    placeholder = "Enter a title for the group",
                    value = task.title,
                    isSingleLine = true,
                    onValueChange = { TaskField.updateTitle(onEvent, it) },
                    modifier = Modifier.padding(
                        bottom = 6.dp
                    )
                )

                NoteEditComponent(
                    note = task.note,
                    onValueChange = { TaskField.updateNote(onEvent, it) },
                    modifier = Modifier.padding(
                        bottom = 12.dp
                    )
                )

                PrioritySliderComponent(
                    priority = task.priority,
                    onPriorityChanged = { TaskField.updatePriority(onEvent, it) },
                    modifier = Modifier.padding(
                        top = 6.dp,
                        bottom = 6.dp
                    )
                )

                DateRangePicker(
                    startDate = task.startDate,
                    endDate = task.endDate,
                    onStartDateValueChange = { TaskField.updateStartDate(onEvent, it) },
                    onEndDateValueChange = { TaskField.updateEndDate(onEvent, it) },
                    clearDates = {
                        TaskEditorEvent.updateTask(
                            onEvent,
                            listOf(
                                TaskField.StartDate(null),
                                TaskField.EndDate(null)
                            )
                        )
                    }
                )

                TimeRangePicker(
                    startTime = task.startTime,
                    endTime = task.endTime,
                    onStartTimeValueChange = { TaskField.updateStartTime(onEvent, it) },
                    onEndTimeValueChange = { TaskField.updateEndTime(onEvent, it) }
                )
            }
        }
    }
}

@ThemePreviews
@Composable
fun TaskEditorScreenPreview() {
    PlaygroundTheme {
        MainTaskEditorContent(
            task = Task(),
            snackbarHostState = SnackbarHostState(),
            onEvent = {},
            onBackClick = {}
        )
    }
}