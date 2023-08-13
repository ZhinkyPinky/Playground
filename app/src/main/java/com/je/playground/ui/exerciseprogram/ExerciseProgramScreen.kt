package com.je.playground.ui.exerciseprogram

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
import com.je.playground.databaseV2.exerciseprogram.entity.ExerciseProgramV2
import com.je.playground.databaseV2.exerciseprogram.entity.ExerciseProgramWithExercisesV2
import com.je.playground.databaseV2.exerciseprogram.entity.ExerciseV2
import com.je.playground.ui.exerciseprogram.exercise.ExercisesV2Component
import com.je.playground.ui.schedule.ScheduleComponent
import kotlinx.coroutines.launch
import java.time.Duration

@Composable
fun ExerciseProgramScreen(
    exerciseProgramViewModel : ExerciseProgramViewModel,
    drawerState : DrawerState,
    navigateToExerciseProgramEditScreen : () -> Unit
) {
    val exerciseProgramUiState by exerciseProgramViewModel.exerciseProgramUiState.collectAsState()

    ExerciseProgramScreen(
        exerciseProgramWithExercisesV2 = ExerciseProgramWithExercisesV2(
            exerciseProgramV2 = ExerciseProgramV2(
                id = 1L,
                name = "Test",
                isActive = true
            ),
            listOf(
                ExerciseV2(
                    id = 1L,
                    exerciseProgramId = 1L,
                    name = "Row",
                    sets = 8,
                    reps = 3,
                    restTime = Duration.ofSeconds(90)
                ),
                ExerciseV2(
                    id = 2L,
                    exerciseProgramId = 1L,
                    name = "Pull ups",
                    sets = 8,
                    reps = 3,
                    restTime = Duration.ofSeconds(90)
                ),
                ExerciseV2(
                    id = 3L,
                    exerciseProgramId = 1L,
                    name = "Push ups",
                    sets = 8,
                    reps = 3,
                    restTime = Duration.ofSeconds(90)
                )
            )
        ),
        drawerState = drawerState,
        navigateToExerciseProgramEditScreen = navigateToExerciseProgramEditScreen
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseProgramScreen(
    exerciseProgramWithExercisesV2 : ExerciseProgramWithExercisesV2,
    drawerState : DrawerState,
    navigateToExerciseProgramEditScreen : () -> Unit
) {
    Scaffold(
        topBar = {
            val coroutineScope = rememberCoroutineScope()

            TopAppBar(
                title = {
                    Text(
                        text = exerciseProgramWithExercisesV2.exerciseProgramV2.name,
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
                        onClick = { navigateToExerciseProgramEditScreen() }) {
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
                taskWithOccasions = null,
                insertWeekdayScheduleEntry = { },
                deleteWeekdayScheduleEntry = { }
            )

            ExercisesV2Component(
                exercises = exerciseProgramWithExercisesV2.exerciseV2,
                updateExerciseOccasion = {}
            )
        }
    }
}