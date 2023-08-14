package com.je.playground.databaseV2.tasks.entity

import androidx.room.Embedded
import androidx.room.Relation

class TaskGroupWithTasks(
    @Embedded var taskGroup : TaskGroup,

    @Relation(
        entity = TaskV2::class,
        parentColumn = "task_group_id",
        entityColumn = "task_group_id"
    )
    var tasks : MutableList<TaskV2>
)