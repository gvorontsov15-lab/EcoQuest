package com.ecocraft.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ecocraft.data.UserState
import com.ecocraft.ui.screens.*
import com.ecocraft.viewmodel.NewAchievement

object Routes {
    const val HOME         = "home"
    const val TASKS        = "tasks"
    const val TASK_DETAIL  = "task/{taskId}"
    const val ACHIEVEMENTS = "achievements"
    const val PROFILE      = "profile"

    fun taskDetail(id: String) = "task/$id"
}

@Composable
fun EcoNavHost(
    navController: NavHostController,
    userState: UserState,
    lastEarnedPoints: Int?,
    onCompleteTask: (String) -> Unit,
    onUpdateUsername: (String) -> Unit,
    onUpdateAvatar: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Routes.HOME,
        modifier = modifier
    ) {
        composable(Routes.HOME) {
            HomeScreen(
                userState = userState,
                onNavigateTasks = { navController.navigate(Routes.TASKS) },
                onNavigateAchievements = { navController.navigate(Routes.ACHIEVEMENTS) },
                onNavigateProfile = { navController.navigate(Routes.PROFILE) }
            )
        }
        composable(Routes.TASKS) {
            TasksScreen(
                userState = userState,
                onNavigateBack = { navController.popBackStack() },
                onOpenTask = { navController.navigate(Routes.taskDetail(it)) }
            )
        }
        composable(
            route = Routes.TASK_DETAIL,
            arguments = listOf(navArgument("taskId") { type = NavType.StringType })
        ) { backStack ->
            val taskId = backStack.arguments?.getString("taskId") ?: return@composable
            TaskDetailScreen(
                taskId = taskId,
                userState = userState,
                lastEarnedPoints = lastEarnedPoints,
                onNavigateBack = { navController.popBackStack() },
                onComplete = { onCompleteTask(taskId) }
            )
        }
        composable(Routes.ACHIEVEMENTS) {
            AchievementsScreen(
                userState = userState,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Routes.PROFILE) {
            ProfileScreen(
                userState = userState,
                onNavigateBack = { navController.popBackStack() },
                onUpdateUsername = onUpdateUsername,
                onUpdateAvatar = onUpdateAvatar
            )
        }
    }
}
