package com.robotbot.financetracker.presentation.bank_account.transfer

import com.robotbot.financetracker.domain.entities.BankAccountEntity

data class CreateTransferState(
    val displayState: CreateTransferDisplayState = CreateTransferDisplayState.Content,
    val accountFrom: BankAccountEntity? = null,
    val accountTo: BankAccountEntity? = null,
    val saveButtonEnabled: Boolean = false
)

sealed interface CreateTransferDisplayState {
    data object Content : CreateTransferDisplayState

    data object Error : CreateTransferDisplayState

    data object WorkEnded : CreateTransferDisplayState
}