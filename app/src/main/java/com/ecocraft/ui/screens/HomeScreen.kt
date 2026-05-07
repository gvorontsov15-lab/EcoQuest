package com.ecocraft.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ecocraft.data.EcoData
import com.ecocraft.data.UserState
import com.ecocraft.ui.components.*
import com.ecocraft.ui.theme.*

@Composable
fun HomeScreen(
    userState: UserState,
    onNavigateTasks: () -> Unit,
    onNavigateAchievements: () -> Unit,
    onNavigateProfile: () -> Unit,
    modifier: Modifier = Modifier
) {
    val levelInfo = EcoData.getLevelInfo(userState.points)
    val level = levelInfo.current
    val nextLevel = levelInfo.next
    val progress = levelInfo.progress

    Box(modifier = modifier.fillMaxSize().background(GreenBackground)) {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {

            GradientHeader {
                Text(
                    "Добро пожаловать",
                    fontSize = 12.sp,
                    letterSpacing = 2.sp,
                    color = Color.White.copy(alpha = 0.7f)
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    userState.username,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    "${level.icon}  ${level.name}",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.85f)
                )

                Spacer(Modifier.height(20.dp))

                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.15f)
                    ),
                    elevation = CardDefaults.cardElevation(0.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "⭐ ${userState.points} баллов",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            if (nextLevel != null) {
                                Text(
                                    "до «${nextLevel.name}»: ${nextLevel.minPoints - userState.points}",
                                    color = Color.White.copy(alpha = 0.7f),
                                    fontSize = 12.sp
                                )
                            }
                        }
                        Spacer(Modifier.height(10.dp))
                        EcoProgressBar(progress = progress)
                    }
                }
            }

            Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 24.dp)) {
                SectionLabel("Навигация")
                Spacer(Modifier.height(14.dp))

                NavCard(
                    icon = "🗺️",
                    title = "Задания",
                    subtitle = "Выполняй и получай баллы",
                    onClick = onNavigateTasks
                )
                Spacer(Modifier.height(12.dp))
                NavCard(
                    icon = "🏅",
                    title = "Достижения",
                    subtitle = "Разблокировано: ${userState.unlockedAchievementIds.size} из ${EcoData.tasks.sumOf { it.achievements.size }}",
                    onClick = onNavigateAchievements
                )
                Spacer(Modifier.height(12.dp))
                NavCard(
                    icon = "👤",
                    title = "Профиль",
                    subtitle = "Аватар, имя, уровень",
                    onClick = onNavigateProfile
                )

                Spacer(Modifier.height(24.dp))
                SectionLabel("Моя статистика")
                Spacer(Modifier.height(14.dp))

                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = GreenMint),
                    elevation = CardDefaults.cardElevation(0.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(20.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        StatItem("🔋", "${userState.taskCounts["batteries"] ?: 0}", "Батарейки")
                        StatItem("💡", "${userState.taskCounts["light"] ?: 0}", "Свет выкл.")
                        StatItem("🏆", "${userState.unlockedAchievementIds.size}", "Ачивки")
                    }
                }

                Spacer(Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun StatItem(icon: String, value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(icon, fontSize = 26.sp)
        Text(value, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        Text(label, fontSize = 11.sp, color = TextMuted)
    }
}
