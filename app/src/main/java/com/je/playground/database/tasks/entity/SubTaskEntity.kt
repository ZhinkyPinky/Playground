package com.je.playground.database.tasks.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import java.time.LocalTime


@Parcelize
@Entity(
    tableName = "sub_task",
    foreignKeys = [
        ForeignKey(
            entity = Task::class,
            parentColumns = ["main_task_id"],
            childColumns = ["main_task_id"],
            onDelete = 5
        )
    ],
    indices = [
        Index(value = ["main_task_id"])
    ]
)
data class SubTask(
    @ColumnInfo(name = "main_task_id") var mainTaskId : Long = -1L,
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "sub_task_id") var subTaskId : Long = 0,
    @ColumnInfo(name = "title") var title : String = "",
    @ColumnInfo(name = "note") var note : String = "",
    @ColumnInfo(name = "start_date") var startDate : LocalDate? = null,
    @ColumnInfo(name = "start_time") var startTime : LocalTime? = null,
    @ColumnInfo(name = "end_date") var endDate : LocalDate? = null,
    @ColumnInfo(name = "end_time") var endTime : LocalTime? = null,
    @ColumnInfo(name = "is_completed") var isCompleted : Boolean = false,
) : Parcelable

class InvalidSubTaskException(message : String) : Exception(message)