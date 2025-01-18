package com.robotbot.financetracker.presentation.bank_account.transfer

import com.robotbot.financetracker.domain.entities.BankAccountEntity
import java.math.BigDecimal

data class CreateTransferState(

    val displayState: CreateTransferDisplayState = CreateTransferDisplayState.SingleCurrency,
    val accountFrom: BankAccountEntity? = null,
    val accountTo: BankAccountEntity? = null,
    val amountFrom: BigDecimal? = null
) {
    val saveButtonEnabled: Boolean
        get() = accountFrom != null && accountTo != null && amountFrom != null

}

sealed interface CreateTransferDisplayState {

    data object SingleCurrency : CreateTransferDisplayState

    data class DifferentCurrencies(val amountToPlaceholder: String) : CreateTransferDisplayState

    data class Error(val errorState: ErrorState) : CreateTransferDisplayState

    data object WorkEnded : CreateTransferDisplayState
}

sealed interface ErrorState {

    data object UnknownError : ErrorState

    data object InsufficientFunds : ErrorState

    data object InvalidTransfer : ErrorState
}