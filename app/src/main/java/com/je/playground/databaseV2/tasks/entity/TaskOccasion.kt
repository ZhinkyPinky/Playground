package com.je.playground.databaseV2.tasks.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalTime

@Entity(
    tableName = "task_occasion",
    foreignKeys = [
        ForeignKey(
            entity = Task::class,
            parentColumns = ["task_id"],
            childColumns = ["task_id"],
            onDelete = ForeignKey.CASCADE
        )]
)
data class TaskOccasion(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "task_occasion_id") val id : Long = 0,
    @ColumnInfo(name = "task_id", index = true) val taskId : Long,
    @ColumnInfo(name = "date_from") val dateFrom : LocalDate? = null,
    @ColumnInfo(name = "time_from") val timeFrom : LocalTime? = null,
    @ColumnInfo(name = "date_to") val dateTo : LocalDate? = null,
    @ColumnInfo(name = "time_to") val timeTo : LocalTime? = null,
    @ColumnInfo(name = "is_completed") var isCompleted : Boolean = false
)
