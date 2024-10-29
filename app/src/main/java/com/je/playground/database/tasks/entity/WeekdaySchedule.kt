package com.je.playground.database.tasks.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import java.time.DayOfWeek

@Entity(
    tableName = "weekday_schedule",
    primaryKeys = ["task_id", "weekday"],
    foreignKeys = [
        ForeignKey(
            entity = Task::class,
            parentColumns = ["task_id"],
            childColumns = ["task_id"],
            onDelete = ForeignKey.CASCADE
        )]
)
data class WeekdaySchedule(
    @ColumnInfo(name = "task_id") val id: Long,
    @ColumnInfo(name = "weekday") val weekday: DayOfWeek
)
