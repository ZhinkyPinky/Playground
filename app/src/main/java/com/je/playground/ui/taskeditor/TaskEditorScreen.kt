package com.je.playground.ui.taskeditor

import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.je.playground.databaseV2.tasks.entity.Exercise
import com.je.playground.databaseV2.tasks.entity.ExerciseProgram
import com.je.playground.databaseV2.tasks.entity.SimpleTask
import com.je.playground.ui.tasklist.viewmodel.Priority
import com.je.playground.ui.tasklist.viewmodel.TaskType
import com.je.playground.ui.tasklist.viewmodel.TasksViewModelV2
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun TaskEditorScreen(
    tasksViewModelV2 : TasksViewModelV2,
    onBackPress : () -> Unit,
) {
    val taskEditorUiState : String /*TODO*/

    TaskEditorScreen(
        onBackPress = onBackPress,
        insertSimpleTask = tasksViewModelV2::insertSimpleTask,
        updateSimpleTask = tasksViewModelV2::updateSimpleTask,
        insertExerciseProgram = tasksViewModelV2::insertExerciseProgram,
        updateExerciseProgram = tasksViewModelV2::updateExerciseProgram,
        insertExercise = tasksViewModelV2::insertExercise,
        updateExercise = tasksViewModelV2::updateExercise
    )
}

@Composable
private fun TaskEditorScreen(
    onBackPress : () -> Unit,
    insertSimpleTask : (String, Priority, String?, LocalDate?, LocalTime?, LocalDate?, LocalTime?) -> Unit,
    updateSimpleTask : (SimpleTask) -> Unit = {},
    insertExerciseProgram : (String) -> Unit = {},
    updateExerciseProgram : (ExerciseProgram) -> Unit = {},
    insertExercise : (Exercise) -> Unit = {},
    updateExercise : (Exercise) -> Unit = {}
) {
    val context = LocalContext.current

    var taskType by remember {
        mutableStateOf("")
    }

    var title by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf("Low") }
    var note : String? by remember { mutableStateOf(null) }
    var weekdays : List<DayOfWeek>? by remember { mutableStateOf(null) }
    var dateFrom : LocalDate? by remember { mutableStateOf(null) }
    var timeFrom : LocalTime? by remember { mutableStateOf(null) }
    var dateTo : LocalDate? by remember { mutableStateOf(null) }
    var timeTo : LocalTime? by remember { mutableStateOf(null) }

    println(title)
    println(priority)
    println(note)
    println(dateFrom)
    println(timeFrom)
    println(dateTo)
    println(timeTo)

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    backgroundColor = MaterialTheme.colors.primary
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = onBackPress) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = MaterialTheme.colors.secondary,
                                modifier = Modifier.padding(end = 2.dp)
                            )
                        }

                        Text(
                            text = "Edit",
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
                    }
                }

                Divider(
                    color = MaterialTheme.colors.primaryVariant
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (title != "") {
                        when (taskType) {
                            "SimpleTask" -> insertSimpleTask(
                                title,
                                Priority.valueOf(priority),
                                note,
                                dateFrom,
                                timeFrom,
                                dateTo,
                                timeTo
                            )
                            "ExerciseProgram" -> insertExerciseProgram
                        }
                        onBackPress()
                    } else Toast
                        .makeText(
                            context,
                            "Need title",
                            Toast.LENGTH_SHORT
                        )
                        .show()
                },
                backgroundColor = MaterialTheme.colors.onPrimary,
                content = {
                    Icon(
                        imageVector = Icons.Filled.Save,
                        contentDescription = "Add new task",
                        tint = MaterialTheme.colors.secondary
                    )
                }
            )
        },
        backgroundColor = MaterialTheme.colors.background
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                        stiffness = Spring.StiffnessMedium,
                    )
                )
        ) {

            TaskTypeComponent {
                taskType = it
            }

            if (TaskType
                    .values()
                    .any { it.name == taskType }
            ) {
                TitleTextFieldComponent {
                    title = it
                }

                Divider()

                if (taskType == "SimpleTask") {
                    PrioritySliderComponent {
                        priority = it
                    }

                    Divider()

                    DateTimePicker(
                        context = context,
                        title = "From",
                        savedDate = dateFrom,
                        onDateValueChange = {
                            dateFrom = it
                        },
                        onTimeValueChange = {
                            timeFrom = it
                        })

                    Divider()

                    DateTimePicker(
                        context = context,
                        title = "To",
                        savedDate = dateTo,
                        onDateValueChange = {
                            dateTo = it
                        },
                        onTimeValueChange = {
                            timeTo = it
                        })

                    Divider()

                    NoteEditComponent {
                        note = it
                    }

                    Divider()
                }
            }
        }
    }
}