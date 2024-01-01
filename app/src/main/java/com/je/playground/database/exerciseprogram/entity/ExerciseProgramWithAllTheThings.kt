package com.je.playground.database.exerciseprogram.entity

import androidx.room.Embedded
import androidx.room.Relation


class ExerciseProgramWithAllTheThings(
    @Embedded var exerciseProgram : ExerciseProgram,

    @Relation(
        entity = Exercise::class,
        parentColumn = "exercise_program_id",
        entityColumn = "exercise_program_id"
    )
    var exercises : List<Exercise> = emptyList(),

    @Relation(
        entity = ExerciseProgramWeekdaySchedule::class,
        parentColumn = "exercise_program_id",
        entityColumn = "exercise_program_id"
    )
    var weekdaySchedule : List<ExerciseProgramWeekdaySchedule> = emptyList()
)


