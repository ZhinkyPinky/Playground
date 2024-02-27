package com.je.playground.feature.tasks.domain

import com.je.playground.database.tasks.entity.TaskWithSubTasks
import com.je.playground.database.tasks.repository.TaskRepository

class ToggleTaskCompletionIfAllSubTasksAreCompletedUseCase(
    private val taskRepository : TaskRepository
) {
    suspend operator fun invoke(taskWithSubTasks : TaskWithSubTasks) : Long =
        taskRepository.insertTask(
            taskWithSubTasks.task.copy(
                isCompleted = taskWithSubTasks.subTasks.isNotEmpty() && taskWithSubTasks.subTasks.all { it.isCompleted })
        )
}