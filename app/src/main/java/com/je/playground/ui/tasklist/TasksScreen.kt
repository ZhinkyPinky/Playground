package com.je.playground.ui.tasklist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.je.playground.databaseV2.tasks.entity.ExerciseOccasion
import com.je.playground.databaseV2.tasks.entity.Task
import com.je.playground.databaseV2.tasks.entity.TaskOccasion
import com.je.playground.databaseV2.tasks.entity.WeekdaySchedule
import com.je.playground.ui.tasklist.components.ExerciseProgramComponent
import com.je.playground.ui.tasklist.components.simpletask.SimpleTaskComponent
import com.je.playground.ui.tasklist.viewmodel.Priority
import com.je.playground.ui.tasklist.viewmodel.TasksUiStateV2
import com.je.playground.ui.tasklist.viewmodel.TasksViewModelV2
import kotlinx.coroutines.launch


@Composable
fun TasksScreen(
    navigateToTaskEditWindow : () -> Unit,
    drawerState : DrawerState,
    tasksViewModelV2 : TasksViewModelV2
) {
    val tasksUiState by tasksViewModelV2.tasksUiState.collectAsState()

    TasksScreen(
        tasksUiState = tasksUiState,
        priorities = tasksUiState.priorities,
        drawerState = drawerState,
        navigateToTaskEditWindow = navigateToTaskEditWindow,
        updateTaskOccasion = tasksViewModelV2::updateTaskOccasion,
        insertWeekdayScheduleEntry = tasksViewModelV2::insertWeekdayScheduleEntry,
        deleteWeekdayScheduleEntry = tasksViewModelV2::deleteWeekdayScheduleEntry,
        deleteTask = tasksViewModelV2::deleteTask,
        updateExerciseOccasion = tasksViewModelV2::updateExerciseOccasion,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(
    tasksUiState : TasksUiStateV2,
    priorities : List<Priority>,
    drawerState : DrawerState,
    navigateToTaskEditWindow : () -> Unit,
    updateTaskOccasion : (TaskOccasion) -> Unit,
    insertWeekdayScheduleEntry : (WeekdaySchedule) -> Unit,
    deleteWeekdayScheduleEntry : (WeekdaySchedule) -> Unit,
    deleteTask : (Task) -> Unit,
    updateExerciseOccasion : (ExerciseOccasion) -> Unit
) {
    Scaffold(
        topBar = {
            val coroutineScope = rememberCoroutineScope()

            Column {
                TopAppBar(
                    title = {
                        Text(
                            text = "Tasks",
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 22.sp,
                            maxLines = 1,
                            textAlign = TextAlign.Start,

                            modifier = Modifier
                                .padding(
                                    start = 0.dp,
                                    top = 4.dp,
                                    end = 8.dp,
                                    bottom = 4.dp
                                )
                                .weight(1f)
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                coroutineScope.launch {
                                    drawerState.open()
                                }
                            },
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Menu,
                                contentDescription = "Menu",
                                tint = MaterialTheme.colorScheme.onPrimary,
                            )
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = { navigateToTaskEditWindow() }) {
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = "Add new task",
                                tint = MaterialTheme.colorScheme.onPrimary,
                            )
                        }

                        IconButton(onClick = {/* TODO */ }) {
                            Icon(
                                imageVector = Icons.Filled.MoreVert,
                                contentDescription = "Config",
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.padding(end = 2.dp)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                )

                Divider(
                    color = MaterialTheme.colorScheme.background
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(1.dp),
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .padding(it)
        ) {

            items(items = tasksUiState.tasksWithOccasions) { taskWithOccasions ->
                key(taskWithOccasions.task.id) {
                    taskWithOccasions.simpleTask?.let {
                        SimpleTaskComponent(
                            taskWithOccasions = taskWithOccasions,
                            updateTaskOccasion = updateTaskOccasion,
                            deleteTask = deleteTask
                        )
                    }

                    taskWithOccasions.exerciseProgramWithExercises?.let {
                        ExerciseProgramComponent(
                            name = taskWithOccasions.exerciseProgramWithExercises.exerciseProgram.name,
                            taskWithOccasions = taskWithOccasions,
                            updateExerciseOccasion = updateExerciseOccasion,
                            insertWeekdayScheduleEntry = insertWeekdayScheduleEntry,
                            deleteWeekdayScheduleEntry = deleteWeekdayScheduleEntry
                        )
                    }
                }
            }
        }
    }
}

/*
@Composable
fun TopAppBar() {
    TopAppBar(
        backgroundColor = MaterialTheme.colors.primary
    ) {
        Text(
            text = "Tasks",
            color = MaterialTheme.colors.secondary,
            fontSize = 22.sp,
            maxLines = 1,
            textAlign = TextAlign.Start,

            modifier = Modifier
                .padding(
                    start = 16.dp,
                    top = 4.dp,
                    end = 8.dp,
                    bottom = 4.dp
                )
                .weight(1f)
        )

        IconButton(onClick = { /*TODO*/ }) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Add a new task",
                tint = MaterialTheme.colors.secondary,
                modifier = Modifier.padding()
            )
        }

        IconButton(
            onClick = { /*TODO*/ },
        ) {
            Icon(
                imageVector = Icons.Filled.Menu,
                contentDescription = "Menu",
                tint = MaterialTheme.colors.secondary,
                modifier = Modifier.padding(end = 8.dp)
            )
        }

    }

    Divider(
        color = MaterialTheme.colors.primaryVariant,
    )
}
 */

