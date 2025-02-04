package com.robotbot.financetracker.domain.usecases.settings

import com.robotbot.financetracker.domain.entities.ApplicationThemeSetting
import com.robotbot.financetracker.domain.repotisories.ApplicationSettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ThemeSettingUseCase @Inject constructor(
    private val applicationSettingsRepository: ApplicationSettingsRepository
) {

    suspend fun saveTheme(theme: ApplicationThemeSetting) {
        applicationSettingsRepository.saveThemePreference(theme)
    }

    fun getTheme(): Flow<ApplicationThemeSetting> = applicationSettingsRepository.getThemePreference()
}