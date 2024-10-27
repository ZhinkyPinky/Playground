import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.je.playground.feature.tasks.editor.TASK_EDITOR_ROUTE
import com.je.playground.feature.tasks.editor.task.MainTaskEditorScreen
import com.je.playground.navigation.sharedViewModel


fun NavController.navigateToMainTaskEditor(
    taskId: Long,
) = navigate(
    route = "tasks/$taskId/mainTask/edit"
)

fun NavGraphBuilder.mainTaskEditorScreen(
    navController: NavController,
) {
    composable(
        route = "tasks/{taskId}/mainTask/edit",
        arguments = listOf(navArgument("taskId") { type = NavType.LongType })
    ) { navBackStackEntry ->
        MainTaskEditorScreen(
            //viewModel = hiltViewModel(navController.getBackStackEntry("tasks/{taskId}/edit")),
            viewModel = navBackStackEntry.sharedViewModel(navController, TASK_EDITOR_ROUTE),
            onBackClick = navController::navigateUp
        )
    }
}
