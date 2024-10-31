package com.je.playground.feature.tasks.editor.task

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.je.playground.LocalSnackHostState
import com.je.playground.R
import com.je.playground.database.tasks.entity.Task
import com.je.playground.designsystem.component.task.taskeditor.EditorTopBar
import com.je.playground.designsystem.component.textfield.EditorTextField
import com.je.playground.designsystem.datetimepickers.DateTimeRangePicker
import com.je.playground.feature.tasks.editor.task.TaskEditorEvent.*
import com.je.playground.feature.tasks.editor.task.TaskEditorViewModel.*
import com.je.playground.feature.tasks.editor.task.TaskEditorViewModel.State.*
import com.je.playground.feature.tasks.editor.task.TaskField.*
import com.je.playground.ui.theme.PlaygroundTheme
import com.je.playground.ui.theme.ThemePreviews
import java.time.LocalDate

@Composable
fun TaskEditorScreen(
    viewModel: TaskEditorViewModel,
    onSave: (Long) -> Unit,
    onBackClick: () -> Unit
) {
    val state: State by viewModel.taskEditorState.collectAsStateWithLifecycle()
    val snackbarHostState = LocalSnackHostState.current

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            event.consume()?.let { message ->
                snackbarHostState.showSnackbar(message)
            }
        }
    }

    TaskEditorScreen(
        state = state,
        onEvent = viewModel::onEvent,
        onSave = onSave,
        onBackClick = onBackClick
    )
}

@Composable
fun TaskEditorScreen(
    state: State,
    onEvent: (TaskEditorEvent) -> Unit,
    onSave: (Long) -> Unit,
    onBackClick: () -> Unit
) {
    when (state) {
        is Loading -> {}
        is Ready -> {
            TaskEditorContent(
                task = state.task,
                onEvent = onEvent,
                onBackClick = onBackClick
            )
        }

        is Saved -> {
            Log.d("Save", state.taskId.toString())
            onSave(state.taskId)
        }
    }
}

@Composable
fun TaskEditorContent(
    task: Task,
    onEvent: (TaskEditorEvent) -> Unit,
    onBackClick: () -> Unit
) {

    val scrollState = rememberScrollState()
    LaunchedEffect(task.note) {
        scrollState.animateScrollTo(scrollState.maxValue)
    }

    Scaffold(
        topBar = {
            EditorTopBar(
                text = stringResource(R.string.edit),
                onAction = { onEvent(SaveTask) },
                onNavigation = onBackClick
            )
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = paddingValues)
        ) {
            Column(
                modifier = Modifier.verticalScroll(scrollState)
            ) {

                EditorTextField(
                    text = task.title,
                    placeholderText = stringResource(R.string.title_required),
                    leadingIcon = { Spacer(modifier = Modifier.size(24.dp)) },
                    onValueChange = { Title(it).update(onEvent) }
                )

                HorizontalDivider()

                DateTimeRangePicker(
                    startDateLabel = "Select a start date",
                    startTimeLabel = "Select a start time",
                    endDateLabel = "Select an end date",
                    endTimeLabel = "Select an end time",
                    startDate = task.startDate,
                    startTime = task.startTime,
                    endDate = task.endDate,
                    endTime = task.endTime,
                    onStartDateSelected = { StartDate(it).update(onEvent) },
                    onStartTimeSelected = { StartTime(it).update(onEvent) },
                    onEndDateSelected = { EndDate(it).update(onEvent) },
                    onEndTimeSelected = { EndTime(it).update(onEvent) },
                )

                HorizontalDivider()

                EditorTextField(
                    text = task.note,
                    placeholderText = stringResource(R.string.add_a_note),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Notes,
                            contentDescription = "Edit note"
                        )
                    },
                    onValueChange = { Note(it.ifEmpty { null }).update(onEvent) }
                )
            }
        }
    }
}

@ThemePreviews
@Composable
fun TaskEditorScreenPreview() {
    PlaygroundTheme {
        TaskEditorContent(
            task = Task(),
            onEvent = {},
            onBackClick = {}
        )
    }
}

@ThemePreviews
@Composable
fun TaskEditorScreenFilledPreview() {
    PlaygroundTheme {
        TaskEditorContent(
            task = Task(
                title = "This is a title",
                startDate = LocalDate.now(),
                note = "This is a description"
            ),
            onEvent = {},
            onBackClick = {}
        )
    }
}
