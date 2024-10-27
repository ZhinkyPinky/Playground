package com.je.playground.feature.tasks.editor.task

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.je.playground.R
import com.je.playground.database.tasks.entity.Task
import com.je.playground.designsystem.DatePicker
import com.je.playground.designsystem.TimePicker
import com.je.playground.ui.theme.ThemePreviews
import com.je.playground.designsystem.component.task.taskeditor.PrioritySliderComponent
import com.je.playground.ui.theme.PlaygroundTheme
import com.je.playground.feature.tasks.editor.TaskEditorEvent
import com.je.playground.feature.tasks.editor.TaskEditorViewModel
import com.je.playground.feature.tasks.editor.TaskField
import com.je.playground.feature.tasks.editor.update

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
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(12.dp)
            ) {
                OutlinedTextField(
                    label = { Text(text = stringResource(R.string.title_required)) },
                    value = task.title,
                    onValueChange = { TaskField.Title(it).update(onEvent) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    label = { Text(text = stringResource(R.string.note)) },
                    value = task.note,
                    onValueChange = { TaskField.Note(it).update(onEvent) },
                    modifier = Modifier.fillMaxWidth()
                )

                DatePicker(
                    label = "From",
                    date = task.startDate,
                    onDateSelected = {},
                )

                DatePicker(
                    label = "To",
                    date = task.endDate,
                    onDateSelected = {},
                )

                TimePicker(
                    label = "From",
                    time = task.startTime,
                    onTimeSelected = {}
                )

                TimePicker(
                    label = "To",
                    time = task.endTime,
                    onTimeSelected = {}
                )

                /*
                DateRangePicker(
                    startDate = task.startDate,
                    endDate = task.endDate,
                    onStartDateValueChange = { TaskField.StartDate(it).update(onEvent) },
                    onEndDateValueChange = { TaskField.EndDate(it).update(onEvent) },
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
                 *
                TimeRangePicker(
                    startTime = task.startTime,
                    endTime = task.endTime,
                    onStartTimeValueChange = { TaskField.StartTime(it).update(onEvent) },
                    onEndTimeValueChange = { TaskField.EndTime(it).update(onEvent) }
                )

                 */

                PrioritySliderComponent(
                    priority = task.priority,
                    onPriorityChanged = { TaskField.Priority(it).update(onEvent) },
                    modifier = Modifier.padding(
                        top = 6.dp,
                        bottom = 6.dp
                    )
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