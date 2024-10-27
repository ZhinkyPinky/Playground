import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.je.playground.feature.tasks.editor.TASK_EDITOR_ROUTE
import com.je.playground.feature.tasks.editor.TaskEditorOverview
import com.je.playground.feature.tasks.editor.subTask.SubTaskEditorScreen
import com.je.playground.navigation.sharedViewModel


fun NavController.navigateToSubTaskEditor(
    taskId: Long,
    index: Int
) = navigate(
    route = "tasks/$taskId/subTasks/$index/edit"
)

fun NavGraphBuilder.subTaskEditorScreen(
    navController: NavController,
) {
    composable(
        route = "tasks/{taskId}/subTasks/{index}/edit",
        arguments = listOf(
            navArgument("taskId") { type = NavType.LongType },
            navArgument("index") { type = NavType.IntType })
    ) { navBackStackEntry ->

        SubTaskEditorScreen(
            viewModel = navBackStackEntry.sharedViewModel(navController, TaskEditorOverview::class),
            onBackClick = navController::navigateUp
        )
    }
}
