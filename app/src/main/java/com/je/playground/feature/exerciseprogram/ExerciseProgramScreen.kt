package com.je.playground.feature.exerciseprogram

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.MoreVert
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.je.playground.database.exerciseprogram.entity.ExerciseProgram
import com.je.playground.database.exerciseprogram.entity.ExerciseProgramWithAllTheThings
import com.je.playground.deprecated.exercise.ExercisesComponent
import com.je.playground.deprecated.schedule.ScheduleComponent
import com.je.playground.feature.exerciseprogram.viewmodel.ExerciseProgramViewModel
import kotlinx.coroutines.launch

@Composable
fun ExerciseProgramScreen(
    exerciseProgramViewModel : ExerciseProgramViewModel,
    drawerState : DrawerState,
    navigateToExerciseProgramEditScreen : (Long) -> Unit
) {
    val exerciseProgramUiState by exerciseProgramViewModel.exerciseProgramUiState.collectAsState()

    if (exerciseProgramUiState.exerciseProgramsWithAllTheThings.isNotEmpty()) {
        ExerciseProgramScreen(
            exerciseProgramWithAllTheThings = exerciseProgramUiState.exerciseProgramsWithAllTheThings.first(),
            /*
        ExerciseProgramWithAllTheThings(
            exerciseProgram = ExerciseProgram(
                exerciseProgramId = 1L,
                title = "Test",
                isActive = true
            ),
            listOf(
                Exercise(
                    id = 1L,
                    exerciseProgramId = 1L,
                    name = "Row",
                    sets = 8,
                    reps = 3,
                    restTime = Duration.ofSeconds(90)
                ),
                Exercise(
                    id = 2L,
                    exerciseProgramId = 1L,
                    name = "Pull ups",
                    sets = 8,
                    reps = 3,
                    restTime = Duration.ofSeconds(90)
                ),
                Exercise(
                    id = 3L,
                    exerciseProgramId = 1L,
                    name = "Push ups",
                    sets = 8,
                    reps = 3,
                    restTime = Duration.ofSeconds(90)
                )
            ),
            listOf(
                ExerciseProgramWeekdaySchedule(
                    1L,
                    DayOfWeek.MONDAY
                ),
                ExerciseProgramWeekdaySchedule(
                    1L,
                    DayOfWeek.WEDNESDAY
                ),
                ExerciseProgramWeekdaySchedule(
                    1L,
                    DayOfWeek.FRIDAY
                ),
                ExerciseProgramWeekdaySchedule(
                    1L,
                    DayOfWeek.SUNDAY
                ),
            )
        )
         */
            drawerState = drawerState,
            navigateToExerciseProgramEditScreen = navigateToExerciseProgramEditScreen
        )
    } else {
        exerciseProgramViewModel.saveExerciseProgramWithAllTheThings(
            ExerciseProgramWithAllTheThings(
                exerciseProgram = ExerciseProgram(
                    exerciseProgramId = 0L,
                    title = "Test",
                    isActive = true
                )
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseProgramScreen(
    exerciseProgramWithAllTheThings : ExerciseProgramWithAllTheThings,
    drawerState : DrawerState,
    navigateToExerciseProgramEditScreen : (Long) -> Unit
) {
    Scaffold(
        topBar = {
            val coroutineScope = rememberCoroutineScope()
            TopAppBar(
                title = {
                    Text(
                        text = exerciseProgramWithAllTheThings.exerciseProgram.title,
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
                            imageVector = Icons.Filled.DirectionsRun,
                            contentDescription = "Menu",
                            tint = MaterialTheme.colorScheme.onPrimary,
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { navigateToExerciseProgramEditScreen(exerciseProgramWithAllTheThings.exerciseProgram.exerciseProgramId) }) {
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
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.spacedBy(1.dp),
            modifier = Modifier
                .padding(paddingValues)
                .padding(top = 1.dp)
        ) {
            ScheduleComponent(
                selectedWeekDays = exerciseProgramWithAllTheThings.weekdaySchedule.map { it.weekday },
                insertWeekdayScheduleEntry = { }
            ) { }

            ExercisesComponent(
                exercises = exerciseProgramWithAllTheThings.exercises,
            )
        }
    }
}