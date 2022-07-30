package com.je.playground.databaseV2.tasks.entity

import androidx.room.Embedded
import androidx.room.Relation

data class TaskWithOccasions(
    @Embedded val task : Task,

    @Relation(
        entity = SimpleTask::class,
        parentColumn = "task_id",
        entityColumn = "simple_task_id"
    )
    val simpleTask : SimpleTask?,

    @Embedded
    val exerciseProgramWithExercises : ExerciseProgramWithExercises?,

    @Relation(
        entity = WeekdaySchedule::class,
        parentColumn = "task_id",
        entityColumn = "task_id"
    )
    val weekdaySchedule : List<WeekdaySchedule>,

    @Relation(
        entity = TaskOccasion::class,
        parentColumn = "task_id",
        entityColumn = "task_id"
    )
    val taskOccasions : List<TaskOccasion>
)