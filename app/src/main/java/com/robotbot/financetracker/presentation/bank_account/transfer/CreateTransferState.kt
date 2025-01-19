package com.robotbot.financetracker.presentation.bank_account.transfer

import android.util.Log
import com.robotbot.financetracker.domain.entities.BankAccountEntity
import java.math.BigDecimal

data class CreateTransferState(

    val displayState: CreateTransferDisplayState = CreateTransferDisplayState.SingleCurrency,
    val accountFrom: BankAccountEntity? = null,
    val accountTo: BankAccountEntity? = null,
    val amountFrom: BigDecimal? = null
) {
    val saveButtonEnabled: Boolean
        get() {
            Log.d("CreateTransferState", displayState.toString())
            return accountFrom != null && accountTo != null && amountFrom != null &&
                    (displayState !is CreateTransferDisplayState.DifferentCurrencies ||
                            (displayState.amountTo != null || displayState.amountToPlaceholder != BigDecimal.ZERO))
        }

}

sealed interface CreateTransferDisplayState {

    data object SingleCurrency : CreateTransferDisplayState

    data class DifferentCurrencies(
        val amountToPlaceholder: BigDecimal,
        val amountTo: BigDecimal? = null
    ) : CreateTransferDisplayState

    data class Error(val errorState: ErrorState) : CreateTransferDisplayState

    data object WorkEnded : CreateTransferDisplayState
}

sealed interface ErrorState {

    data object UnknownError : ErrorState

    data object InsufficientFunds : ErrorState

    data object InvalidTransfer : ErrorState
}