package com.robotbot.financetracker.presentation

import android.app.Application
import com.robotbot.financetracker.di.DaggerApplicationComponent
import com.robotbot.financetracker.domain.usecases.settings.ThemeSettingUseCase
import com.robotbot.financetracker.presentation.utils.applyTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class FinanceTrackerApp : Application() {

    val component by lazy {
        DaggerApplicationComponent.factory().create(this)
    }

    @Inject
    lateinit var themeSettingUseCase: ThemeSettingUseCase

    override fun onCreate() {
        component.inject(this)
        super.onCreate()
        val theme = runBlocking { themeSettingUseCase.getTheme().first() }
        applyTheme(theme)
    }
}