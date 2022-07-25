package com.je.playground.ui.tasks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.je.playground.database.tasks.ExerciseProgramExerciseConnection
import com.je.playground.ui.tasks.components.ExerciseProgramComponent
import com.je.playground.ui.tasks.components.NoteComponent
import com.je.playground.ui.tasks.components.SimpleTask.SimpleTaskComponent
import com.je.playground.ui.tasks.components.shared.BaseTaskComponent
import com.je.playground.ui.tasks.viewmodel.TasksUiState
import com.je.playground.ui.tasks.viewmodel.TasksViewModel


@Composable
fun TasksScreen(tasksViewModel : TasksViewModel) {
    val tasksUiState by tasksViewModel.tasksUiState.collectAsState()

    TasksScreen(
        tasksUiState = tasksUiState,
        setExerciseCompletion = tasksViewModel::updateExerciseProgramExerciseConnection
    )
}

@Composable
fun TasksScreen(
    tasksUiState : TasksUiState,
    setExerciseCompletion : (ExerciseProgramExerciseConnection) -> Unit
) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colors.primary)
            .fillMaxSize()
    ) {
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

        LazyColumn(
            modifier = Modifier
                .padding(top = 1.dp)
        ) {
            items(items = tasksUiState.simpleTasks) { simpleTask ->
                SimpleTaskComponent(
                    simpleTask = simpleTask
                ) {}
            }

            items(items = tasksUiState.exerciseProgramsWithExercisesAndConnections) { exerciseProgramWithExercisesAndConnections ->
                ExerciseProgramComponent(
                    exerciseProgramWithExercisesAndConnections = exerciseProgramWithExercisesAndConnections,
                    setExerciseCompletion = setExerciseCompletion
                )
            }
        }
    }
}

/*
@Composable
fun SingleActivity(name : String) {
    var completed by remember {
        mutableStateOf(false)
    }

    Column {
        Row(
            modifier = Modifier
                .background(if (completed) MaterialTheme.colors.secondary else MaterialTheme.colors.primary)
        ) {
            Text(
                text = name,
                style = title(MaterialTheme.colors.onPrimary),
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
                    .padding(8.dp)
            )

            Checkbox(
                checked = completed,
                onCheckedChange = { completed = !completed },
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colors.secondary,
                    checkmarkColor = MaterialTheme.colors.onPrimary,
                    uncheckedColor = MaterialTheme.colors.onPrimary
                ),
                modifier = Modifier.padding(8.dp)
            )
        }
        Divider(color = MaterialTheme.colors.secondary)
    }
}
 */

/*
@Composable
private fun MainContent2(exerciseProgramTask : ExerciseProgramTask) {
    var expanded by remember { mutableStateOf(false) }

    Surface(
        color = MaterialTheme.colors.primary
    ) {
        Column(
            modifier = Modifier
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessMedium,
                    )
                )
        ) {
            Row(
                modifier = Modifier
                    .background(if (expanded) MaterialTheme.colors.secondary else MaterialTheme.colors.primary)
            ) {
                Text(
                    text = exerciseProgramTask.name,
                    style = title(MaterialTheme.colors.onPrimary),
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(1f)
                        .align(Alignment.CenterVertically)
                )

                IconButton(
                    onClick = { expanded = !expanded },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Icon(
                        imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                        contentDescription = "Placeholder",
                        tint = MaterialTheme.colors.onPrimary
                    )
                }
            }

            Divider(color = MaterialTheme.colors.secondary)
            SubContent(
                expanded = expanded,
                exerciseProgramTask = exerciseProgramTask
            )
        }
    }
}
 */

/*
@Composable
private fun MainContent(exerciseProgramTask : ExerciseProgramTask) {
    var expanded by remember { mutableStateOf(false) }

    Surface(
        color = MaterialTheme.colors.primary
    ) {
        Column(
            modifier = Modifier
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessMedium,
                    )
                )
        ) {
            Row(
                modifier = Modifier
                    .background(if (expanded) MaterialTheme.colors.secondary else MaterialTheme.colors.primary)
            ) {
                Text(
                    text = exerciseProgramTask.name,
                    style = title(MaterialTheme.colors.onPrimary),
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(1f)
                        .align(Alignment.CenterVertically)
                )

                IconButton(
                    onClick = { expanded = !expanded },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Icon(
                        imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                        contentDescription = "Placeholder",
                        tint = MaterialTheme.colors.onPrimary
                    )
                }
            }

            Divider(color = MaterialTheme.colors.secondary)
            SubContent(
                expanded = expanded,
                exerciseProgramTask = exerciseProgramTask
            )
        }
    }
}
 */

/*
@Composable
private fun SubContent(
    expanded : Boolean,
    exerciseProgramTask : ExerciseProgramTask
) {
    if (expanded) {
        Column {
            Schedule(exerciseProgramTask.schedule)
            Spacer(
                modifier = Modifier
                    .height(2.dp)
                    .background(Color.White)
            )
            Exercises(exerciseProgramTask.exercises)
        }
        Divider(
            color = MaterialTheme.colors.secondary,
            thickness = 4.dp
        )
    }
}
 */

/*
@Composable
private fun Schedule(schedule : List<DayOfWeek>) {
    Column {
        LazyRow(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 1.dp)
        ) {
            items(items = schedule) { day ->
                var selected by remember {
                    mutableStateOf(false)
                }

                TextButton(
                    onClick = { selected = !selected },
                    shape = RectangleShape,
                    modifier = Modifier
                        .background(if (selected) MaterialTheme.colors.secondary else MaterialTheme.colors.primary)
                        .size(
                            54.dp,
                            50.dp
                        )
                ) {
                    Text(
                        text = day.name.substring(0..0),
                        style = subcontent(MaterialTheme.colors.onPrimary)
                    )
                }
            }
        }
        Divider(color = MaterialTheme.colors.secondary)
    }
}
 */

/*
@Composable
fun Exercises(exercises : List<Exercise>) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        exercises.forEach { exercise ->
            var expanded by remember {
                mutableStateOf(false)
            }

            var completed by remember {
                mutableStateOf(exercise.completed)
            }

            Surface(
                color = if (expanded) MaterialTheme.colors.secondary else MaterialTheme.colors.primary
            ) {
                Row(modifier = Modifier.padding(horizontal = 4.dp)) {
                    Text(
                        text = exercise.name,
                        style = subcontent(MaterialTheme.colors.onPrimary),
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .weight(1f)
                    )

                    val hapticFeedback = LocalHapticFeedback.current
                    Checkbox(
                        checked = completed,
                        onCheckedChange = {
                            completed = !completed
                            exercise.completed = completed
                            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                            println(exercise.completed)
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = if (expanded) MaterialTheme.colors.secondary else MaterialTheme.colors.primary,
                            checkmarkColor = MaterialTheme.colors.onPrimary,
                            uncheckedColor = MaterialTheme.colors.onPrimary
                        )
                    )

                    IconButton(
                        onClick = { expanded = !expanded }) {
                        Icon(
                            imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                            contentDescription = "Placeholder",
                            tint = MaterialTheme.colors.onPrimary
                        )
                    }


                }
            }

            if (expanded) Exercise(exercise)
        }
    }
}
 */

/*
@Composable
fun Exercise(exercise : Exercise) {
    Column(
        modifier = Modifier.padding(4.dp)
    ) {
        Text(
            text = "Sets:  ${exercise.sets}"
        )

        Text(
            text = "Reps: ${exercise.reps}"
        )

        Row {
            Text(
                text = "Rest: ${exercise.rest} s",
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            )

            var timerOn by remember { mutableStateOf(false) }
            IconButton(onClick = { timerOn = !timerOn }) {
                Icon(
                    imageVector = if (timerOn) Icons.Filled.Stop else Icons.Filled.PlayArrow,
                    contentDescription = "Start Timer",
                    tint = MaterialTheme.colors.onPrimary
                )
            }
        }

        Divider(
            color = MaterialTheme.colors.secondary,
        )
    }
}
 */

/*
@Composable
private fun OldMainContent(exerciseProgramTask : ExerciseProgramTask) {
    Card(
        elevation = 2.dp,
        modifier = Modifier
            .padding(
                vertical = 4.dp,
                horizontal = 8.dp
            )
            .fillMaxWidth()
    ) {
        var expanded by remember { mutableStateOf(false) }

        Surface(
            color = MaterialTheme.colors.primary,
            modifier = Modifier
                .border(
                    1.dp,
                    MaterialTheme.colors.secondary,
                    MaterialTheme.shapes.medium
                )
        ) {
            Column(
                modifier = Modifier
                    .animateContentSize(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioLowBouncy,
                            stiffness = Spring.StiffnessMedium
                        )
                    )
            ) {
                Row(modifier = Modifier.padding(8.dp)) {
                    Text(
                        text = exerciseProgramTask.name,
                        style = title(MaterialTheme.colors.secondary),
                        modifier = Modifier
                            .weight(1f)
                            .align(Alignment.CenterVertically)
                    )

                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(
                            imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                            contentDescription = "Placeholder"
                        )
                    }
                }

                SubContent(
                    expanded = expanded,
                    exerciseProgramTask = exerciseProgramTask
                )
            }
        }
    }
}
 */

