package com.je.playground.feature.tasks.domain

import com.je.playground.database.tasks.entity.SubTask
import com.je.playground.database.tasks.repository.SubTaskRepository
import com.je.playground.database.tasks.repository.TaskRepository
import javax.inject.Inject

class ToggleSubTaskIsCompletedUseCase @Inject constructor(
    private val subTaskRepository : SubTaskRepository,
    private val taskRepository : TaskRepository
) {
    suspend operator fun invoke(subTask : SubTask)  {
        subTaskRepository.insert(subTask.copy(isCompleted = !subTask.isCompleted))
        taskRepository.toggleCompletionBasedOnSubTasksCompletion(subTask.taskId)
    }
}