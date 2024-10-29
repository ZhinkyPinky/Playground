package com.je.playground.feature.tasks.domain

import com.je.playground.database.tasks.entity.TaskWithSubTasks
import com.je.playground.database.tasks.repository.TaskRepository
import com.je.playground.notification.NotificationScheduler
import javax.inject.Inject

class DeleteTaskUseCase @Inject constructor(
    private val taskRepository : TaskRepository,
    private val notificationScheduler : NotificationScheduler
) {
    operator fun invoke(taskWithSubTasks : TaskWithSubTasks) {
        taskRepository
            .deleteTask(taskWithSubTasks.task)
            .invokeOnCompletion {
                notificationScheduler.cancelNotification(taskWithSubTasks.task.taskId.toInt())

                taskWithSubTasks.subTasks.forEach {
                    notificationScheduler.cancelNotification(it.subTaskId.toInt() * 1009)
                }
            }
    }
}