package com.je.playground.databaseV2.tasks.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercise_program")
data class ExerciseProgram(
    @NonNull @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "exercise_program_id") val id : Long,
    @NonNull @ColumnInfo(name = "name") var name : String,
    @NonNull @ColumnInfo(name = "is_active") var isActive : Boolean = true
)
