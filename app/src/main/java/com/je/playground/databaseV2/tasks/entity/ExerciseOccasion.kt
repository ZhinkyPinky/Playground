package com.je.playground.databaseV2.tasks.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "exercise_occasion",
    foreignKeys = [
        ForeignKey(
            entity = TaskOccasion::class,
            parentColumns = ["task_occasion_id"],
            childColumns = ["task_occasion_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Exercise::class,
            parentColumns = ["exercise_id"],
            childColumns = ["exercise_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ExerciseOccasion(
    @NonNull @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "task_occasion_id") val taskOccasionId : Long,
    @NonNull @ColumnInfo(name = "exercise_id") val exerciseId : Long,
    @NonNull @ColumnInfo(name = "is_completed") var isCompleted : Boolean = false
)
