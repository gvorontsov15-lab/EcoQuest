package com.ecocraft.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ecocraft.data.EcoData
import com.ecocraft.data.EcoTask
import com.ecocraft.data.UserState
import com.ecocraft.ui.components.*
import com.ecocraft.ui.theme.*

// ─── Tasks list ──────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(
    userState: UserState,
    onNavigateBack: () -> Unit,
    onOpenTask: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize().background(GreenBackground)) {

        GradientHeader {
            IconButton(
                onClick = onNavigateBack,
                modifier = Modifier.offset(x = (-12).dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White)
            }
            Text("Эко-задания", fontSize = 12.sp, letterSpacing = 2.sp, color = Color.White.copy(alpha = 0.7f))
            Text("Помоги планете", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            EcoData.tasks.forEach { task ->
                TaskCard(
                    task = task,
                    count = userState.taskCounts[task.id] ?: 0,
                    onClick = { onOpenTask(task.id) }
                )
            }

            // Coming soon card
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = GreenMint),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("🔜", fontSize = 30.sp)
                    Spacer(Modifier.height(8.dp))
                    Text("Скоро новые задания", fontWeight = FontWeight.Bold, color = TextSecondary)
                    Text("Сортировка мусора, велопрогулка и другие", fontSize = 12.sp, color = TextMuted)
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TaskCard(task: EcoTask, count: Int, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(22.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            // Coloured top part
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.linearGradient(listOf(GreenMedium, GreenAccent))
                    )
                    .padding(20.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(task.icon, fontSize = 42.sp)
                    Spacer(Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            task.subtitle.uppercase(),
                            fontSize = 10.sp,
                            letterSpacing = 2.sp,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                        Text(task.title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.2f))
                    ) {
                        Text(
                            "+${task.pointsPerCompletion}⭐",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
            // Light bottom strip
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(GreenSurface)
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Выполнено: $count раз", fontSize = 13.sp, color = TextSecondary)
                Text("Начать →", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = GreenAccent)
            }
        }
    }
}

// ─── Task detail ─────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailScreen(
    taskId: String,
    userState: UserState,
    lastEarnedPoints: Int?,
    onNavigateBack: () -> Unit,
    onComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val task = EcoData.tasks.find { it.id == taskId } ?: return
    val count = userState.taskCounts[taskId] ?: 0
    var justDone by remember { mutableStateOf(false) }

    LaunchedEffect(lastEarnedPoints) {
        if (lastEarnedPoints != null) {
            justDone = true
            kotlinx.coroutines.delay(2000)
            justDone = false
        }
    }

    Box(modifier = modifier.fillMaxSize().background(GreenBackground)) {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {

            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Brush.linearGradient(listOf(GreenDark, GreenAccent)))
                    .statusBarsPadding()
                    .padding(24.dp)
            ) {
                Column {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.offset(x = (-12).dp)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White)
                    }
                    Text(task.icon, fontSize = 56.sp)
                    Spacer(Modifier.height(8.dp))
                    Text(task.subtitle.uppercase(), fontSize = 11.sp, letterSpacing = 2.sp, color = Color.White.copy(alpha = 0.7f))
                    Text(task.title, fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(Modifier.height(12.dp))
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.15f))
                    ) {
                        Text(
                            "⭐ +${task.pointsPerCompletion} баллов за выполнение",
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                            color = Color.White,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            Column(modifier = Modifier.padding(24.dp)) {

                // Description
                SectionLabel("Что нужно сделать")
                Spacer(Modifier.height(12.dp))
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = GreenSurface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        task.description,
                        modifier = Modifier.padding(20.dp),
                        fontSize = 15.sp,
                        lineHeight = 24.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(Modifier.height(24.dp))

                // Achievements
                SectionLabel("Достижения задания")
                Spacer(Modifier.height(12.dp))
                task.achievements.forEach { ach ->
                    val unlocked = userState.unlockedAchievementIds.contains(ach.id)
                    AchievementRow(
                        icon = ach.icon,
                        title = ach.title,
                        description = ach.description,
                        unlocked = unlocked
                    )
                    Spacer(Modifier.height(10.dp))
                }

                Spacer(Modifier.height(16.dp))

                Text(
                    "Выполнено раз: $count",
                    fontSize = 13.sp,
                    color = TextMuted,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(12.dp))

                PulsingButton(
                    text = "Подтвердить выполнение +${task.pointsPerCompletion}⭐",
                    onClick = {
                        onComplete()
                    },
                    done = justDone
                )

                Spacer(Modifier.height(32.dp))
            }
        }

        // Floating +points
        if (justDone) {
            FloatingPoints(
                points = task.pointsPerCompletion,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
private fun AchievementRow(
    icon: String,
    title: String,
    description: String,
    unlocked: Boolean
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (unlocked) GreenSurface else Color(0xFFF5F5F5)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                icon,
                fontSize = 28.sp,
                color = if (unlocked) Color.Unspecified else Color.Gray.copy(alpha = 0.4f)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = if (unlocked) TextPrimary else Color.Gray
                )
                Text(
                    description,
                    fontSize = 12.sp,
                    color = if (unlocked) TextSecondary else Color.LightGray
                )
            }
            if (unlocked) {
                Text("✓", fontSize = 18.sp, color = GreenAccent)
            }
        }
    }
}
