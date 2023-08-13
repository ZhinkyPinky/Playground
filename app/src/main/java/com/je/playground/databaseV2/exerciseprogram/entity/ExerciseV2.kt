package com.je.playground.databaseV2.exerciseprogram.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.Duration

@Entity(
    tableName = "exercise_V2",
    foreignKeys = [
        ForeignKey(
            entity = ExerciseProgramV2::class,
            parentColumns = ["exercise_program_v2_id"],
            childColumns = ["exercise_program_v2_id"],
            onDelete = ForeignKey.CASCADE
        )]
)
data class ExerciseV2(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "exercise_v2_id") val id : Long,
    @ColumnInfo(
        name = "exercise_program_v2_id",
        index = true
    ) val exerciseProgramId : Long,
    @ColumnInfo(name = "name") val name : String,
    @ColumnInfo(name = "sets") val sets : Int,
    @ColumnInfo(name = "reps") val reps : Int,
    @ColumnInfo(name = "duration") val duration : Duration? = null,
    @ColumnInfo(name = "weight") val weight : Int? = null,
    @ColumnInfo(name = "rest_time") val restTime : Duration
)