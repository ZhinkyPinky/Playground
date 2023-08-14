package com.je.playground.databaseV2.tasks.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalTime

@Entity(
    tableName = "task_v2",
    foreignKeys = [
        ForeignKey(
            entity = TaskGroup::class,
            parentColumns = ["task_group_id"],
            childColumns = ["task_group_id"]
        )
    ]
)
data class TaskV2(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "task_v2_id") var taskId : Long = 0,
    @ColumnInfo(name = "task_group_id") var taskGroupId : Long,
    @ColumnInfo(name = "title") var title : String,
    //@ColumnInfo(name = "priority") var priority : Int = -1
    @ColumnInfo(name = "note") var note : String? = null,
    @ColumnInfo(name = "date_from") val dateFrom : LocalDate? = null,
    @ColumnInfo(name = "time_from") val timeFrom : LocalTime? = null,
    @ColumnInfo(name = "date_to") val dateTo : LocalDate? = null,
    @ColumnInfo(name = "time_to") val timeTo : LocalTime? = null,
    @ColumnInfo(name = "is_completed") var isCompleted : Boolean = false,
)