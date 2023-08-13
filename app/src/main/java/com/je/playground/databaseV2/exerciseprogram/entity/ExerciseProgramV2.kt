package com.je.playground.databaseV2.exerciseprogram.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "exercise_program_v2",
)
data class ExerciseProgramV2(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "exercise_program_v2_id") val id : Long,
    @ColumnInfo(name = "name") var name : String,
    @ColumnInfo(name = "is_active") var isActive : Boolean = true
)
