package com.robotbot.financetracker.presentation

import com.robotbot.financetracker.domain.entities.BankAccountEntity
import com.robotbot.financetracker.domain.entities.Currency

data class AccountManagementState(
    val displayState: AccountManagementDisplayState = AccountManagementDisplayState.Content(),
    val selectedCurrency: Currency = Currency.RUB,
    val accountToDeleteName: String? = null
)

sealed interface AccountManagementDisplayState {
    data class InitialEditMode(
        val accountEntity: BankAccountEntity
    ) : AccountManagementDisplayState
    data object Loading : AccountManagementDisplayState
    data class Content(
        val nameError: String? = null,
        val balanceError: String? = null
    ) : AccountManagementDisplayState
    data object WorkEnded : AccountManagementDisplayState
}

