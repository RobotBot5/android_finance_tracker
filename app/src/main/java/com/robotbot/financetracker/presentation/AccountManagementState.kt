package com.robotbot.financetracker.presentation

import com.robotbot.financetracker.domain.entities.BankAccountEntity
import com.robotbot.financetracker.domain.entities.Currency

data class AccountManagementState(
    val displayState: DisplayState = DisplayState.Content(),
    val selectedCurrency: Currency = Currency.RUB,
)

sealed interface DisplayState {
    data class InitialEditMode(
        val accountEntity: BankAccountEntity
    ) : DisplayState
    data object Loading : DisplayState
    data class Content(
        val nameError: String? = null,
        val balanceError: String? = null
    ) : DisplayState
    data object WorkEnded : DisplayState
}

