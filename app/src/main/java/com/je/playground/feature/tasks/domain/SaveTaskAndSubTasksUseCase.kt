package com.je.playground.feature.tasks.domain

import com.je.playground.database.tasks.entity.InvalidTaskException
import com.je.playground.database.tasks.entity.SubTask
import com.je.playground.database.tasks.entity.Task
import com.je.playground.database.tasks.repository.SubTaskRepository
import com.je.playground.database.tasks.repository.TaskRepository
import com.je.playground.notification.NotificationItem
import com.je.playground.notification.NotificationScheduler
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

class SaveTaskAndSubTasksUseCase @Inject constructor(
    private val taskRepository: TaskRepository,
    private val notificationScheduler: NotificationScheduler
) {
    suspend operator fun invoke(task: Task, subTasks: List<SubTask>, removedSubTasks: List<Long>) {
        if (task.title.isBlank()) throw InvalidTaskException("A task must have a title.")

        //Set task to completed if all its subtasks are completed.
        task.isCompleted = subTasks.isNotEmpty() && subTasks.all { it.isCompleted }

        //Insert task and connect to subtasks.
        task.mainTaskId = taskRepository.insertTask(task)
        subTasks.forEach { subTask ->
            subTask.mainTaskId = task.mainTaskId
            subTask.subTaskId = taskRepository.insertSubTask(subTask)
        }

        scheduleTaskNotification(task)
        scheduleSubTasksNotification(subTasks)

        removedSubTasks.forEach {
            notificationScheduler.cancelNotification(it.toInt() * 1009)
        }
    }

    private fun validateTask(){

    }

    /**
     * Schedules a notification for a [task] if it has a start date, otherwise tries to cancel
     * an existing scheduled notification.
     */
    private fun scheduleTaskNotification(task: Task) {
        if (task.startDate != null) {
            val startTime = task.startTime ?: LocalTime.MIDNIGHT
            notificationScheduler.scheduleNotification(
                NotificationItem(
                    id = task.mainTaskId.toInt(),
                    title = task.title,
                    message = task.note,
                    dateTime = LocalDateTime.of(
                        task.startDate,
                        startTime
                    )
                )
            )
        } else {
            notificationScheduler.cancelNotification(task.mainTaskId.toInt())
        }
    }

    /**
     * Schedules a notification for each subtask with a start date, and tries to cancel an existing
     * notification for each subtask without a start date.
     */
    private fun scheduleSubTasksNotification(subTasks: List<SubTask>) = subTasks.forEach { subTask ->
        if (subTask.startDate != null) {
            val startTime = subTask.startTime ?: LocalTime.MIDNIGHT
            notificationScheduler.scheduleNotification(
                NotificationItem(
                    id = subTask.subTaskId.toInt() * 1009,
                    title = subTask.title,
                    message = subTask.note,
                    dateTime = LocalDateTime.of(
                        subTask.startDate,
                        startTime
                    )
                )
            )
        } else {
            notificationScheduler.cancelNotification(subTask.subTaskId.toInt() * 1009)
        }
    }
}