package com.je.playground.view.taskview.taskeditor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.je.playground.database.tasks.entity.MainTask
import com.je.playground.view.taskview.taskeditor.datetimerangepicker.DateRangePicker
import com.je.playground.view.taskview.taskeditor.datetimerangepicker.TimeRangePicker
import com.je.playground.view.taskview.taskeditor.viewmodel.TaskEditorViewModel

@Composable
fun MainTaskEditorScreen(
    taskEditorViewModel : TaskEditorViewModel,
    onBackPress : () -> Unit
) {
    when (taskEditorViewModel.taskEditorUiState.collectAsState().value) {
        is TaskEditorViewModel.State.Loading -> {
            //TODO: Add loading screen?
        }

        is TaskEditorViewModel.State.Ready -> {
            MainTaskEditorScreen(
                mainTask = taskEditorViewModel.mainTask.collectAsState().value,
                updateMainTask = {
                    taskEditorViewModel.updateMainTask(it)
                    onBackPress()
                },
                onBackPress = onBackPress
            )

        }
    }
}

@Composable
fun MainTaskEditorScreen(
    mainTask : MainTask,
    updateMainTask : (MainTask) -> Unit,
    onBackPress : () -> Unit
) {
    var title by rememberSaveable { mutableStateOf(mainTask.title) }
    var note by rememberSaveable { mutableStateOf(mainTask.note) }
    var priority by rememberSaveable { mutableIntStateOf(mainTask.priority) }
    var startDate by rememberSaveable { mutableStateOf(mainTask.startDate) }
    var startTime by rememberSaveable { mutableStateOf(mainTask.startTime) }
    var endDate by rememberSaveable { mutableStateOf(mainTask.endDate) }
    var endTime by rememberSaveable { mutableStateOf(mainTask.endTime) }

    Scaffold(
        topBar = {
            EditorTopBar(
                text = "Edit",
                onSave = {
                    updateMainTask(
                        mainTask.copy(
                            title = title,
                            note = note,
                            priority = priority,
                            startDate = startDate,
                            startTime = startTime,
                            endDate = endDate,
                            endTime = endTime
                        )
                    )
                },
                onBackPress = onBackPress
            )
        },
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

                PrioritySliderComponent(
                    priority = mainTask.priority,
                    onPriorityChanged = { priority = it },
                    modifier = Modifier.padding(
                        top = 6.dp,
                        bottom = 6.dp
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