package com.je.playground.database.tasks.entity

import androidx.room.Embedded
import androidx.room.Relation

class TaskWithSubTasks(
    @Embedded var task : Task = Task(),

    @Relation(
        entity = SubTask::class,
        parentColumn = "task_id",
        entityColumn = "task_id"
    )
    var subTasks : List<SubTask> = emptyList()
)