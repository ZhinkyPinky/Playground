package com.je.playground.ui.tasklist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.je.playground.databaseV2.tasks.entity.ExerciseOccasion
import com.je.playground.databaseV2.tasks.entity.Task
import com.je.playground.databaseV2.tasks.entity.TaskOccasion
import com.je.playground.databaseV2.tasks.entity.WeekdaySchedule
import com.je.playground.ui.tasklist.components.ExerciseProgramComponent
import com.je.playground.ui.tasklist.components.SimpleTask.SimpleTaskComponent
import com.je.playground.ui.tasklist.viewmodel.Priority
import com.je.playground.ui.tasklist.viewmodel.TasksUiStateV2
import com.je.playground.ui.tasklist.viewmodel.TasksViewModelV2
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime


@Composable
fun TasksScreen(
    navigateToTaskEditWindow : () -> Unit,
    tasksViewModelV2 : TasksViewModelV2
) {
    val tasksUiState by tasksViewModelV2.tasksUiState.collectAsState()

    TasksScreen(
        tasksUiState = tasksUiState,
        priorities = tasksUiState.priorities,
        navigateToTaskEditWindow = navigateToTaskEditWindow,
        updateTaskOccasion = tasksViewModelV2::updateTaskOccasion,
        insertWeekdayScheduleEntry = tasksViewModelV2::insertWeekdayScheduleEntry,
        deleteWeekdayScheduleEntry = tasksViewModelV2::deleteWeekdayScheduleEntry,
        deleteTask = tasksViewModelV2::deleteTask,
        insertSimpleTask = tasksViewModelV2::insertSimpleTask,
        updateExerciseOccasion = tasksViewModelV2::updateExerciseOccasion,
    )
}

@Composable
fun TasksScreen(
    tasksUiState : TasksUiStateV2,
    priorities : List<Priority>,
    navigateToTaskEditWindow : () -> Unit,
    updateTaskOccasion : (TaskOccasion) -> Unit,
    insertWeekdayScheduleEntry : (WeekdaySchedule) -> Unit,
    deleteWeekdayScheduleEntry : (WeekdaySchedule) -> Unit,
    deleteTask : (Task) -> Unit,
    insertSimpleTask : (String, Priority, String, LocalDate, LocalTime, LocalDate, LocalTime) -> Unit,
    updateExerciseOccasion : (ExerciseOccasion) -> Unit
) {
    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    backgroundColor = MaterialTheme.colors.primary
                ) {
                    IconButton(
                        onClick = { /*TODO*/ },
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "Menu",
                            tint = MaterialTheme.colors.secondary,
                        )
                    }

                    Text(
                        text = "Tasks",
                        color = MaterialTheme.colors.secondary,
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

                    IconButton(onClick = {/* TODO */ }) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = "Config",
                            tint = MaterialTheme.colors.secondary,
                            modifier = Modifier.padding(end = 2.dp)
                        )
                    }
                }

                Divider(
                    color = MaterialTheme.colors.primaryVariant,
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navigateToTaskEditWindow()
                },
                backgroundColor = MaterialTheme.colors.onPrimary,
                content = {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add new task",
                        tint = MaterialTheme.colors.secondary
                    )
                }
            )
        },

        content = {
            LazyColumn(
                modifier = Modifier
                    .padding(top = 1.dp)
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
    )
}

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

