package com.je.playground.feature.tasks.domain

import com.je.playground.database.tasks.entity.Task
import com.je.playground.database.tasks.repository.TaskRepository
import kotlinx.coroutines.flow.Flow

class GetTaskUseCase(
    private val taskRepository : TaskRepository
) {
    suspend operator fun invoke(id : Long) : Task = taskRepository.getTaskById(id) ?: Task()
}