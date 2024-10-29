package com.je.playground.feature.tasks.editor.task

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.je.playground.R
import com.je.playground.database.tasks.entity.Task
import com.je.playground.designsystem.datetimepickers.DateTimeRangePicker
import com.je.playground.feature.tasks.editor.TaskEditorEvent
import com.je.playground.feature.tasks.editor.TaskEditorEvent.UpdateTask
import com.je.playground.feature.tasks.editor.TaskEditorViewModel
import com.je.playground.feature.tasks.editor.TaskField
import com.je.playground.feature.tasks.editor.with
import com.je.playground.ui.theme.PlaygroundTheme
import com.je.playground.ui.theme.ThemePreviews
import java.time.LocalDate

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

    var title by rememberSaveable { mutableStateOf(task.title) }
    var note by rememberSaveable { mutableStateOf(task.note) }
    var priority by rememberSaveable { mutableIntStateOf(task.priority) }
    var startDate by rememberSaveable { mutableStateOf(task.startDate) }
    var startTime by rememberSaveable { mutableStateOf(task.startTime) }
    var endDate by rememberSaveable { mutableStateOf(task.endDate) }
    var endTime by rememberSaveable { mutableStateOf(task.endTime) }


    val scrollState = rememberScrollState()

    LaunchedEffect(note) {
        scrollState.animateScrollTo(scrollState.maxValue)
    }

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
                },
                actions = {
                    IconButton(onClick = {
                        UpdateTask(
                            listOf(
                                TaskField.Title(title),
                                TaskField.Note(note),
                                TaskField.StartDate(startDate),
                                TaskField.StartTime(startTime),
                                TaskField.EndDate(endDate),
                                TaskField.EndTime(endTime)
                            )
                        ).with(onEvent)

                        onBackClick()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Save,
                            contentDescription = stringResource(R.string.save)
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
                modifier = Modifier.verticalScroll(scrollState)
            ) {
                Box(
                    modifier = Modifier.padding(
                        start = 56.dp,
                        top = 16.dp,
                        end = 16.dp,
                        bottom = 16.dp
                    )
                ) {

                    if (title.isEmpty()) {
                        Text(
                            text = "Add title*",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    BasicTextField(
                        value = title,
                        textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onSurface),
                        onValueChange = { title = it },
                        cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                HorizontalDivider()

                DateTimeRangePicker(
                    startDateLabel = "Select a start date",
                    startTimeLabel = "Select a start time",
                    endDateLabel = "Select an end date",
                    endTimeLabel = "Select an end time",
                    startDate = startDate,
                    startTime = startTime,
                    endDate = endDate,
                    endTime = endTime,
                    onStartDateSelected = { startDate = it },
                    onStartTimeSelected = { startTime = it },
                    onEndDateSelected = { endDate = it },
                    onEndTimeSelected = { endTime = it },
                )

                /*
                PrioritySliderComponent(
                    priority = priority,
                    onPriorityChanged = { TaskField.Priority(it).update(onEvent) },
                    modifier = Modifier.padding(
                        top = 6.dp,
                        bottom = 6.dp
                    )
                )
                 */

                HorizontalDivider()

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp)
                ) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.Notes, contentDescription = "")

                    Box {
                        if (note == null) {
                            Text(
                                text = stringResource(R.string.add_a_description),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        BasicTextField(
                            value = note ?: "",
                            textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onSurface),
                            onValueChange = { note = it.ifEmpty { null } },
                            cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
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

@ThemePreviews
@Composable
fun TaskEditorScreenFilledPreview() {
    PlaygroundTheme {
        MainTaskEditorContent(
            task = Task(
                title = "This is a title",
                startDate = LocalDate.now(),
                note = "This is a description"
            ),
            snackbarHostState = SnackbarHostState(),
            onEvent = {},
            onBackClick = {}
        )
    }
}
