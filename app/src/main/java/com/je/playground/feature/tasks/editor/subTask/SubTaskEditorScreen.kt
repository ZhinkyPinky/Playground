package com.je.playground.feature.tasks.editor.subTask

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
import com.je.playground.database.tasks.entity.SubTask
import com.je.playground.designsystem.component.task.taskeditor.EditorTopBar
import com.je.playground.designsystem.component.textfield.EditorTextField
import com.je.playground.designsystem.datetimepickers.DateTimeRangePicker
import com.je.playground.feature.tasks.editor.subTask.SubTaskEditorEvent.Save
import com.je.playground.feature.tasks.editor.subTask.SubTaskEditorViewModel.State
import com.je.playground.feature.tasks.editor.subTask.SubTaskEditorViewModel.State.Loading
import com.je.playground.feature.tasks.editor.subTask.SubTaskEditorViewModel.State.Ready
import com.je.playground.feature.tasks.editor.subTask.SubTaskEditorViewModel.State.Saved
import com.je.playground.feature.tasks.editor.subTask.SubTaskField.EndDate
import com.je.playground.feature.tasks.editor.subTask.SubTaskField.EndTime
import com.je.playground.feature.tasks.editor.subTask.SubTaskField.Note
import com.je.playground.feature.tasks.editor.subTask.SubTaskField.StartDate
import com.je.playground.feature.tasks.editor.subTask.SubTaskField.StartTime
import com.je.playground.feature.tasks.editor.subTask.SubTaskField.Title

@Composable
fun SubTaskEditorScreen(
    viewModel: SubTaskEditorViewModel,
    onBackClick: () -> Unit
) {
    val state: State by viewModel.subTaskEditorState.collectAsStateWithLifecycle()
    val snackbarHostState = LocalSnackHostState.current

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            event.consume()?.let { message ->
                snackbarHostState.showSnackbar(message)
            }
        }
    }

    SubTaskEditorScreen(
        state = state,
        onEvent = viewModel::onEvent,
        onBackClick = onBackClick
    )
}

@Composable
fun SubTaskEditorScreen(
    state: State,
    onEvent: (SubTaskEditorEvent) -> Unit,
    onBackClick: () -> Unit
) {
    when (state) {
        is Loading -> {}
        is Ready -> {
            SubTaskEditorContent(
                subTask = state.subTask,
                onEvent = onEvent,
                onBackClick = onBackClick
            )
        }

        is Saved -> onBackClick()
    }
}

@Composable
fun SubTaskEditorContent(
    subTask: SubTask,
    onEvent: (SubTaskEditorEvent) -> Unit,
    onBackClick: () -> Unit
) {
    val scrollState = rememberScrollState()
    LaunchedEffect(subTask.note) { scrollState.animateScrollTo(scrollState.maxValue) }

    Scaffold(
        topBar = {
            EditorTopBar(
                text = "Edit subtask",
                onAction = { onEvent(Save) },
                onNavigation = { onBackClick() }
            )
        },
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = paddingValues)
        ) {
            Column(modifier = Modifier.verticalScroll(scrollState)) {
                EditorTextField(
                    text = subTask.title,
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
                    startDate = subTask.startDate,
                    startTime = subTask.startTime,
                    endDate = subTask.endDate,
                    endTime = subTask.endTime,
                    onStartDateSelected = { StartDate(it).update(onEvent) },
                    onStartTimeSelected = { StartTime(it).update(onEvent) },
                    onEndDateSelected = { EndDate(it).update(onEvent) },
                    onEndTimeSelected = { EndTime(it).update(onEvent) },
                )

                HorizontalDivider()

                EditorTextField(
                    text = subTask.note,
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
