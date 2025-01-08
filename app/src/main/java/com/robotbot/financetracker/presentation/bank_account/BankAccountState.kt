package com.robotbot.financetracker.presentation.bank_account

import com.robotbot.financetracker.domain.entities.BankAccountEntity

data class BankAccountState(
    val displayState: BankAccountDisplayState
)

sealed interface BankAccountDisplayState {
    data object Initial : BankAccountDisplayState
    data object Loading : BankAccountDisplayState
    data class Content(
        val accounts: List<BankAccountEntity>,
        val totalBalance: Double
    ) : BankAccountDisplayState
}