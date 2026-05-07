package com.ecocraft.data

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserRepository(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("ecoquest_prefs", Context.MODE_PRIVATE)

    private val _userStateFlow = MutableStateFlow(loadState())
    val userStateFlow: StateFlow<UserState> = _userStateFlow.asStateFlow()

    private fun loadState(): UserState {
        val taskCountsStr = prefs.getString("task_counts", "") ?: ""
        val taskCounts = mutableMapOf<String, Int>()
        if (taskCountsStr.isNotEmpty()) {
            taskCountsStr.split(",").forEach { entry ->
                val parts = entry.split(":")
                if (parts.size == 2) {
                    taskCounts[parts[0]] = parts[1].toIntOrNull() ?: 0
                }
            }
        }

        val achStr = prefs.getString("achievements", "") ?: ""
        val achievements = if (achStr.isNotEmpty()) achStr.split(",") else emptyList()

        return UserState(
            username = prefs.getString("username", "Эко-путник") ?: "Эко-путник",
            avatarIndex = prefs.getInt("avatar_index", 0),
            points = prefs.getInt("points", 0),
            taskCounts = taskCounts,
            unlockedAchievementIds = achievements
        )
    }

    fun saveUserState(state: UserState) {
        val taskCountsStr = state.taskCounts.entries.joinToString(",") { "${it.key}:${it.value}" }
        val achStr = state.unlockedAchievementIds.joinToString(",")

        prefs.edit()
            .putString("username", state.username)
            .putInt("avatar_index", state.avatarIndex)
            .putInt("points", state.points)
            .putString("task_counts", taskCountsStr)
            .putString("achievements", achStr)
            .apply()

        _userStateFlow.value = state
    }
}
