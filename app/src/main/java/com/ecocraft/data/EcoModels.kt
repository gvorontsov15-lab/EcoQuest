package com.ecoquest.data

import kotlinx.serialization.Serializable

// ─── Achievement ────────────────────────────────────────────────────────────

data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val targetCount: Int,
    val icon: String,
    val taskId: String
)

// ─── Task ───────────────────────────────────────────────────────────────────

data class EcoTask(
    val id: String,
    val icon: String,
    val title: String,
    val subtitle: String,
    val description: String,
    val pointsPerCompletion: Int,
    val achievements: List<Achievement>
)

// ─── Level ──────────────────────────────────────────────────────────────────

data class Level(
    val name: String,
    val minPoints: Int,
    val icon: String
)

// ─── Persisted user state (DataStore) ───────────────────────────────────────

@Serializable
data class UserState(
    val username: String = "Эко-путник",
    val avatarIndex: Int = 0,
    val points: Int = 0,
    val taskCounts: Map<String, Int> = emptyMap(),
    val unlockedAchievementIds: List<String> = emptyList()
)

// ─── Static data ────────────────────────────────────────────────────────────

object EcoData {

    val avatars = listOf("🌱", "🦋", "🐝", "🌸", "🐢", "🦅", "🌊", "🦁")

    val levels = listOf(
        Level("Новичок",   0,    "🌱"),
        Level("Эко-друг",  100,  "🌿"),
        Level("Защитник",  300,  "🌲"),
        Level("Эко-герой", 700,  "🦋"),
        Level("Хранитель", 1500, "🌍")
    )

    val tasks = listOf(
        EcoTask(
            id = "batteries",
            icon = "🔋",
            title = "Сдать батарейки",
            subtitle = "Recycling",
            description = "Отнесите использованные батарейки в специальный пункт приёма.\n\n" +
                    "Батарейки содержат токсичные вещества: свинец, кадмий и ртуть, " +
                    "которые разрушают почву и загрязняют воду.\n\n" +
                    "Каждая сданная батарейка — реальный вклад в здоровье планеты!",
            pointsPerCompletion = 30,
            achievements = listOf(
                Achievement("bat_1",  "Первый шаг",     "Сдать батарейки 1 раз",  1,  "⚡", "batteries"),
                Achievement("bat_5",  "Эко-герой",      "Сдать батарейки 5 раз",  5,  "🌿", "batteries"),
                Achievement("bat_15", "Хранитель земли","Сдать батарейки 15 раз", 15, "🏆", "batteries")
            )
        ),
        EcoTask(
            id = "light",
            icon = "💡",
            title = "Выключить свет",
            subtitle = "Energy Save",
            description = "Выключите свет во всех неиспользуемых комнатах.\n\n" +
                    "Экономия электроэнергии снижает выбросы CO₂ и уменьшает " +
                    "нагрузку на электростанции.\n\n" +
                    "Маленькая привычка — большой результат для всей планеты!",
            pointsPerCompletion = 10,
            achievements = listOf(
                Achievement("light_1",  "Бережливый", "Выключить свет 1 раз",  1,  "💡", "light"),
                Achievement("light_10", "Экономист",  "Выключить свет 10 раз", 10, "⚡", "light"),
                Achievement("light_30", "Эко-мастер", "Выключить свет 30 раз", 30, "🌟", "light")
            )
        )
    )

    fun getLevelInfo(points: Int): Triple<Level, Level?, Float> {
        var current = levels.first()
        var nextIdx = 1
        for (i in levels.indices.reversed()) {
            if (points >= levels[i].minPoints) {
                current = levels[i]
                nextIdx = i + 1
                break
            }
        }
        val next = levels.getOrNull(nextIdx)
        val progress = if (next != null) {
            ((points - current.minPoints).toFloat() / (next.minPoints - current.minPoints)).coerceIn(0f, 1f)
        } else 1f
        return Triple(current, next, progress)
    }
}
