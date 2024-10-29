package com.je.playground.database.tasks.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime


@Entity(tableName = "task")
data class Task(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "task_id") var taskId: Long = 0,
    @ColumnInfo(name = "title") var title: String = "",
    @ColumnInfo(name = "note") var note: String? = null,
    @ColumnInfo(name = "priority") var priority: Int = 0,
    @ColumnInfo(name = "is_completed") var isCompleted: Boolean = false,
    @ColumnInfo(name = "completion_date_time") var completionDateTime: LocalDateTime? = null,
    @ColumnInfo(name = "is_archived") var isArchived: Boolean = false,
    @ColumnInfo(name = "archived_date_time") var archivedDateTime: LocalDateTime? = null,
    @ColumnInfo(name = "start_date") var startDate: LocalDate? = null,
    @ColumnInfo(name = "start_time") var startTime: LocalTime? = null,
    @ColumnInfo(name = "end_date") var endDate: LocalDate? = null,
    @ColumnInfo(name = "end_time") var endTime: LocalTime? = null,
    @ColumnInfo(name = "last_edit_date_time") var lastEditDateTime: LocalDateTime = LocalDateTime.now(),
    @ColumnInfo(name = "creation_date_time") var creationDateTime: LocalDateTime = LocalDateTime.now()
)

class InvalidTaskException(message: String) : Exception(message)