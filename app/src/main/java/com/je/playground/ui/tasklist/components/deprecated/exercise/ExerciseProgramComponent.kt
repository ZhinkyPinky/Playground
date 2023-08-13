package com.je.playground.ui.tasklist.components

import androidx.compose.runtime.Composable
import com.je.playground.databaseV2.tasks.entity.ExerciseOccasion
import com.je.playground.databaseV2.tasks.entity.TaskWithOccasions
import com.je.playground.databaseV2.tasks.entity.WeekdaySchedule
import com.je.playground.ui.schedule.ScheduleComponent
import com.je.playground.ui.tasklist.components.deprecated.exercise.ExercisesComponent
import com.je.playground.ui.tasklist.components.shared.BaseTaskComponentV2

@Composable
fun ExerciseProgramComponent(
    name : String,
    taskWithOccasions : TaskWithOccasions,
    updateExerciseOccasion : (ExerciseOccasion) -> Unit,
    insertWeekdayScheduleEntry : (WeekdaySchedule) -> Unit,
    deleteWeekdayScheduleEntry : (WeekdaySchedule) -> Unit
) {
    BaseTaskComponentV2(
        title = name,
        taskWithOccasions = taskWithOccasions,
        subContent = mutableListOf(
            {
                ScheduleComponent(
                    taskWithOccasions = taskWithOccasions,
                    insertWeekdayScheduleEntry = insertWeekdayScheduleEntry,
                    deleteWeekdayScheduleEntry = deleteWeekdayScheduleEntry
                )
            },
            {
                taskWithOccasions.exerciseProgramWithExercises?.let {
                    ExercisesComponent(
                        it.exercisesWithOccasions,
                        updateExerciseOccasion
                    )
                }
            }
        )
    )
}




