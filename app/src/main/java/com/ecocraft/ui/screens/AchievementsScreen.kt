package com.ecocraft.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ecocraft.data.EcoData
import com.ecocraft.data.UserState
import com.ecocraft.ui.components.GradientHeader
import com.ecocraft.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AchievementsScreen(
    userState: UserState,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val allAchievements = EcoData.tasks.flatMap { task ->
        task.achievements.map { ach -> Pair(task.icon, ach) }
    }
    val unlockedIds = userState.unlockedAchievementIds

    Column(modifier = modifier.fillMaxSize().background(GreenBackground)) {

        GradientHeader {
            IconButton(
                onClick = onNavigateBack,
                modifier = Modifier.offset(x = (-12).dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White)
            }
            Text("Коллекция", fontSize = 12.sp, letterSpacing = 2.sp, color = Color.White.copy(alpha = 0.7f))
            Text("Достижения", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(Modifier.height(4.dp))
            Text(
                "Разблокировано: ${unlockedIds.size} из ${allAchievements.size}",
                fontSize = 13.sp,
                color = Color.White.copy(alpha = 0.8f)
            )
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(20.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(allAchievements) { (taskIcon, ach) ->
                val unlocked = unlockedIds.contains(ach.id)
                AchievementGridCard(
                    icon = ach.icon,
                    taskIcon = taskIcon,
                    title = ach.title,
                    description = ach.description,
                    unlocked = unlocked
                )
            }
        }

        // Share row
        Card(
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
            colors = CardDefaults.cardColors(containerColor = GreenMint),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .navigationBarsPadding(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "🌍 Покажи достижения друзьям!",
                    fontSize = 13.sp,
                    color = TextSecondary,
                    modifier = Modifier.weight(1f)
                )
                FilledTonalButton(
                    onClick = { /* Share intent */ },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Share, null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Поделиться")
                }
            }
        }
    }
}

@Composable
private fun AchievementGridCard(
    icon: String,
    taskIcon: String,
    title: String,
    description: String,
    unlocked: Boolean
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (unlocked) GreenSurface else Color(0xFFF5F5F5)
        ),
        elevation = CardDefaults.cardElevation(if (unlocked) 3.dp else 0.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                icon,
                fontSize = 36.sp,
                color = if (unlocked) Color.Unspecified else Color.Gray.copy(alpha = 0.35f)
            )
            Spacer(Modifier.height(4.dp))
            Text(taskIcon, fontSize = 18.sp)
            Spacer(Modifier.height(6.dp))
            Text(
                title,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = if (unlocked) TextPrimary else Color.LightGray,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(4.dp))
            Text(
                description,
                fontSize = 11.sp,
                color = if (unlocked) TextSecondary else Color.LightGray,
                textAlign = TextAlign.Center,
                lineHeight = 15.sp
            )
            if (unlocked) {
                Spacer(Modifier.height(8.dp))
                Text("✓ Получено", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GreenAccent)
            }
        }
    }
}
