package com.ecocraft.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.ecocraft.data.EcoData
import com.ecocraft.data.UserState
import com.ecocraft.ui.components.AvatarCircle
import com.ecocraft.ui.components.EcoProgressBar
import com.ecocraft.ui.components.GradientHeader
import com.ecocraft.ui.components.SectionLabel
import com.ecocraft.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    userState: UserState,
    onNavigateBack: () -> Unit,
    onUpdateUsername: (String) -> Unit,
    onUpdateAvatar: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val (level, nextLevel, progress) = EcoData.getLevelInfo(userState.points)
    val avatar = EcoData.avatars.getOrElse(userState.avatarIndex) { "🌱" }

    var showNameDialog by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxSize().background(GreenBackground)) {

        // ── Header ────────────────────────────────────────────────────────
        GradientHeader {
            IconButton(
                onClick = onNavigateBack,
                modifier = Modifier.offset(x = (-12).dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                AvatarCircle(emoji = avatar, size = 80)
                Column {
                    Text(userState.username, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(level.icon, fontSize = 16.sp)
                        Text(level.name, fontSize = 14.sp, color = Color.White.copy(alpha = 0.85f))
                        Text("· ${userState.points} ⭐", fontSize = 13.sp, color = Color.White.copy(alpha = 0.7f))
                    }
                    Spacer(Modifier.height(10.dp))
                    FilledTonalButton(
                        onClick = { showNameDialog = true },
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 4.dp),
                        modifier = Modifier.height(32.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = Color.White.copy(alpha = 0.2f),
                            contentColor = Color.White
                        )
                    ) {
                        Icon(Icons.Default.Edit, null, modifier = Modifier.size(14.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("Изменить имя", fontSize = 12.sp)
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {

            // ── Avatar picker ─────────────────────────────────────────────
            SectionLabel("Выбери аватар")
            Spacer(Modifier.height(14.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.height(200.dp),
                userScrollEnabled = false
            ) {
                itemsIndexed(EcoData.avatars) { index, av ->
                    val selected = index == userState.avatarIndex
                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (selected) GreenSurface else Color(0xFFF5F5F5))
                            .border(
                                width = if (selected) 2.dp else 0.dp,
                                color = if (selected) GreenAccent else Color.Transparent,
                                shape = RoundedCornerShape(16.dp)
                            )
                            .clickable { onUpdateAvatar(index) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(av, fontSize = 34.sp)
                    }
                }
            }

            Spacer(Modifier.height(28.dp))

            // ── Level progress ────────────────────────────────────────────
            SectionLabel("Прогресс уровня")
            Spacer(Modifier.height(14.dp))

            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = GreenMint),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    // Level icons row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        EcoData.levels.forEach { lv ->
                            val reached = userState.points >= lv.minPoints
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    lv.icon,
                                    fontSize = 22.sp,
                                    color = if (reached) Color.Unspecified else Color.Gray.copy(alpha = 0.35f)
                                )
                                Text(
                                    lv.name,
                                    fontSize = 9.sp,
                                    color = if (reached) TextSecondary else Color.LightGray,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(14.dp))

                    EcoProgressBar(
                        progress = progress,
                        trackColor = GreenPastel,
                        fillColor = GreenMedium
                    )

                    Spacer(Modifier.height(10.dp))
                    Text(
                        if (nextLevel != null)
                            "${userState.points} / ${nextLevel.minPoints} до «${nextLevel.name}»"
                        else "Максимальный уровень достигнут! 🏆",
                        fontSize = 13.sp,
                        color = TextSecondary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(Modifier.height(32.dp))
        }
    }

    // ── Name edit dialog ──────────────────────────────────────────────────
    if (showNameDialog) {
        var tempName by remember { mutableStateOf(userState.username) }
        Dialog(onDismissRequest = { showNameDialog = false }) {
            Card(shape = RoundedCornerShape(20.dp)) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text("Изменить имя", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = TextPrimary)
                    Spacer(Modifier.height(16.dp))
                    OutlinedTextField(
                        value = tempName,
                        onValueChange = { tempName = it },
                        label = { Text("Имя") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = GreenMedium,
                            focusedLabelColor = GreenMedium
                        )
                    )
                    Spacer(Modifier.height(20.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = { showNameDialog = false },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        ) { Text("Отмена") }
                        Button(
                            onClick = { onUpdateUsername(tempName); showNameDialog = false },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = GreenMedium)
                        ) { Text("Сохранить") }
                    }
                }
            }
        }
    }
}
