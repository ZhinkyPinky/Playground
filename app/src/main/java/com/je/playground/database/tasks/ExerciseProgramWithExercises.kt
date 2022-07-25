package com.je.playground.database.tasks

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class ExerciseProgramWithExercises(
    @Embedded var exerciseProgram : ExerciseProgram,
    @Relation(
        entity = Exercise::class,
        parentColumn = "exercise_program_id",
        entityColumn = "exercise_id",
        associateBy = Junction(
            ExerciseProgramExerciseConnection::class,
            parentColumn = "exercise_program_id",
            entityColumn = "exercise_id"
        )
    )
    var exercises : List<Exercise>
)