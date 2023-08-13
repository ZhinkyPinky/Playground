package com.je.playground.ui.exerciseprogram.exercise

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import com.je.playground.databaseV2.exerciseprogram.entity.ExerciseV2
import com.je.playground.databaseV2.tasks.entity.ExerciseOccasion
import com.je.playground.ui.sharedcomponents.CheckboxComponent
import com.je.playground.ui.sharedcomponents.ExpandButtonComponent
import com.je.playground.ui.theme.subcontent

@Composable
fun ExercisesV2Component(
    exercises : List<ExerciseV2>,
    updateExerciseOccasion : (ExerciseOccasion) -> Unit
) {
    val hapticFeedback = LocalHapticFeedback.current

    Column(
        verticalArrangement = Arrangement.spacedBy(1.dp),
        modifier = Modifier.wrapContentHeight()
    ) {
        Surface(
            color = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.wrapContentHeight()
        ) {
            Column(
                modifier = Modifier
                    .wrapContentHeight()
                    .padding(
                        top = 12.dp,
                        bottom = 1.dp,
                    )
            ) {
                Text(
                    text = "Exercises",
                    style = subcontent(MaterialTheme.colorScheme.onPrimaryContainer),
                    modifier = Modifier
                        .padding(
                            start = 16.dp,
                            bottom = 12.dp
                        )
                )

                LinearProgressIndicator(
                    progress = 0.5f,
                    color = MaterialTheme.colorScheme.onPrimary,
                    trackColor = MaterialTheme.colorScheme.background,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                )
            }
        }

        exercises.forEach { exercise ->
            var expanded by remember {
                mutableStateOf(false)
            }

            var isCompleted = false

            Surface(
                color = if (expanded) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.primaryContainer
            ) {
                Row(
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    CheckboxComponent(
                        isChecked = isCompleted,
                        modifier = Modifier
                    ) {
                        isCompleted = !isCompleted
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                    }

                    Text(
                        text = exercise.name,
                        style = subcontent(MaterialTheme.colorScheme.onPrimaryContainer),
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .weight(1f)
                            .padding(start = 16.dp)
                    )

                    ExpandButtonComponent(expanded) {
                        expanded = !expanded
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                    }
                }
            }

            if (expanded) {
                ExerciseComponentV2(exercise)
            }
        }
    }
}