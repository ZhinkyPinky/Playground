package com.je.playground.databaseV2.tasks.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.DayOfWeek

@Entity(
    tableName = "weekday_schedule",
    primaryKeys = ["task_id", "weekday"]
)
data class WeekdaySchedule(
    @NonNull @ColumnInfo(name = "task_id") val id : Long,
    @NonNull @ColumnInfo(name = "weekday") val weekday : DayOfWeek
)
