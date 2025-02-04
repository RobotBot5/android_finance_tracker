package com.robotbot.financetracker.domain.repotisories

import com.robotbot.financetracker.domain.entities.ApplicationSettingsEntity
import com.robotbot.financetracker.domain.entities.ApplicationThemeSetting
import kotlinx.coroutines.flow.Flow

interface ApplicationSettingsRepository {

    suspend fun saveThemePreference(theme: ApplicationThemeSetting)

    fun getThemePreference(): Flow<ApplicationThemeSetting>

    fun getAllSettings(): Flow<ApplicationSettingsEntity>
}