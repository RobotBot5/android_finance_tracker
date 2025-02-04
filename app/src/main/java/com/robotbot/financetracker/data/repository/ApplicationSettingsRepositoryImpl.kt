package com.robotbot.financetracker.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.robotbot.financetracker.domain.entities.ApplicationSettingsEntity
import com.robotbot.financetracker.domain.entities.ApplicationThemeSetting
import com.robotbot.financetracker.domain.repotisories.ApplicationSettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ApplicationSettingsRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : ApplicationSettingsRepository {

    companion object {
        val KEY_THEME = stringPreferencesKey("theme")
    }

    override suspend fun saveThemePreference(theme: ApplicationThemeSetting) {
        dataStore.edit {
            it[KEY_THEME] = theme.name
        }
    }

    override fun getThemePreference(): Flow<ApplicationThemeSetting> =
        dataStore.data.map { preferences ->
            preferences[KEY_THEME]?.let { ApplicationThemeSetting.valueOf(it) }
                ?: ApplicationThemeSetting.SYSTEM
        }

    override fun getAllSettings(): Flow<ApplicationSettingsEntity> = dataStore.data.map {
        val theme = it[KEY_THEME]?.let { ApplicationThemeSetting.valueOf(it) }
            ?: ApplicationThemeSetting.SYSTEM
        ApplicationSettingsEntity(
            theme = theme
        )
    }
}