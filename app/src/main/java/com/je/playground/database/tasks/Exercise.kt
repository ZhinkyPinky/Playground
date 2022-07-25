package com.je.playground.database.tasks

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Duration

@Entity(tableName = "exercise")
data class Exercise(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "exercise_id") val id : Int = 0,
    @NonNull @ColumnInfo(name = "name") val name : String,
    @ColumnInfo(name = "sets") val sets : Int = 0,
    @ColumnInfo(name = "repetitions") val reps : Int = 0,
    @ColumnInfo(name = "rest_time") val rest : Duration = Duration.ZERO
)
