package com.robotbot.financetracker.presentation.profile

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robotbot.financetracker.R
import com.robotbot.financetracker.domain.entities.ApplicationThemeSetting
import com.robotbot.financetracker.domain.entities.SettingToDisplayEntity
import com.robotbot.financetracker.domain.usecases.settings.ThemeSettingUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ThemeChooseViewModel @Inject constructor(
    private val application: Application,
    private val themeSettingUseCase: ThemeSettingUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<ThemeChooseState>(ThemeChooseState.Initial)
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.value = ThemeChooseState.Loading
            val theme = themeSettingUseCase.getTheme()
            _state.value = ThemeChooseState.Content(
                ApplicationThemeSetting.entries.map {
                    SettingToDisplayEntity(
                        name = it.getLocaleName(),
                        value = it.name,
                        isSelected = it == theme
                    )
                }
            )
        }

    }

    private fun ApplicationThemeSetting.getLocaleName(): String =
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

    fun setThemeInSettings(settingToDisplayEntity: SettingToDisplayEntity) {
        viewModelScope.launch {
            if (!settingToDisplayEntity.isSelected) {
                themeSettingUseCase.saveTheme(ApplicationThemeSetting.valueOf(settingToDisplayEntity.value))
                _state.value = ThemeChooseState.WorkEnded
            }
        }
    }
}