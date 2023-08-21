package com.je.playground.database.tasks.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.je.playground.ui.taskview.viewmodel.TaskTypeV2
import java.time.LocalDate
import java.time.LocalTime

@Entity(tableName = "task_group")
data class TaskGroup(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "task_group_id") var taskGroupId : Long = 0,
    @ColumnInfo(name = "title") var title : String = "",
    @ColumnInfo(name = "note") var note : String = "",
    @ColumnInfo(name = "type") var type : Int = TaskTypeV2.RegularTask.ordinal,
    @ColumnInfo(name = "priority") var priority : Int = -1,
    @ColumnInfo(name = "is_completed") var isCompleted : Boolean = false,
    @ColumnInfo(name = "is_archived") var isArchived : Boolean = false,
    @ColumnInfo(name = "date_from") var startDate : LocalDate? = null,
    @ColumnInfo(name = "time_from") var startTime : LocalTime? = null,
    @ColumnInfo(name = "date_to") var endDate : LocalDate? = null,
    @ColumnInfo(name = "time_to") var endTime : LocalTime? = null,
)