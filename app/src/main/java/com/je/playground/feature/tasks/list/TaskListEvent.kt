package com.je.playground.feature.tasks.list

import com.je.playground.database.tasks.entity.SubTask
import com.je.playground.database.tasks.entity.Task
import com.je.playground.database.tasks.entity.TaskWithSubTasks

sealed interface TaskListEvent {
    data class Delete(val taskWithSubTasks: TaskWithSubTasks) : TaskListEvent
    data class ToggleTaskCompletion(val task: Task) : TaskListEvent
    data class ToggleSubTaskCompletion(val subTask: SubTask) : TaskListEvent
    data class ToggleTaskArchived(val task: Task) : TaskListEvent
}
