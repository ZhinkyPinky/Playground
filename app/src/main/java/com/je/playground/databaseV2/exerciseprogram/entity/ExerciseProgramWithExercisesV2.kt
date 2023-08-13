package com.je.playground.databaseV2.exerciseprogram.entity

import androidx.room.Embedded
import androidx.room.Relation


class ExerciseProgramWithExercisesV2(
    @Embedded var exerciseProgramV2 : ExerciseProgramV2,

    @Relation(
        entity = ExerciseV2::class,
        parentColumn = "exercise_program_v2_id",
        entityColumn = "exercise_program_v2_id"
    )
    var exerciseV2 : List<ExerciseV2>
)

