package com.je.playground.feature.tasks.domain

import android.util.Log
import com.je.playground.database.tasks.entity.Task
import com.je.playground.database.tasks.repository.TaskRepository
import com.je.playground.feature.utility.Result

class SaveTaskUseCase(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(task: Task): Pair<Result, Long> {
        //TODO: Validate

        var taskId = task.taskId

        Log.d("Save invoke before", taskId.toString())
        if (taskId == 0L) {
            taskId = taskRepository.insertTask(task)
        } else {
            taskRepository.updateTask(task)
        }
        Log.d("Save invoke after", taskId.toString())

        return Result.Success to taskId
    }
}