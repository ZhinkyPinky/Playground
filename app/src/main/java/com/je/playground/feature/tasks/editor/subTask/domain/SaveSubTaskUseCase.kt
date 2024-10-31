package com.je.playground.feature.tasks.editor.subTask.domain

import com.je.playground.database.tasks.entity.SubTask
import com.je.playground.database.tasks.repository.SubTaskRepository
import com.je.playground.feature.utility.Result

class SaveSubTaskUseCase(
    private val subTaskRepository: SubTaskRepository
) {
    suspend operator fun invoke(subTask: SubTask): Result {
        //TODO: Validate subtask and return result.
        subTaskRepository.insert(subTask)

        return Result.Success
    }

}