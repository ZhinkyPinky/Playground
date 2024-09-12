package com.je.playground.feature.tasks.domain

import com.je.playground.database.tasks.entity.Task
import com.je.playground.database.tasks.repository.TaskRepository
import com.je.playground.notification.NotificationScheduler
import javax.inject.Inject

class ToggleTaskIsArchivedUseCase @Inject constructor(
    private val taskRepository : TaskRepository,
    private val notificationScheduler : NotificationScheduler
){
    operator fun invoke(task: Task){
        if (!task.isArchived) {
            notificationScheduler.cancelNotification(task.mainTaskId.toInt())
        }

        taskRepository.updateMainTask(task = task.copy(isArchived = !task.isArchived))
    }
}