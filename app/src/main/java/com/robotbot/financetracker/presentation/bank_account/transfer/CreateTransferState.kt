package com.robotbot.financetracker.presentation.bank_account.transfer

import com.robotbot.financetracker.domain.entities.BankAccountEntity

data class CreateTransferState(
    val accountFrom: BankAccountEntity? = null,
    val accountTo: BankAccountEntity? = null,
    val saveButtonEnabled: Boolean = false,
    val displayState: CreateTransferDisplayState = CreateTransferDisplayState.Initial
)

sealed interface CreateTransferDisplayState {
    data object Initial : CreateTransferDisplayState
    data object Loading : CreateTransferDisplayState
    data object Loaded : CreateTransferDisplayState
}