package com.ecocraft.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ecocraft.data.EcoData
import com.ecocraft.data.UserRepository
import com.ecocraft.data.UserState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class NewAchievement(val icon: String, val title: String, val description: String)

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = UserRepository(application)

    private val _userState = MutableStateFlow(UserState())
    val userState: StateFlow<UserState> = _userState.asStateFlow()

    private val _newAchievement = MutableStateFlow<NewAchievement?>(null)
    val newAchievement: StateFlow<NewAchievement?> = _newAchievement.asStateFlow()

    private val _lastEarnedPoints = MutableStateFlow<Int?>(null)
    val lastEarnedPoints: StateFlow<Int?> = _lastEarnedPoints.asStateFlow()

    init {
        viewModelScope.launch {
            repo.userStateFlow.collect { state ->
                _userState.value = state
            }
        }
    }

    fun completeTask(taskId: String) {
        val task = EcoData.tasks.find { it.id == taskId } ?: return
        val current = _userState.value
        val newCount = (current.taskCounts[taskId] ?: 0) + 1
        val newPoints = current.points + task.pointsPerCompletion
        val newCounts = current.taskCounts + (taskId to newCount)

        val newlyUnlocked = task.achievements.filter { ach ->
            newCount == ach.targetCount && !current.unlockedAchievementIds.contains(ach.id)
        }
        val newUnlockedIds = current.unlockedAchievementIds + newlyUnlocked.map { it.id }

        val newState = current.copy(
            points = newPoints,
            taskCounts = newCounts,
            unlockedAchievementIds = newUnlockedIds
        )
        _userState.value = newState
        _lastEarnedPoints.value = task.pointsPerCompletion
        repo.saveUserState(newState)

        if (newlyUnlocked.isNotEmpty()) {
            val ach = newlyUnlocked.first()
            viewModelScope.launch {
                kotlinx.coroutines.delay(600)
                _newAchievement.value = NewAchievement(ach.icon, ach.title, ach.description)
            }
        }
    }

    fun clearAchievementToast() { _newAchievement.value = null }
    fun clearEarnedPoints() { _lastEarnedPoints.value = null }

    fun updateUsername(name: String) {
        val newState = _userState.value.copy(username = name.trim().ifEmpty { "Эко-путник" })
        _userState.value = newState
        repo.saveUserState(newState)
    }

    fun updateAvatar(index: Int) {
        val newState = _userState.value.copy(avatarIndex = index)
        _userState.value = newState
        repo.saveUserState(newState)
    }
}
