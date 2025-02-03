package com.robotbot.financetracker.presentation.profile

import com.robotbot.financetracker.domain.entities.ApplicationSettingsEntity

sealed interface SettingsState {

    data object Initial : SettingsState

    data object Loading : SettingsState

    data object Error : SettingsState

    data class Content(val applicationSettings: ApplicationSettingsEntity) : SettingsState
}
