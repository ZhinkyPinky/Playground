package com.je.playground.feature.tasks.editor.overview.domain

import com.je.playground.database.tasks.entity.SubTask
import com.je.playground.database.tasks.repository.SubTaskRepository

class DeleteSubTaskUseCase(
    private val subTaskRepository: SubTaskRepository
) {
    suspend operator fun invoke(subTask: SubTask) : Int = subTaskRepository.delete(subTask)
}