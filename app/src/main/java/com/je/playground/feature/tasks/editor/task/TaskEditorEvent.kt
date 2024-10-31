package com.je.playground.feature.tasks.editor.task

import java.time.LocalDate
import java.time.LocalTime

sealed interface TaskEditorEvent {
    data class UpdateTask(val fields: List<TaskField>) : TaskEditorEvent
    data object SaveTask : TaskEditorEvent

    companion object {
        fun updateTask(
            onEvent: (TaskEditorEvent) -> Unit,
            fields: List<TaskField>
        ): Unit = onEvent(UpdateTask(fields))
    }
}

fun TaskEditorEvent.UpdateTask.using(onEvent: (TaskEditorEvent) -> Unit) = onEvent(this)

sealed interface TaskField {
    data class Title(val title: String) : TaskField
    data class Note(val note: String?) : TaskField
    data class Priority(val priority: Int) : TaskField
    data class Completed(val completed: Boolean) : TaskField
    data class Archived(val archived: Boolean) : TaskField
    data class StartDate(val startDate: LocalDate?) : TaskField
    data class EndDate(val endDate: LocalDate?) : TaskField
    data class StartTime(val startTime: LocalTime?) : TaskField
    data class EndTime(val endTime: LocalTime?) : TaskField
}

fun TaskField.update(onEvent: (TaskEditorEvent) -> Unit) =
    TaskEditorEvent.updateTask(onEvent, listOf(this))
