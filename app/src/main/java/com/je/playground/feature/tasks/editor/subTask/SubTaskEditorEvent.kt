package com.je.playground.feature.tasks.editor.subTask

import com.je.playground.feature.utility.Result
import java.time.LocalDate
import java.time.LocalTime


sealed interface SubTaskEditorEvent {
    data class Update(val fields: List<SubTaskField>) : SubTaskEditorEvent
    data object Save : SubTaskEditorEvent

    companion object {
        fun updateSubTask(
            onEvent: (SubTaskEditorEvent) -> Unit,
            fields: List<SubTaskField>
        ) = onEvent(Update(fields))
    }
}

fun SubTaskEditorEvent.Update.using(onEvent: (SubTaskEditorEvent) -> Unit) = onEvent(this)

sealed interface SubTaskField {
    data class Title(val title: String) : SubTaskField
    data class Note(val note: String?) : SubTaskField
    data class StartDate(val startDate: LocalDate?) : SubTaskField
    data class EndDate(val endDate: LocalDate?) : SubTaskField
    data class StartTime(val startTime: LocalTime?) : SubTaskField
    data class EndTime(val endTime: LocalTime?) : SubTaskField
    data class Completed(val completed: Boolean) : SubTaskField
}

fun SubTaskField.update(onEvent: (SubTaskEditorEvent) -> Unit) =
    SubTaskEditorEvent.updateSubTask(onEvent, listOf(this))
