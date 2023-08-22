package com.je.playground.database.tasks.entity

import androidx.room.Embedded
import androidx.room.Relation

class MainTaskWithSubTasks(
    @Embedded var mainTask : MainTask,

    @Relation(
        entity = SubTask::class,
        parentColumn = "main_task_id",
        entityColumn = "main_task_id"
    )
    var subTasks : List<SubTask> = emptyList()
)