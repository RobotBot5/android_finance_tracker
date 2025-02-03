package com.robotbot.financetracker.domain.usecases.settings

import com.robotbot.financetracker.domain.entities.ApplicationThemeSetting
import com.robotbot.financetracker.domain.repotisories.ApplicationSettingsRepository
import javax.inject.Inject

class ThemeSettingUseCase @Inject constructor(
    private val applicationSettingsRepository: ApplicationSettingsRepository
) {

    suspend fun saveTheme(theme: ApplicationThemeSetting) {
        applicationSettingsRepository.saveThemePreference(theme)
    }

    suspend fun getTheme(): ApplicationThemeSetting = applicationSettingsRepository.getThemePreference()
}