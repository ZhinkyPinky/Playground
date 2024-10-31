package com.je.playground.database.tasks.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime


@Parcelize
@Entity(
    tableName = "sub_task",
    foreignKeys = [
        ForeignKey(
            entity = Task::class,
            parentColumns = ["task_id"],
            childColumns = ["task_id"],
            onDelete = 5
        )
    ],
    indices = [
        Index(value = ["task_id"])
    ]
)
data class SubTask(
    @ColumnInfo(name = "task_id") var taskId: Long,
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "sub_task_id") var subTaskId: Long = 0L,
    @ColumnInfo(name = "title") var title: String = "",
    @ColumnInfo(name = "note") var note: String? = null,
    @ColumnInfo(name = "start_date") var startDate: LocalDate? = null,
    @ColumnInfo(name = "start_time") var startTime: LocalTime? = null,
    @ColumnInfo(name = "end_date") var endDate: LocalDate? = null,
    @ColumnInfo(name = "end_time") var endTime: LocalTime? = null,
    @ColumnInfo(name = "is_completed") var isCompleted: Boolean = false,
    @ColumnInfo(name = "completion_date_time") var completionDateTime: LocalDateTime? = null,
    @ColumnInfo(name = "last_edit_date_time") var lastEditDateTime: LocalDateTime = LocalDateTime.now(),
    @ColumnInfo(name = "creation_date_time") var creationDateTime: LocalDateTime = LocalDateTime.now()
) : Parcelable

class InvalidSubTaskException(message: String) : Exception(message)