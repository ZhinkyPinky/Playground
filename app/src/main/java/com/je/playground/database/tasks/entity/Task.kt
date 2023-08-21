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
    tableName = "task",
    foreignKeys = [
        ForeignKey(
            entity = TaskGroup::class,
            parentColumns = ["task_group_id"],
            childColumns = ["task_group_id"]
        )
    ],
    indices = [
        Index(value = ["task_group_id"])
    ]
)
data class Task(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "task_id") var taskId : Long = 0,
    @ColumnInfo(name = "task_group_id") var taskGroupId : Long = -1L,
    @ColumnInfo(name = "title") var title : String = "",
    @ColumnInfo(name = "note") var note : String = "",
    @ColumnInfo(name = "date_from") var startDate : LocalDate? = null,
    @ColumnInfo(name = "time_from") var startTime : LocalTime? = null,
    @ColumnInfo(name = "date_to") var endDate : LocalDate? = null,
    @ColumnInfo(name = "time_to") var endTime : LocalTime? = null,
    @ColumnInfo(name = "is_completed") var isCompleted : Boolean = false,
) : Parcelable