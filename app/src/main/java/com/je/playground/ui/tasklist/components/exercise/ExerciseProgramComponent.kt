package com.je.playground.ui.tasklist.components

import androidx.compose.runtime.*
import com.je.playground.databaseV2.tasks.entity.ExerciseOccasion
import com.je.playground.databaseV2.tasks.entity.TaskWithOccasions
import com.je.playground.databaseV2.tasks.entity.WeekdaySchedule
import com.je.playground.ui.tasklist.components.exercise.ExercisesComponent
import com.je.playground.ui.tasklist.components.schedule.ScheduleComponent
import com.je.playground.ui.tasklist.components.shared.BaseTaskComponent
import com.je.playground.ui.tasklist.components.shared.BaseTaskComponentV2
import com.je.playground.ui.tasklist.components.shared.SubContentComponent

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




