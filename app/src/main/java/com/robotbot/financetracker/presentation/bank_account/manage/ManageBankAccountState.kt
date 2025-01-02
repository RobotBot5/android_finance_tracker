package com.robotbot.financetracker.presentation.bank_account.manage

import com.robotbot.financetracker.domain.entities.BankAccountEntity
import com.robotbot.financetracker.domain.entities.Currency

data class ManageBankAccountState(
    val displayState: ManageBankAccountDisplayState = ManageBankAccountDisplayState.Content(),
    val selectedCurrency: Currency = Currency.RUB,
    val accountToDeleteName: String? = null
)

sealed interface ManageBankAccountDisplayState {
    data class InitialEditMode(
        val accountEntity: BankAccountEntity
    ) : ManageBankAccountDisplayState
    data object Loading : ManageBankAccountDisplayState
    data class Content(
        val nameError: String? = null,
        val balanceError: String? = null
    ) : ManageBankAccountDisplayState
    data object WorkEnded : ManageBankAccountDisplayState
}

