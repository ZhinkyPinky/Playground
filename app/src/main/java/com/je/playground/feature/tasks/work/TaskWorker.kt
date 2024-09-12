package com.je.playground.feature.tasks.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.je.playground.database.tasks.entity.Task
import com.je.playground.database.tasks.repository.TaskRepository
import dagger.assisted.Assisted
import kotlinx.coroutines.runBlocking

@HiltWorker
class TaskWorker(
    @Assisted context : Context,
    @Assisted workerParameters : WorkerParameters,
    private val taskRepository : TaskRepository
) : Worker(
    context,
    workerParameters
) {
    override fun doWork() : Result {
        runBlocking {
            val task = Task()
            taskRepository.insertTask(task)
        }

        return Result.success()
    }
}