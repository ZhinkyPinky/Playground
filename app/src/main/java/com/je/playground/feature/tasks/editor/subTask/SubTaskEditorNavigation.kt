import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.je.playground.feature.tasks.editor.subTask.SubTaskEditorScreen
import com.je.playground.navigation.TaskEditorRoute
import kotlinx.serialization.Serializable


@Serializable
data class SubTaskEditor(val index: Int)

fun NavController.navigateToSubTaskEditor(index: Int) =
    navigate(route = SubTaskEditor(index))

fun NavGraphBuilder.subTaskEditorScreen(
    navController: NavController,
) {
    composable<SubTaskEditor> { backStackEntry ->
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry(TaskEditorRoute)
        }

        val subTaskEditor: SubTaskEditor = backStackEntry.toRoute()

        SubTaskEditorScreen(
            viewModel = hiltViewModel(parentEntry),
            subTaskIndex = subTaskEditor.index,
            onBackClick = navController::navigateUp
        )
    }
}
