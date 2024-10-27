package com.je.playground.feature.tasks.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.je.playground.R
import com.je.playground.database.tasks.entity.SubTask
import com.je.playground.database.tasks.entity.Task
import com.je.playground.database.tasks.entity.TaskWithSubTasks
import com.je.playground.designsystem.component.task.tasklist.TaskComponent
import com.je.playground.ui.theme.PlaygroundTheme
import com.je.playground.ui.theme.ThemePreviews
import java.time.LocalDate
import java.time.LocalTime


@Composable
fun TasksScreen(
    viewModel: TaskListViewModel,
    navigateToTaskEditScreen: (Long) -> Unit,
) {
    val tasksUiState by viewModel.tasksUiState.collectAsState()

    TasksScreen(
        tasksWithSubTasks = tasksUiState.mainTasksWithSubTasks,
        navigateToTaskEditScreen = navigateToTaskEditScreen,
        onEvent = viewModel::onEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(
    tasksWithSubTasks: List<TaskWithSubTasks>,
    navigateToTaskEditScreen: (Long) -> Unit,
    onEvent: (TaskListEvent) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.tasks_title)) },
                actions = {
                    IconButton(
                        onClick = { navigateToTaskEditScreen(-1L) }) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Add new task",
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(1.dp),
            contentPadding = PaddingValues(vertical = 1.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            itemsIndexed(
                items = tasksWithSubTasks,
                key = { _, tasksWithSubTasks -> tasksWithSubTasks.task.mainTaskId }) { _, taskWithSubTasks ->
                if (!taskWithSubTasks.task.isArchived) { //TODO: Just retrieve unarchived???
                    TaskComponent(
                        taskWithSubTasks = taskWithSubTasks,
                        navigateToTaskEditScreen = navigateToTaskEditScreen,
                        onEvent = onEvent
                    )
                }
            }
        }
    }
}

@ThemePreviews
@Composable
fun TaskListScreenPreview() {
    val tasksWithSubTasks = listOf(
        TaskWithSubTasks(
            task = Task(
                title = "Test",
                note = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                startDate = LocalDate.now().plusDays(50),
                endDate = LocalDate.now().plusDays(58),
                startTime = LocalTime.now(),
                endTime = LocalTime.now().plusHours(1)
            ),
            subTasks = listOf(
                SubTask(
                    title = "TestSubTask1",
                    note = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                    startDate = LocalDate.now().plusDays(50),
                    endDate = LocalDate.now().plusDays(58),
                    startTime = LocalTime.now(),
                    endTime = LocalTime.now().plusHours(1),
                    isCompleted = true
                ),
                SubTask(
                    title = "TestSubTask2",
                    note = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                    isCompleted = false
                )
            )
        ),
        TaskWithSubTasks(
            task = Task(
                title = "Test",
                note = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                startDate = LocalDate.now().plusDays(50),
                endDate = LocalDate.now().plusDays(58),
                startTime = LocalTime.now(),
                endTime = LocalTime.now().plusHours(1)
            ),
            subTasks = listOf(
                SubTask(
                    title = "TestSubTask1",
                    note = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                    startDate = LocalDate.now().plusDays(50),
                    endDate = LocalDate.now().plusDays(58),
                    startTime = LocalTime.now(),
                    endTime = LocalTime.now().plusHours(1),
                    isCompleted = true
                ),
                SubTask(
                    title = "TestSubTask2",
                    note = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                    isCompleted = false
                )
            )
        ),
        TaskWithSubTasks(
            task = Task(
                title = "Test",
                note = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                startDate = LocalDate.now().plusDays(50),
                endDate = LocalDate.now().plusDays(58),
                startTime = LocalTime.now(),
                endTime = LocalTime.now().plusHours(1)
            ),
            subTasks = listOf(
                SubTask(
                    title = "TestSubTask1",
                    note = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                    startDate = LocalDate.now().plusDays(50),
                    endDate = LocalDate.now().plusDays(58),
                    startTime = LocalTime.now(),
                    endTime = LocalTime.now().plusHours(1),
                    isCompleted = true
                ),
                SubTask(
                    title = "TestSubTask2",
                    note = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                    isCompleted = false
                )
            )
        ),
    )

    PlaygroundTheme {
       TasksScreen(
           tasksWithSubTasks = tasksWithSubTasks,
           navigateToTaskEditScreen = {},
           onEvent = {}
       )
    }
}