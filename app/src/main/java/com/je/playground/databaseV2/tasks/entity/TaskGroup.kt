package com.je.playground.databaseV2.tasks.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task_group")
data class TaskGroup(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "task_group_id") var taskGroupId : Long = 0,
    @ColumnInfo(name = "title") var title : String,
    @ColumnInfo(name = "type") var type : Int,
    @ColumnInfo(name = "priority") var priority : Int = -1,
    @ColumnInfo(name = "note") var note : String? = null,
    @ColumnInfo(name = "is_completed") var isCompleted : Boolean = false,
    @ColumnInfo(name = "is_archived") var isArchived : Boolean = false
)