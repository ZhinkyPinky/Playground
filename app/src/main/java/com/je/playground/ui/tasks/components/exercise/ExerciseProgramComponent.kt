package com.je.playground.ui.tasks.components

import androidx.compose.runtime.Composable
import com.je.playground.database.tasks.ExerciseProgramExerciseConnection
import com.je.playground.database.tasks.ExerciseProgramWithExercisesAndConnections
import com.je.playground.ui.tasks.components.exercise.ExercisesComponent
import com.je.playground.ui.tasks.components.shared.BaseTaskComponent
import com.je.playground.ui.tasks.components.shared.SubContentComponent

@Composable
fun ExerciseProgramComponent(
    exerciseProgramWithExercisesAndConnections : ExerciseProgramWithExercisesAndConnections,
    setExerciseCompletion : (ExerciseProgramExerciseConnection) -> Unit
) {
    BaseTaskComponent(
        title = exerciseProgramWithExercisesAndConnections.exerciseProgramWithExercises.exerciseProgram.name,
        subContent = {
            SubContentComponent(
                content = listOf(
                    { ScheduleComponent() },
                    {
                        ExercisesComponent(
                            exerciseProgramWithExercisesAndConnections.exerciseProgramWithExercises.exercises,
                            exerciseProgramWithExercisesAndConnections.exerciseProgramExerciseConnection,
                            setExerciseCompletion
                        )
                    }
                )
            )
        }
    )
}


