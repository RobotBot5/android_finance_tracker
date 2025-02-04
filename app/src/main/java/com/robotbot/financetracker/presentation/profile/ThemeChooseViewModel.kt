package com.robotbot.financetracker.presentation.profile

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robotbot.financetracker.domain.entities.ApplicationThemeSetting
import com.robotbot.financetracker.domain.entities.SettingToDisplayEntity
import com.robotbot.financetracker.domain.entities.SettingsEnum
import com.robotbot.financetracker.domain.usecases.settings.ThemeSettingUseCase
import com.robotbot.financetracker.presentation.utils.applyTheme
import com.robotbot.financetracker.presentation.utils.toTranslatedName
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

    private lateinit var settingKey: SettingsEnum

    fun setSettingKey(settingKeyFromFragment: SettingsEnum) {
        settingKey = settingKeyFromFragment
        viewModelScope.launch {
            _state.value = ThemeChooseState.Loading
            when (settingKey) {
                SettingsEnum.THEME -> {
                    themeSettingUseCase.getTheme().collect { theme ->
                        _state.value = ThemeChooseState.Content(
                            ApplicationThemeSetting.entries.map {
                                SettingToDisplayEntity(
                                    name = it.toTranslatedName(application),
                                    value = it.name,
                                    isSelected = it == theme
                                )
                            }
                        )
                    }
                }
            }
        }
    }

    fun setThemeInSettings(settingToDisplayEntity: SettingToDisplayEntity) {
        viewModelScope.launch {
            if (!settingToDisplayEntity.isSelected) {
                when (settingKey) {
                    SettingsEnum.THEME -> {
                        val theme = ApplicationThemeSetting.valueOf(settingToDisplayEntity.value)
                        themeSettingUseCase.saveTheme(theme)
                        applyTheme(theme)
                    }
                }
            }
        }
    }
}