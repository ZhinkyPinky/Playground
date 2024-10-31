package com.je.playground.feature.tasks.editor.subTask.domain

import com.je.playground.database.tasks.entity.SubTask
import com.je.playground.database.tasks.repository.SubTaskRepository

class GetSubTaskUseCase(
    private val subTaskRepository: SubTaskRepository
) {
    suspend operator fun invoke(subTaskId: Long): SubTask =
        subTaskRepository.getById(subTaskId = subTaskId) ?: SubTask(taskId = 0L)
}