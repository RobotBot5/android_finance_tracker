package com.robotbot.financetracker.presentation.profile

import com.robotbot.financetracker.domain.entities.SettingToDisplayEntity

sealed interface ThemeChooseState {
    data object Initial : ThemeChooseState
    data object Loading : ThemeChooseState
    data class Content(val settings: List<SettingToDisplayEntity>) : ThemeChooseState
    data object WorkEnded : ThemeChooseState
    data object Error : ThemeChooseState

}