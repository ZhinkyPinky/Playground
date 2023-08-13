package com.je.playground.ui.tasklist.components.deprecated.exercise

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.je.playground.databaseV2.tasks.entity.ExerciseOccasion
import com.je.playground.databaseV2.tasks.entity.ExerciseWithOccasions
import com.je.playground.ui.sharedcomponents.CheckboxComponent
import com.je.playground.ui.sharedcomponents.ExpandButtonComponent
import com.je.playground.ui.theme.subcontent

@Composable
fun ExercisesComponent(
    exercisesWithOccasions : List<ExerciseWithOccasions>,
    updateExerciseOccasion : (ExerciseOccasion) -> Unit
) {
    val hapticFeedback = LocalHapticFeedback.current

    Spacer(modifier = Modifier.height(1.dp))

    Column {
        exercisesWithOccasions.forEach { exerciseWithOccasions ->

            var expanded by remember {
                mutableStateOf(false)
            }

            var isCompleted = false

            Surface(
                color = if (expanded) MaterialTheme.colors.primaryVariant else MaterialTheme.colors.primary
            ) {
                Row(
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text(
                        text = exerciseWithOccasions.exercise.name,
                        style = subcontent(MaterialTheme.colors.secondary),
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .weight(1f)
                            .padding(start = 16.dp)
                    )

                    CheckboxComponent(
                        isChecked = isCompleted,
                        modifier = Modifier
                    ) {
                        isCompleted = !isCompleted
                        exerciseWithOccasions.exerciseOccasions.first().isCompleted = isCompleted
                        exerciseWithOccasions.exerciseOccasions
                            .first()
                            .let(updateExerciseOccasion)
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
                ExerciseComponent(exerciseWithOccasions.exercise)

                Divider(
                    color = MaterialTheme.colors.primaryVariant,
                    modifier = Modifier.padding(bottom = 1.dp)
                )
            }
        }
    }
}