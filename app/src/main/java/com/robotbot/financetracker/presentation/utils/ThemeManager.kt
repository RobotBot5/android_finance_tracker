package com.robotbot.financetracker.presentation.utils

import androidx.appcompat.app.AppCompatDelegate
import com.robotbot.financetracker.domain.entities.ApplicationThemeSetting

fun applyTheme(themeSetting: ApplicationThemeSetting) {
    val mode = when (themeSetting) {
        ApplicationThemeSetting.DARK -> AppCompatDelegate.MODE_NIGHT_YES
        ApplicationThemeSetting.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
        ApplicationThemeSetting.SYSTEM -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
    }
    AppCompatDelegate.setDefaultNightMode(mode)
}
