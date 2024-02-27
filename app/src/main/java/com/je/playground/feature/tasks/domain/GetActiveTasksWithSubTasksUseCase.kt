package com.je.playground.feature.tasks.domain

import com.je.playground.database.tasks.entity.TaskWithSubTasks
import com.je.playground.database.tasks.repository.TaskWithSubTasksRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetActiveTasksWithSubTasksUseCase @Inject constructor (
    private val tasksWithSubTasksRepository : TaskWithSubTasksRepository
) {
    operator fun invoke() : Flow<List<TaskWithSubTasks>> = tasksWithSubTasksRepository.getAllActive()
}