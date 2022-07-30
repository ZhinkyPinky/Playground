package com.je.playground.databaseV2.tasks.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task")
data class Task(
    @NonNull @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "task_id") val id : Long = 0,
)