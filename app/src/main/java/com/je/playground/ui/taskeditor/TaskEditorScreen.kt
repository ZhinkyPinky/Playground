package com.je.playground.ui.taskeditor

import android.content.Context
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.je.playground.databaseV2.tasks.entity.Exercise
import com.je.playground.databaseV2.tasks.entity.ExerciseProgram
import com.je.playground.databaseV2.tasks.entity.SimpleTask
import com.je.playground.ui.taskeditor.datetimepicker.DateTimePicker
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

    var taskType by rememberSaveable { mutableStateOf("") }

    var title by rememberSaveable { mutableStateOf("") }
    var priority by rememberSaveable { mutableStateOf("Low") }
    var note : String? by rememberSaveable { mutableStateOf(null) }
    var weekdays : List<DayOfWeek>? by rememberSaveable { mutableStateOf(null) }
    var dateFrom : LocalDate? by rememberSaveable { mutableStateOf(null) }
    var timeFrom : LocalTime? by rememberSaveable { mutableStateOf(null) }
    var dateTo : LocalDate? by rememberSaveable { mutableStateOf(null) }
    var timeTo : LocalTime? by rememberSaveable { mutableStateOf(null) }

    println(taskType)
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

                        IconButton(
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
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Save,
                                contentDescription = "Save task",
                                tint = MaterialTheme.colors.secondary,
                            )
                        }
                    }
                }

                Divider(
                    color = MaterialTheme.colors.primaryVariant
                )
            }
        },
        backgroundColor = MaterialTheme.colors.background
    ) { padding ->
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
                .padding(padding)
        ) {
            TaskTypeComponent(
                taskType = taskType,
                onValueChange = { taskType = it }
            )

            if (TaskType
                    .values()
                    .any { it.name == taskType }
            ) {
                TextFieldComponent(
                    labelText = "Title*",
                    value = title,
                    isSingleLine = true,
                    onValueChange = { title = it }
                )

                Divider()

                when (taskType) {
                    "SimpleTask" -> SimpleTaskEditComponent(
                        context = context,
                        note = note,
                        dateFrom = dateFrom,
                        dateTo = dateTo,
                        timeFrom = timeFrom,
                        timeTo = timeTo,
                        onPriorityChanged = { priority = it },
                        onNoteChanged = { note = it },
                        onDateFromChanged = {
                            dateFrom = it
                            if (dateTo?.isBefore(dateFrom) == true) {
                                dateTo = dateFrom
                            }
                        },
                        onDateToChanged = {
                            dateTo = it
                            if (dateFrom?.isAfter(dateTo) == true) {
                                dateFrom = dateTo
                            }
                        },
                        onTimeFromChanged = {
                            if (dateFrom == null) dateFrom = LocalDate.now();
                            timeFrom = it
                        },
                        onTimeToChanged = {
                            if (dateTo == null) dateTo = dateFrom;
                            timeTo = it
                        }
                    )
                    "ExerciseProgram" -> ExerciseProgramEditComponent()
                }
            }
        }
    }
}

@Composable fun ExerciseProgramEditComponent() {
    TODO("Not yet implemented")
}

@Composable
fun SimpleTaskEditComponent(
    context : Context,
    note : String?,
    dateFrom : LocalDate?,
    dateTo : LocalDate?,
    timeFrom : LocalTime?,
    timeTo : LocalTime?,
    onPriorityChanged : (String) -> Unit,
    onNoteChanged : (String) -> Unit,
    onDateFromChanged : (LocalDate?) -> Unit,
    onDateToChanged : (LocalDate?) -> Unit,
    onTimeFromChanged : (LocalTime?) -> Unit,
    onTimeToChanged : (LocalTime?) -> Unit,
) {
    PrioritySliderComponent(onPriorityChanged = onPriorityChanged)

    Divider()

    Column(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMedium,
                )
            )
    ) {
        TextFieldComponent(
            labelText = "Note",
            value = note ?: "",
            isSingleLine = false,
            onValueChange = onNoteChanged
        )
    }

    Divider()

    DateTimePicker(
        context = context,
        title = "From",
        savedDate = dateFrom,
        savedTime = timeFrom,
        onDateValueChange = onDateFromChanged,
        onTimeValueChange = onTimeFromChanged
    )

    Divider()

    if (dateFrom != null || timeFrom != null) {
        DateTimePicker(
            context = context,
            title = "To",
            savedDate = dateTo,
            savedTime = timeTo,
            onDateValueChange = onDateToChanged,
            onTimeValueChange = onTimeToChanged
        )

        Divider()
    }
}