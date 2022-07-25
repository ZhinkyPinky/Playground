package com.je.playground.database.tasks

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "simple_task")
data class SimpleTask(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "simple_task_id") val id : Int = 0,
    @NonNull @ColumnInfo(name = "name") val name : String,
    @ColumnInfo(name = "date_from") val dateFrom : LocalDateTime? = null,
    @ColumnInfo(name = "date_to") val dateTo : LocalDateTime? = null,
    @NonNull @ColumnInfo(name = "priority") val priority : Int = -1,
    @NonNull @ColumnInfo(name = "note") val note : String = ""
)
