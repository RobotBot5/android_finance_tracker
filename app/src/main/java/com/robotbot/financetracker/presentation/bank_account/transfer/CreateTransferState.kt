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

    data class Error(val errorState: ErrorState) : CreateTransferDisplayState

    data object WorkEnded : CreateTransferDisplayState
}

sealed interface ErrorState {

    data object UnknownError : ErrorState

    data object InsufficientFunds : ErrorState

    data object InvalidTransfer : ErrorState
}