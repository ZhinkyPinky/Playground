package com.je.playground.database.tasks.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import java.time.DayOfWeek

@Entity(
    tableName = "weekday_schedule",
    primaryKeys = ["main_task_id", "weekday"],
    foreignKeys = [
        ForeignKey(
            entity = MainTask::class,
            parentColumns = ["main_task_id"],
            childColumns = ["main_task_id"],
            onDelete = ForeignKey.CASCADE
        )]
)
data class WeekdaySchedule(
    @ColumnInfo(name = "main_task_id") val id : Long,
    @ColumnInfo(name = "weekday") val weekday : DayOfWeek
)
