package com.je.playground.designsystem.component.taskeditor

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.je.playground.database.tasks.entity.SubTask
import com.je.playground.designsystem.component.SnackbarComponent
import com.je.playground.designsystem.component.TextFieldComponent
import com.je.playground.designsystem.component.datetimerangepicker.DateRangePicker
import com.je.playground.designsystem.component.datetimerangepicker.TimeRangePicker
import com.je.playground.feature.tasks.editor.TaskEditorEvent
import com.je.playground.feature.tasks.editor.TaskEditorViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun SubTaskEditorScreen(
    subTaskIndex : Int = -1,
    taskEditorViewModel : TaskEditorViewModel,
    onBackPress : () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    val localCoroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        taskEditorViewModel.eventFlow.collectLatest { event ->
            when (event) {
                is TaskEditorViewModel.Event.ShowSnackbar -> {
                    localCoroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            message = event.message
                        )
                    }
                }

                is TaskEditorViewModel.Event.Saved -> {
                    onBackPress()
                }
            }
        }
    }

    when (taskEditorViewModel.taskEditorUiState.collectAsState().value) {
        is TaskEditorViewModel.State.Loading -> {
            //TODO: Add loading screen?
        }

        is TaskEditorViewModel.State.Ready -> {
            SubTaskEditorScreen(
                subTaskIndex = subTaskIndex,
                subTask = if (subTaskIndex == -1) SubTask() else taskEditorViewModel.subTasks[subTaskIndex],
                snackbarHostState = snackbarHostState,
                onEvent = taskEditorViewModel::onEvent,
                onBackPress = onBackPress
            )
        }
    }
}

@Composable
fun SubTaskEditorScreen(
    subTaskIndex : Int,
    subTask : SubTask,
    snackbarHostState : SnackbarHostState,
    onEvent : (TaskEditorEvent) -> Unit,
    onBackPress : () -> Unit
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
                    onEvent(
                        TaskEditorEvent.SaveSubTask(
                            subTaskIndex,
                            subTask.copy(
                                title = title,
                                note = note,
                                startDate = startDate,
                                startTime = startTime,
                                endDate = endDate,
                                endTime = endTime
                            )
                        )
                    )
                    //onBackPress()
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
