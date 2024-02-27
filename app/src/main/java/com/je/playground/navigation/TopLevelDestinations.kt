package com.je.playground.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import com.je.playground.R
import com.je.playground.designsystem.icon.PlaygroundIcons

enum class TopLevelDestinations(
    val selectedIcon : ImageVector,
    val unselectedIcon : ImageVector,
    val iconTextId : Int,
    val titleTextId : Int,
) {
    TASK_LIST(
        selectedIcon = PlaygroundIcons.TaskList,
        unselectedIcon = PlaygroundIcons.TaskListUnselected,
        iconTextId = R.string.feature_task_list_icon,
        titleTextId = R.string.feature_task_list_title
    )
}