package com.je.playground.feature.tasks.domain

import com.je.playground.database.tasks.entity.Task
import com.je.playground.database.tasks.repository.TaskRepository
import kotlinx.coroutines.flow.Flow

class GetTaskUseCase(
    private val taskRepository : TaskRepository
) {
    operator fun invoke(id : Long) : Flow<Task> = taskRepository.getTaskById(id)
}