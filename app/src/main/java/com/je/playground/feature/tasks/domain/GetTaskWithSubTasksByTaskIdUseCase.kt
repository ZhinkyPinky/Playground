package com.je.playground.feature.tasks.domain

import com.je.playground.database.tasks.entity.Task
import com.je.playground.database.tasks.entity.TaskWithSubTasks
import com.je.playground.database.tasks.repository.TaskWithSubTasksRepository
import kotlinx.coroutines.flow.Flow

class GetTaskWithSubTasksByTaskIdUseCase(
    private val taskWithSubTasksRepository: TaskWithSubTasksRepository,
) {
    //TODO: Make flow.
     operator fun invoke(taskId: Long): Flow<TaskWithSubTasks> =
        taskWithSubTasksRepository.getByTaskId(taskId = taskId)
}
