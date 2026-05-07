package com.ecocraft

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.ecocraft.ui.EcoNavHost
import com.ecocraft.ui.components.AchievementToast
import com.ecocraft.ui.theme.EcoQuestTheme
import com.ecocraft.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EcoQuestTheme {
                EcoQuestApp()
            }
        }
    }
}

@Composable
fun EcoQuestApp() {
    val viewModel: MainViewModel = viewModel()
    val navController = rememberNavController()

    val userState by viewModel.userState.collectAsState()
    val newAchievement by viewModel.newAchievement.collectAsState()
    val lastEarnedPoints by viewModel.lastEarnedPoints.collectAsState()

    LaunchedEffect(newAchievement) {
        if (newAchievement != null) {
            kotlinx.coroutines.delay(3000)
            viewModel.clearAchievementToast()
        }
    }
    LaunchedEffect(lastEarnedPoints) {
        if (lastEarnedPoints != null) {
            kotlinx.coroutines.delay(1500)
            viewModel.clearEarnedPoints()
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            EcoNavHost(
                navController = navController,
                userState = userState,
                lastEarnedPoints = lastEarnedPoints,
                onCompleteTask = { viewModel.completeTask(it) },
                onUpdateUsername = { viewModel.updateUsername(it) },
                onUpdateAvatar = { viewModel.updateAvatar(it) },
                modifier = Modifier.fillMaxSize().padding(innerPadding)
            )
            AchievementToast(
                achievement = newAchievement,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp)
            )
        }
    }
}
