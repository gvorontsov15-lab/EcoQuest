package com.ecocraft.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "ecoquest_prefs")

class UserRepository(private val context: Context) {

    companion object {
        private val USER_STATE_KEY = stringPreferencesKey("user_state")
    }

    val userStateFlow: Flow<UserState> = context.dataStore.data.map { prefs ->
        val json = prefs[USER_STATE_KEY]
        if (json != null) {
            try { Json.decodeFromString(json) } catch (e: Exception) { UserState() }
        } else {
            UserState()
        }
    }

    suspend fun saveUserState(state: UserState) {
        context.dataStore.edit { prefs ->
            prefs[USER_STATE_KEY] = Json.encodeToString(state)
        }
    }
}
