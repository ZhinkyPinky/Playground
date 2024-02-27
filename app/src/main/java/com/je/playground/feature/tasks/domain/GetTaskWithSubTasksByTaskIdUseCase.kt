package com.je.playground.feature.tasks.domain

import com.je.playground.database.tasks.entity.TaskWithSubTasks
import com.je.playground.database.tasks.repository.TaskWithSubTasksRepository

class GetTaskWithSubTasksByTaskIdUseCase(
    private val taskWithSubTasksRepository : TaskWithSubTasksRepository,
) {
    operator fun invoke(taskId : Long) : TaskWithSubTasks? = taskWithSubTasksRepository.getByTaskId(taskId = taskId)
}