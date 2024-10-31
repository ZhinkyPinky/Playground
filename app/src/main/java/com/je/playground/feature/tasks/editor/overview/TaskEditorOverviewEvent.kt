package com.je.playground.feature.tasks.editor.overview

import com.je.playground.database.tasks.entity.SubTask

sealed interface TaskEditorOverviewEvent {
    data class RemoveSubTask(val subTask: SubTask) : TaskEditorOverviewEvent
    data class UndoRemoveSubTask(val subTask: SubTask) : TaskEditorOverviewEvent
}