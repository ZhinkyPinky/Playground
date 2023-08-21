package com.je.playground.database.tasks.entity

import androidx.room.Embedded
import androidx.room.Relation

class TaskGroupWithTasks(
    @Embedded var taskGroup : TaskGroup,

    @Relation(
        entity = Task::class,
        parentColumn = "task_group_id",
        entityColumn = "task_group_id"
    )
    var tasks : List<Task> = emptyList()
)