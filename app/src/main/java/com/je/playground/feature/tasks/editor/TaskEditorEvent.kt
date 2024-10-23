package com.je.playground.feature.tasks.editor

import com.je.playground.database.tasks.entity.SubTask
import com.je.playground.database.tasks.entity.Task

sealed class TaskEditorEvent {
    data class SaveSubTask(val index : Int, val subTask : SubTask) : TaskEditorEvent()
    data class RemoveSubTask(val index : Int) : TaskEditorEvent()
    data object SaveTask : TaskEditorEvent()
    data class UpdateTask(val task : Task) : TaskEditorEvent()
    data object ToggleGroup : TaskEditorEvent()
}

