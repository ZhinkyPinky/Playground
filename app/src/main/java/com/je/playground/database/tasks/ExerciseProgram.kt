package com.je.playground.database.tasks

import androidx.annotation.NonNull
import androidx.room.*
import java.time.DayOfWeek
import java.time.LocalDateTime

@Entity(tableName = "exercise_program")
data class ExerciseProgram(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "exercise_program_id") val exerciseProgramId : Int = 0,
    @NonNull @ColumnInfo(name = "name") var name : String,
    @ColumnInfo(name = "date") var date : LocalDateTime? = LocalDateTime.now(),
    @ColumnInfo(name = "is_active") var isActive : Boolean = true
)

