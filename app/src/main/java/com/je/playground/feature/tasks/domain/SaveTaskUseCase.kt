package com.je.playground.feature.tasks.domain

import com.je.playground.database.tasks.entity.Task
import com.je.playground.database.tasks.repository.TaskRepository

class SaveTaskUseCase(
    private val taskRepository : TaskRepository
) {
    suspend operator fun invoke(task : Task) : Long = taskRepository.insertTask(task)
}