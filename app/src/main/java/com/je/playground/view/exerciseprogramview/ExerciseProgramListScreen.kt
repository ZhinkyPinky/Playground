package com.je.playground.view.exerciseprogramview

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.je.playground.database.exerciseprogram.entity.ExerciseProgramWithAllTheThings
import com.je.playground.view.exerciseprogramview.viewmodel.ExerciseProgramViewModel


@Composable
fun ExerciseProgramListScreen(
    exerciseProgramViewModel : ExerciseProgramViewModel,
    drawerState : DrawerState,
    navigateToExerciseProgramScreen : (Long) -> Unit,
    navigateToExerciseProgramEditScreen : (Long) -> Unit
) {
    val exerciseProgramUiState by exerciseProgramViewModel.exerciseProgramUiState.collectAsState()

    ExerciseProgramListScreenContent(
        exerciseProgramsWithAllTheThings = exerciseProgramUiState.exerciseProgramsWithAllTheThings,
        drawerState = drawerState,
        navigateToExerciseProgramScreen = navigateToExerciseProgramScreen,
        navigateToExerciseProgramEditScreen = navigateToExerciseProgramEditScreen
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseProgramListScreenContent(
    exerciseProgramsWithAllTheThings : List<ExerciseProgramWithAllTheThings>,
    drawerState : DrawerState,
    navigateToExerciseProgramScreen : (Long) -> Unit,
    navigateToExerciseProgramEditScreen : (Long) -> Unit,
) {
    var showInactive by rememberSaveable {
        mutableStateOf(false)
    }

    Scaffold(
        topBar = {
            val coroutineScope = rememberCoroutineScope()
            Column {
                TopAppBar(
                    title = {
                        Text(
                            text = "Exercise Programs",
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 22.sp,
                            maxLines = 1,
                            textAlign = TextAlign.Start,

                            modifier = Modifier
                                .padding(
                                    start = 0.dp,
                                    top = 4.dp,
                                    end = 8.dp,
                                    bottom = 4.dp
                                )
                                .weight(1f)
                        )
                    },
                    actions = {
                        IconButton(
                            onClick = { navigateToExerciseProgramEditScreen(-1L) }) {
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = "Add new task",
                                tint = MaterialTheme.colorScheme.onPrimary,
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                )

                Divider(
                    color = MaterialTheme.colorScheme.background
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(1.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            itemsIndexed(
                items = exerciseProgramsWithAllTheThings,
                key = { _, exerciseProgramWithAllTheThings -> exerciseProgramWithAllTheThings.exerciseProgram.exerciseProgramId }) { _, exerciseProgramWithAllTheThings ->
                if (exerciseProgramWithAllTheThings.exerciseProgram.isActive || showInactive) {
                    ExerciseProgramListItem(
                        exerciseProgramWithAllTheThings = exerciseProgramWithAllTheThings,
                        navigateToExerciseProgramScreen = {
                            navigateToExerciseProgramScreen(exerciseProgramWithAllTheThings.exerciseProgram.exerciseProgramId)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ExerciseProgramListItem(
    exerciseProgramWithAllTheThings : ExerciseProgramWithAllTheThings,
    navigateToExerciseProgramScreen : () -> Unit
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    var isDropDownMenuExpanded by rememberSaveable { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .background(
                color = if (isExpanded) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.primaryContainer
            )
            .wrapContentHeight()
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )
            .clickable { isExpanded = !isExpanded }
    ) {
        Column(
            modifier = Modifier.padding(
                start = 6.dp,
                top = 6.dp,
                bottom = 6.dp
            ).fillMaxSize()
        ) {
            Log.d("ExerciseProgramColumn", exerciseProgramWithAllTheThings.exerciseProgram.title)
            Row(
                modifier = Modifier
                    .height(IntrinsicSize.Max)
            ) {
                Text(
                    text = exerciseProgramWithAllTheThings.exerciseProgram.title
                )
            }
        }
    }
}
