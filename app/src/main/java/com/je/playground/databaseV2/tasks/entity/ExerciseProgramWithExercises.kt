package com.je.playground.databaseV2.tasks.entity

import androidx.room.Embedded
import androidx.room.Relation

class ExerciseProgramWithExercises(
    @Embedded var exerciseProgram : ExerciseProgram,

    @Relation(
        entity = Exercise::class,
        parentColumn = "exercise_program_id",
        entityColumn = "exercise_program_id"
    )
    var exercisesWithOccasions : List<ExerciseWithOccasions>
)
