package com.je.playground.databaseV2.tasks.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(
    tableName = "simple_task",
    foreignKeys = [
        ForeignKey(
            entity = Task::class,
            parentColumns = ["task_id"],
            childColumns = ["simple_task_id"],
            onDelete = ForeignKey.CASCADE
        )]
)
data class SimpleTask(
    @NonNull @PrimaryKey @ColumnInfo(name = "simple_task_id") val id : Long,
    @NonNull @ColumnInfo(name = "name") var name : String,
    @NonNull @ColumnInfo(name = "priority") var priority : Int = -1,
    @ColumnInfo(name = "note") var note : String? = null
)
