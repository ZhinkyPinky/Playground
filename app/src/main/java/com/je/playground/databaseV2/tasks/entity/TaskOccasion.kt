package com.je.playground.databaseV2.tasks.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalTime

@Entity(tableName = "task_occasion")
data class TaskOccasion(
    @NonNull @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "task_occasion_id") val id : Long = 0,
    @NonNull @ColumnInfo(name = "task_id") val taskId : Long,
    @NonNull @ColumnInfo(name = "date_from") val dateFrom : LocalDate,
    @ColumnInfo(name = "time_from") val timeFrom : LocalTime? = null,
    @ColumnInfo(name = "date_to") val dateTo : LocalDate? = null,
    @ColumnInfo(name = "time_to") val timeTo : LocalTime? = null,
    @ColumnInfo(name = "is_completed") val isCompleted : Boolean = false
)
