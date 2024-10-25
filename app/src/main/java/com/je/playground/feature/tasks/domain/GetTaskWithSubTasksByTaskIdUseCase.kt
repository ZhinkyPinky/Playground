package com.je.playground.feature.tasks.domain

import com.je.playground.database.tasks.entity.Task
import com.je.playground.database.tasks.entity.TaskWithSubTasks
import com.je.playground.database.tasks.repository.TaskWithSubTasksRepository

class GetTaskWithSubTasksByTaskIdUseCase(
    private val taskWithSubTasksRepository: TaskWithSubTasksRepository,
) {
    suspend operator fun invoke(taskId: Long): TaskWithSubTasks? =
        if (taskId == -1L) {
            TaskWithSubTasks()
        } else {
            taskWithSubTasksRepository.getByTaskId(taskId = taskId)
        }
}
