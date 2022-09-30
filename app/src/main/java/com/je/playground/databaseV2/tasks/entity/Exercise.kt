package com.je.playground.databaseV2.tasks.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.Duration

@Entity(
    tableName = "exercise",
    foreignKeys = [
        ForeignKey(
            entity = ExerciseProgram::class,
            parentColumns = ["exercise_program_id"],
            childColumns = ["exercise_program_id"],
            onDelete = ForeignKey.CASCADE
        )]
)

data class Exercise(
    @NonNull @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "exercise_id") val id : Long,
    @NonNull @ColumnInfo(name = "exercise_program_id") val exerciseProgramId : Int,
    @NonNull @ColumnInfo(name = "name") val name : String,
    @NonNull @ColumnInfo(name = "sets") val sets : Int,
    @NonNull @ColumnInfo(name = "reps") val reps : Int,
    @ColumnInfo(name = "duration") val duration : Duration? = null,
    @ColumnInfo(name = "weight") val weight : Int? = null,
    @NonNull @ColumnInfo(name = "rest_time") val restTime : Duration
)