package com.robotbot.financetracker.presentation.profile

import android.app.Application
import com.robotbot.financetracker.domain.entities.ApplicationSettingsEntity
import com.robotbot.financetracker.presentation.utils.toTranslatedName

sealed interface SettingsState {

    data object Initial : SettingsState

    data object Loading : SettingsState

    data object Error : SettingsState

    data class Content(val translatedSettings: TranslatedSettings) : SettingsState
}

data class TranslatedSettings(
    val theme: String
)

fun ApplicationSettingsEntity.toTranslatedSettings(application: Application) =
    TranslatedSettings(
        theme = this.theme.toTranslatedName(application)
    )