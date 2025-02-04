package com.robotbot.financetracker.presentation.utils

import android.app.Application
import com.robotbot.financetracker.R
import com.robotbot.financetracker.domain.entities.ApplicationThemeSetting

fun ApplicationThemeSetting.toTranslatedName(application: Application): String =
    when (this) {
        ApplicationThemeSetting.DARK -> {
            application.getString(R.string.theme_dark)
        }

        ApplicationThemeSetting.LIGHT -> {
            application.getString(R.string.theme_light)
        }

        ApplicationThemeSetting.SYSTEM -> {
            application.getString(R.string.theme_system)
        }
    }