package com.je.playground.feature.tasks.editor

import com.je.playground.feature.tasks.editor.TaskField.Archived
import com.je.playground.feature.tasks.editor.TaskField.EndDate
import com.je.playground.feature.tasks.editor.TaskField.EndTime
import com.je.playground.feature.tasks.editor.TaskField.Priority
import com.je.playground.feature.tasks.editor.TaskField.StartDate
import com.je.playground.feature.tasks.editor.TaskField.StartTime
import java.time.LocalDate
import java.time.LocalTime

sealed interface TaskEditorEvent {
    data class UpdateTask(val fields: List<TaskField>) : TaskEditorEvent
    data class UpdateSubTask(val index: Int, val fields: List<SubTaskField>) : TaskEditorEvent
    data class RemoveSubTask(val index: Int) : TaskEditorEvent
    data object ToggleGroup : TaskEditorEvent
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

    companion object {
        fun updateTitle(
            onEvent: (TaskEditorEvent) -> Unit,
            title: String
        ) = TaskEditorEvent.updateTask(onEvent, listOf(Title(title)))

        fun updateNote(
            onEvent: (TaskEditorEvent) -> Unit,
            note: String
        ) = TaskEditorEvent.updateTask(onEvent, listOf(Note(note)))

        fun updatePriority(
            onEvent: (TaskEditorEvent) -> Unit,
            priority: Int
        ) = TaskEditorEvent.updateTask(onEvent, listOf(Priority(priority)))

        fun updateCompleted(
            onEvent: (TaskEditorEvent) -> Unit,
            completed: Boolean
        ) = TaskEditorEvent.updateTask(onEvent, listOf(Completed(completed)))

        fun updateArchived(
            onEvent: (TaskEditorEvent) -> Unit,
            archived: Boolean
        ) = TaskEditorEvent.updateTask(onEvent, listOf(Archived(archived)))

        fun updateStartDate(
            onEvent: (TaskEditorEvent) -> Unit,
            startDate: LocalDate?
        ) = TaskEditorEvent.updateTask(onEvent, listOf(StartDate(startDate)))

        fun updateEndDate(
            onEvent: (TaskEditorEvent) -> Unit,
            endDate: LocalDate?
        ) = TaskEditorEvent.updateTask(onEvent, listOf(EndDate(endDate)))

        fun updateStartTime(
            onEvent: (TaskEditorEvent) -> Unit,
            startTime: LocalTime?
        ) = TaskEditorEvent.updateTask(onEvent, listOf(StartTime(startTime)))

        fun updateEndTime(
            onEvent: (TaskEditorEvent) -> Unit,
            endTime: LocalTime?
        ) = TaskEditorEvent.updateTask(onEvent, listOf(EndTime(endTime)))
    }
}

sealed interface SubTaskField {
    data class Title(val title: String) : SubTaskField
    data class Note(val note: String) : SubTaskField
    data class StartDate(val startDate: LocalDate?) : SubTaskField
    data class EndDate(val endDate: LocalDate?) : SubTaskField
    data class StartTime(val startTime: LocalTime?) : SubTaskField
    data class EndTime(val endTime: LocalTime?) : SubTaskField
    data class Completed(val completed: Boolean) : SubTaskField

    companion object {
        fun updateTitle(
            onEvent: (TaskEditorEvent) -> Unit,
            index: Int,
            title: String
        ) = TaskEditorEvent.updateSubTask(onEvent, index, listOf(Title(title)))

        fun updateNote(
            onEvent: (TaskEditorEvent) -> Unit,
            index: Int,
            note: String
        ) = TaskEditorEvent.updateSubTask(onEvent, index, listOf(Note(note)))

        fun updateStartDate(
            onEvent: (TaskEditorEvent) -> Unit,
            index: Int,
            startDate: LocalDate?
        ) = TaskEditorEvent.updateSubTask(onEvent, index, listOf(StartDate(startDate)))

        fun updateEndDate(
            onEvent: (TaskEditorEvent) -> Unit,
            index: Int,
            endDate: LocalDate?
        ) = TaskEditorEvent.updateSubTask(onEvent, index, listOf(EndDate(endDate)))

        fun updateStartTime(
            onEvent: (TaskEditorEvent) -> Unit,
            index: Int,
            startTime: LocalTime?
        ) = TaskEditorEvent.updateSubTask(onEvent, index, listOf(StartTime(startTime)))

        fun updateEndTime(
            onEvent: (TaskEditorEvent) -> Unit,
            index: Int,
            endTime: LocalTime?
        ) = TaskEditorEvent.updateSubTask(onEvent, index, listOf(EndTime(endTime)))

        fun updateComplete(
            onEvent: (TaskEditorEvent) -> Unit,
            index: Int,
            completed: Boolean
        ) = TaskEditorEvent.updateSubTask(onEvent, index, listOf(Completed(completed)))
    }
}