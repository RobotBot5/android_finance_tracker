package com.robotbot.financetracker.presentation.bank_account

import com.robotbot.financetracker.domain.entities.BankAccountEntity

data class BankAccountState(
    val bankAccountListState: BankAccountListState,
    val totalBalanceState: TotalBalanceState
)

sealed interface BankAccountListState {
    data object Initial : BankAccountListState
    data object Loading : BankAccountListState
    data class Content(val accounts: List<BankAccountEntity>) : BankAccountListState
}

sealed interface TotalBalanceState {
    data object Initial : TotalBalanceState
    data object Loading : TotalBalanceState
    data object Error : TotalBalanceState
    data class Content(val totalBalance: Double) : TotalBalanceState
}