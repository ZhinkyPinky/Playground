package com.je.playground.database.exerciseprogram.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "exercise_program",
)
data class ExerciseProgram(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "exercise_program_id") val exerciseProgramId : Long = 0L,
    @ColumnInfo(name = "title") var title : String = "",
    @ColumnInfo(name = "priority") var priority : Int = 0,
    @ColumnInfo(name = "is_active") var isActive : Boolean = true
)
