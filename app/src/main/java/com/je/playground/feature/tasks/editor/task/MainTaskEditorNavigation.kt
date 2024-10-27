import android.util.Log
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.je.playground.feature.tasks.editor.TASK_EDITOR_ROUTE
import com.je.playground.feature.tasks.editor.TaskEditorOverview
import com.je.playground.feature.tasks.editor.task.MainTaskEditorScreen
import com.je.playground.navigation.TaskEditorRoute
import com.je.playground.navigation.sharedViewModel
import kotlinx.serialization.Serializable


fun NavController.navigateToMainTaskEditor(
    taskId: Long,
) = navigate(
    route = "tasks/$taskId/mainTask/edit"
)

@Serializable
object TaskEditor


fun NavGraphBuilder.mainTaskEditorScreen(
    navController: NavController,
) {
    composable<TaskEditor> { backStackEntry ->
        val parentEntry =
            remember(backStackEntry) { navController.getBackStackEntry(TaskEditorRoute) }
        MainTaskEditorScreen(
            viewModel = hiltViewModel(parentEntry),
            //navBackStackEntry.sharedViewModel(navController, TASK_EDITOR_ROUTE),
            onBackClick = navController::navigateUp
        )
    }
}
