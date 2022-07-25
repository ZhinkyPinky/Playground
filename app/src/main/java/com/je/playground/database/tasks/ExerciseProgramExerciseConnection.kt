package com.je.playground.database.tasks

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "exercise_program_exercise_connection",
    primaryKeys = ["exercise_program_id", "exercise_id"],
    foreignKeys = [
        ForeignKey(
            entity = ExerciseProgram::class,
            parentColumns = ["exercise_program_id"],
            childColumns = ["exercise_program_id"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Exercise::class,
            parentColumns = ["exercise_id"],
            childColumns = ["exercise_id"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ExerciseProgramExerciseConnection(
    @NonNull @ColumnInfo(name = "exercise_program_id") var exerciseProgramId : Int,
    @NonNull @ColumnInfo(name = "exercise_id") var exerciseId : Int,
    @NonNull @ColumnInfo(name = "is_completed") var isCompleted : Boolean = false
)
