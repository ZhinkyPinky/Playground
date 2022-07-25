package com.je.playground.ui.tasks.components.exercise

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.je.playground.database.tasks.Exercise
import com.je.playground.database.tasks.ExerciseProgramExerciseConnection
import com.je.playground.ui.tasks.components.shared.CheckboxComponent
import com.je.playground.ui.tasks.components.shared.ExpandButtonComponent
import com.je.playground.ui.theme.subcontent

@Composable
fun ExercisesComponent(
    exercises : List<Exercise>,
    exerciseProgramExercisesConnections : List<ExerciseProgramExerciseConnection>,
    setExerciseCompletion : (ExerciseProgramExerciseConnection) -> Unit
) {
    val hapticFeedback = LocalHapticFeedback.current

    Spacer(modifier = Modifier.height(1.dp))

    Column {
        exercises.forEach { exercise ->

            var expanded by remember {
                mutableStateOf(false)
            }

            var exerciseProgramExerciseConnection : ExerciseProgramExerciseConnection? = null
            var isCompleted = false
            exerciseProgramExercisesConnections.forEach {
                if (exercise.id == it.exerciseId) {
                    exerciseProgramExerciseConnection = it
                    isCompleted = it.isCompleted
                }
            }


            Surface(
                color = if (expanded) MaterialTheme.colors.primaryVariant else MaterialTheme.colors.primary
            ) {
                Row(
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text(
                        text = exercise.name,
                        style = subcontent(MaterialTheme.colors.secondary),
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .weight(1f)
                            .padding(start = 16.dp)
                    )

                    CheckboxComponent(isCompleted) {
                        isCompleted = !isCompleted
                        exerciseProgramExerciseConnection?.isCompleted = isCompleted
                        exerciseProgramExerciseConnection?.let(setExerciseCompletion)
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                    }

                    ExpandButtonComponent(expanded) {
                        expanded = !expanded
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                    }
                }
            }

            Divider(
                color = MaterialTheme.colors.primaryVariant,
                modifier = Modifier.padding(
                    top = 1.dp,
                    bottom = 1.dp
                )
            )

            if (expanded) {
                ExerciseComponent(exercise)

                Divider(
                    color = MaterialTheme.colors.primaryVariant,
                    modifier = Modifier.padding(bottom = 1.dp)
                )
            }
        }
    }
}