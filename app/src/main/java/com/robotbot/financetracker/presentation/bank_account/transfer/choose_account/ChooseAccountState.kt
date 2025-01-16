package com.robotbot.financetracker.presentation.bank_account.transfer.choose_account

import com.robotbot.financetracker.domain.entities.BankAccountEntity

sealed interface ChooseAccountState {

    data object Initial : ChooseAccountState

    data object Loading : ChooseAccountState

    data class Loaded(val bankAccounts: List<BankAccountEntity>) : ChooseAccountState
}