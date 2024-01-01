package com.je.playground.database.exerciseprogram.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import java.time.DayOfWeek

@Entity(
    tableName = "exercise_program_weekday_schedule",
    primaryKeys = ["exercise_program_id", "weekday"],
    foreignKeys = [
        ForeignKey(
            entity = ExerciseProgram::class,
            parentColumns = ["exercise_program_id"],
            childColumns = ["exercise_program_id"],
            onDelete = ForeignKey.CASCADE
        )]
)
data class ExerciseProgramWeekdaySchedule(
    @ColumnInfo(name = "exercise_program_id") val exerciseProgramId : Long,
    @ColumnInfo(name = "weekday") val weekday : DayOfWeek
)
