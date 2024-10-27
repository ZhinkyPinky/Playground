package com.je.playground.feature.tasks.editor

import java.time.LocalDate
import java.time.LocalTime

sealed interface TaskEditorEvent {
    data class UpdateTask(val fields: List<TaskField>) : TaskEditorEvent
    data class UpdateSubTask(val index: Int, val fields: List<SubTaskField>) : TaskEditorEvent
    data class RemoveSubTask(val index: Int) : TaskEditorEvent
    data object Save : TaskEditorEvent

    companion object {
        fun updateTask(
            onEvent: (TaskEditorEvent) -> Unit,
            fields: List<TaskField>
        ) = onEvent(UpdateTask(fields))

        fun updateSubTask(
            onEvent: (TaskEditorEvent) -> Unit,
            index: Int,
            fields: List<SubTaskField>
        ) = onEvent(UpdateSubTask(index, fields))
    }
}

sealed interface TaskField {
    data class Title(val title: String) : TaskField
    data class Note(val note: String) : TaskField
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

sealed interface SubTaskField {
    data class Title(val title: String) : SubTaskField
    data class Note(val note: String) : SubTaskField
    data class StartDate(val startDate: LocalDate?) : SubTaskField
    data class EndDate(val endDate: LocalDate?) : SubTaskField
    data class StartTime(val startTime: LocalTime?) : SubTaskField
    data class EndTime(val endTime: LocalTime?) : SubTaskField
    data class Completed(val completed: Boolean) : SubTaskField
}

fun SubTaskField.update(onEvent: (TaskEditorEvent) -> Unit, index: Int) =
    TaskEditorEvent.updateSubTask(
        onEvent,
        index,
        listOf(this)
    )