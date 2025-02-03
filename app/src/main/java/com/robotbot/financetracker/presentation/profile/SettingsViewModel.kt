package com.robotbot.financetracker.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robotbot.financetracker.domain.usecases.settings.GetAllSettingsUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    getAllSettingsUseCase: GetAllSettingsUseCase
) : ViewModel() {

    val state = getAllSettingsUseCase()
        .onStart { SettingsState.Loading }
        .map { SettingsState.Content(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = SettingsState.Initial
        )
}